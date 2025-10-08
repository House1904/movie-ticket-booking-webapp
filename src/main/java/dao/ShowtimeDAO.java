package dao;

import model.Cinema;
import model.Showtime;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class ShowtimeDAO {
    public List<Showtime> getshowtimeList(){
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        List<Showtime> showtimes = null;

        try {
            showtimes = entity.createQuery("SELECT s FROM Showtime s", Showtime.class)
                    .getResultList();
        } finally {
            entity.close();
        }

        return showtimes;
    }
    public Showtime showtime(long id){
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        String query = "SELECT s FROM Showtime s WHERE s.id = :id";
        TypedQuery<Showtime> query1 = entity.createQuery(query, Showtime.class);
        query1.setParameter("id", id);
        return query1.getSingleResult();
    }
    public List<Showtime> getShowtimesByCinema(long cinemaId, LocalDate date) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT s FROM Showtime s " +
                    "JOIN FETCH s.movie m " +
                    "JOIN FETCH s.auditorium a " +
                    "JOIN FETCH a.cinema c " +
                    "WHERE c.id = :cinemaId " +
                    "AND DATE(s.startTime) = :selectedDate " +
                    "ORDER BY m.id, s.startTime";

            return em.createQuery(jpql, Showtime.class)
                    .setParameter("cinemaId", cinemaId)
                    .setParameter("selectedDate", date)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
