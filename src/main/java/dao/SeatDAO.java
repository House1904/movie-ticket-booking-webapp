package dao;

import model.Movie;
import model.Seat;
import model.Showtime;
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
}
