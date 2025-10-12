package dao;

import model.Booking;
import model.BookingSeat;
import model.Seat;
import model.Showtime;
import model.enums.SeatBookedFormat;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BookingDAO {
    public void insertSeat(BookingSeat bookingSeat) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(bookingSeat);
            tx.commit();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            tx.rollback();
        }
        finally {
            em.close();
        }
    }
    public List<BookingSeat> getBookingSeat(long auditID, long showtimeID) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        String query = "SELECT b FROM BookingSeat b " +
                "JOIN FETCH b.auditorium a " +
                "JOIN FETCH b.showtime st "  +
                "WHERE a.id = :auditID AND st.id = :showtimeID";
        TypedQuery<BookingSeat> query1 = em.createQuery(query, BookingSeat.class);
        query1.setParameter("auditID", auditID);
        query1.setParameter("showtimeID", showtimeID);
        return query1.getResultList();
    }

    public void deleteBookingSeat() {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        String jpql = "DELETE FROM BookingSeat bs " +
                        "WHERE bs.status = :status " +
                        "AND bs.createdAt < :expireTime";
        em.getTransaction().begin();
        LocalDateTime expireTime = LocalDateTime.now().minusMinutes(5);
        Query query = em.createQuery(jpql);
        query.setParameter("expireTime", expireTime);
        query.setParameter("status", SeatBookedFormat.HOLD);
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    public boolean insertBooking (Booking booking) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(booking);
            tx.commit();
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            tx.rollback();
        }
        return false;
    }

    public void updateBooking(Booking booking) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(booking);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) tx.rollback();
        } finally {
            em.close();
        }
    }

    public Booking getBookingById(long id) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            return em.find(Booking.class, id);
        } finally {
            em.close();
        }
    }

    // 🔍 Tìm BookingSeat theo seatId + showtimeId
    public BookingSeat findBookingSeatBySeatAndShowtime(int seatId, long showtimeId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT bs FROM BookingSeat bs " +
                    "WHERE bs.seat.id = :seatId AND bs.showtime.id = :showtimeId";
            TypedQuery<BookingSeat> query = em.createQuery(jpql, BookingSeat.class);
            query.setParameter("seatId", seatId);  // <-- Integer
            query.setParameter("showtimeId", showtimeId); // <-- Long
            List<BookingSeat> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }


    // 🔄 Cập nhật trạng thái ghế (HOLD → CONFIRMED / EXPIRED)
    public void updateBookingSeatStatus(BookingSeat bookingSeat, model.enums.SeatBookedFormat newStatus) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            bookingSeat.setStatus(newStatus);
            em.merge(bookingSeat);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) tx.rollback();
        } finally {
            em.close();
        }
    }

}
