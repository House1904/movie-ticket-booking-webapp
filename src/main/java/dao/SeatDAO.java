package dao;

import model.*;
import util.DBConnection;

import javax.persistence.EntityManager;
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
