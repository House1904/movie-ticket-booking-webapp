package dao;

import model.*;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class SeatDAO {
    public Seat getSeatbyID(int seatId) {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        Seat seat = null;
        try {
            seat = entity.find(Seat.class, seatId);
        }
        finally {
            entity.close();
        }
        return seat;
    }
    public List<Seat> getSeatByShowtime(long auditID) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT s FROM Seat s " +
                    "JOIN FETCH s.auditorium a " +
                    "WHERE a.id = :auditID " +
                    "order by s.rowLabel";

            return em.createQuery(jpql, Seat.class)
                    .setParameter("auditID", auditID)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean seatExists(long auditoriumId, String rowLabel, String seatNumber) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(s) FROM Seat s WHERE s.auditorium.id = :auditoriumId AND s.rowLabel = :rowLabel AND s.seatNumber = :seatNumber";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("auditoriumId", auditoriumId);
            query.setParameter("rowLabel", rowLabel);
            query.setParameter("seatNumber", seatNumber);
            Long count = query.getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public void save(Seat seat) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(seat);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void update(Seat seat) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(seat);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void delete(int seatId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Seat seat = em.find(Seat.class, seatId);
            if (seat != null) {
                em.remove(seat);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public double getSeatPrice(Seat seat) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();

        String jpql = "SELECT sp.price FROM seatPricing sp " +
                "WHERE sp.seatType = :seatType " +
                "AND sp.auditType = :audType " +
                "AND sp.cinemaBrand = :cinemaBrand";
        double price = 0.0;
        try {
            price = em.createQuery(jpql, Double.class)
                    .setParameter("seatType", seat.getSeatType().name())
                    .setParameter("audType", seat.getAuditorium().getFormat().name())
                    .setParameter("cinemaBrand", seat.getAuditorium().getCinema().getPartner().getBrand())
                    .getSingleResult();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return price;
    }
}
