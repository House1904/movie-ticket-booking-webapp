package dao;

import model.Showtime;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class ShowtimeDAO {

    /* -------------------- LẤY DANH SÁCH THEO PARTNER -------------------- */
    public List<Showtime> getShowtimesByPartner(long partnerId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            return em.createQuery(
                            "SELECT s FROM Showtime s " +
                                    "JOIN FETCH s.movie m " +
                                    "JOIN FETCH s.auditorium a " +
                                    "JOIN FETCH a.cinema c " +
                                    "WHERE c.partner.id = :partnerId " +
                                    "ORDER BY s.startTime DESC", Showtime.class)
                    .setParameter("partnerId", partnerId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /* -------------------- LẤY TẤT CẢ SHOWTIME -------------------- */
    public List<Showtime> getshowtimeList() {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Showtime s", Showtime.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /* -------------------- LẤY SHOWTIME THEO ID -------------------- */
    public Showtime showtime(long id) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Showtime s WHERE s.id = :id", Showtime.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    /* -------------------- LẤY SHOWTIME THEO CINEMA + NGÀY -------------------- */
    public List<Showtime> getShowtimesByCinema(long cinemaId, LocalDate date) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT s FROM Showtime s " +
                    "JOIN FETCH s.movie m " +
                    "JOIN FETCH s.auditorium a " +
                    "JOIN FETCH a.cinema c " +
                    "WHERE c.id = :cinemaId " +
                    "AND FUNCTION('DATE', s.startTime) = :selectedDate " +  // ✅ sửa DATE() -> FUNCTION('DATE', ...)
                    "ORDER BY m.id, s.startTime";

            return em.createQuery(jpql, Showtime.class)
                    .setParameter("cinemaId", cinemaId)
                    .setParameter("selectedDate", date)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /* -------------------- THÊM SUẤT CHIẾU -------------------- */
    public void addShowtime(Showtime showtime) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(showtime);
            em.flush(); // ✅ bắt buộc để insert vào DB trước khi commit
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /* -------------------- CẬP NHẬT SUẤT CHIẾU -------------------- */
    public void updateShowtime(Showtime showtime) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(showtime);
            em.flush(); // ✅ đảm bảo cập nhật xong mới commit
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /* -------------------- XÓA SUẤT CHIẾU -------------------- */
    public void deleteShowtime(long id) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Showtime showtime = em.find(Showtime.class, id);
            if (showtime != null) {
                em.remove(showtime);
            }
            em.flush();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /* -------------------- LẤY SUẤT CHIẾU THEO PHÒNG -------------------- */
    public List<Showtime> getShowtimesByAuditorium(long auditoriumId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            return em.createQuery(
                            "SELECT s FROM Showtime s " +
                                    "JOIN FETCH s.movie m " +
                                    "JOIN FETCH s.auditorium a " +
                                    "JOIN FETCH a.cinema c " +
                                    "WHERE a.id = :auditoriumId " +
                                    "ORDER BY s.startTime", Showtime.class)
                    .setParameter("auditoriumId", auditoriumId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
