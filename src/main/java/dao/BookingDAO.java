package dao;

import model.BookingSeat;
import model.Seat;
import model.Showtime;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
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

}
