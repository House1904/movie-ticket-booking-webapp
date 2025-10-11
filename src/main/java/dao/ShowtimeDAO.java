package dao;

import model.Cinema;
import model.Showtime;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class ShowtimeDAO {
    public List<Showtime> getShowtimesByPartner(long partnerId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        List<Showtime> showtimes = null;
        try {
            showtimes = em.createQuery(
                            "SELECT s FROM Showtime s " +
                                    "WHERE s.auditorium.cinema.partner.id = :partnerId " +
                                    "ORDER BY s.startTime DESC", Showtime.class)
                    .setParameter("partnerId", partnerId)
                    .getResultList();
        } finally {
            em.close();
        }
        return showtimes;
    }

    public List<Showtime> getshowtimeList() {
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

    public Showtime showtime(long id) {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        String query = "SELECT s FROM Showtime s WHERE s.id = :id";
        TypedQuery<Showtime> query1 = entity.createQuery(query, Showtime.class);
        query1.setParameter("id", id);
        Showtime showtime = null;
        try {
            showtime = query1.getSingleResult();
        } finally {
            entity.close();
        }
        return showtime;
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

    public void addShowtime(Showtime showtime) {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        try {
            entity.getTransaction().begin();
            entity.persist(showtime);
            entity.getTransaction().commit();
        } finally {
            entity.close();
        }
    }

    public void updateShowtime(Showtime showtime) {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        try {
            entity.getTransaction().begin();
            entity.merge(showtime);
            entity.getTransaction().commit();
        } finally {
            entity.close();
        }
    }

    public void deleteShowtime(long id) {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        try {
            entity.getTransaction().begin();
            Showtime showtime = entity.find(Showtime.class, id);
            if (showtime != null) {
                entity.remove(showtime);
            }
            entity.getTransaction().commit();
        }
        finally {
            entity.close();
        }
    }

    public List<Showtime> getShowtimesByAuditorium(long auditoriumId) {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        List<Showtime> showtimes = null;
        try {
            showtimes = entity.createQuery(
                            "SELECT s FROM Showtime s WHERE s.auditorium.id = :auditoriumId ORDER BY s.startTime",
                            Showtime.class)
                    .setParameter("auditoriumId", auditoriumId)
                    .getResultList();
        } finally {
            entity.close();
        }
        return showtimes;
    }

}