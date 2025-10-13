package dao;

import model.Movie;
import model.BookingSeat;
import model.Ticket;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

public class TicketDAO {

    public double getTotalRevenue(LocalDateTime start, LocalDateTime end, List<Long> cinemaIds) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String query = "SELECT SUM(t.price) FROM Ticket t JOIN t.showtime s JOIN s.auditorium a WHERE t.createdAt BETWEEN :start AND :end";
            if (cinemaIds != null && !cinemaIds.isEmpty()) {
                query += " AND a.cinema.id IN :cinemaIds";
            }
            Query jpql = em.createQuery(query);
            jpql.setParameter("start", start);
            jpql.setParameter("end", end);
            if (cinemaIds != null && !cinemaIds.isEmpty()) {
                jpql.setParameter("cinemaIds", cinemaIds);
            }
            Double result = (Double) jpql.getSingleResult();
            return result != null ? result : 0.0;
        } finally {
            em.close();
        }
    }

    public int getTotalTickets(LocalDateTime start, LocalDateTime end, List<Long> cinemaIds) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String query = "SELECT COUNT(t.id) FROM Ticket t JOIN t.showtime s JOIN s.auditorium a WHERE t.createdAt BETWEEN :start AND :end";
            if (cinemaIds != null && !cinemaIds.isEmpty()) {
                query += " AND a.cinema.id IN :cinemaIds";
            }
            Query jpql = em.createQuery(query);
            jpql.setParameter("start", start);
            jpql.setParameter("end", end);
            if (cinemaIds != null && !cinemaIds.isEmpty()) {
                jpql.setParameter("cinemaIds", cinemaIds);
            }
            Long result = (Long) jpql.getSingleResult();
            return result.intValue();
        } finally {
            em.close();
        }
    }

    public List<Map<String, Object>> getRevenueByDay(LocalDateTime start, LocalDateTime end, List<Long> cinemaIds) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String query = "SELECT DATE(t.createdat) as date, SUM(t.price) as revenue " +
                    "FROM ticket t JOIN showtime s ON t.showtimeid = s.id JOIN auditorium a ON s.auditid = a.id " +
                    "WHERE t.createdat BETWEEN :start AND :end";
            if (cinemaIds != null && !cinemaIds.isEmpty()) {
                query += " AND a.cinemaid IN :cinemaIds";
            }
            query += " GROUP BY DATE(t.createdat)";
            Query nativeQuery = em.createNativeQuery(query);
            nativeQuery.setParameter("start", start);
            nativeQuery.setParameter("end", end);
            if (cinemaIds != null && !cinemaIds.isEmpty()) {
                nativeQuery.setParameter("cinemaIds", cinemaIds);
            }
            List<Object[]> results = nativeQuery.getResultList();
            List<Map<String, Object>> revenueByDay = new ArrayList<>();
            for (Object[] row : results) {
                Map<String, Object> map = new HashMap<>();
                map.put("date", row[0].toString());
                map.put("revenue", ((Number) row[1]).doubleValue());
                revenueByDay.add(map);
            }
            return revenueByDay;
        } finally {
            em.close();
        }
    }

    public List<Map<String, Object>> getRevenueByCinema(LocalDateTime start, LocalDateTime end, List<Long> cinemaIds) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String query = "SELECT a.cinemaid, c.name, SUM(t.price) as revenue " +
                    "FROM ticket t JOIN showtime s ON t.showtimeid = s.id JOIN auditorium a ON s.auditid = a.id JOIN cinema c ON a.cinemaid = c.id " +
                    "WHERE t.createdat BETWEEN :start AND :end";
            if (cinemaIds != null && !cinemaIds.isEmpty()) {
                query += " AND a.cinemaid IN :cinemaIds";
            }
            query += " GROUP BY a.cinemaid, c.name";
            Query nativeQuery = em.createNativeQuery(query);
            nativeQuery.setParameter("start", start);
            nativeQuery.setParameter("end", end);
            if (cinemaIds != null && !cinemaIds.isEmpty()) {
                nativeQuery.setParameter("cinemaIds", cinemaIds);
            }
            List<Object[]> results = nativeQuery.getResultList();
            List<Map<String, Object>> revenueByCinema = new ArrayList<>();
            for (Object[] row : results) {
                Map<String, Object> map = new HashMap<>();
                map.put("cinemaId", ((Number) row[0]).longValue());
                map.put("cinemaName", row[1].toString());
                map.put("revenue", ((Number) row[2]).doubleValue());
                revenueByCinema.add(map);
            }
            return revenueByCinema;
        } finally {
            em.close();
        }
    }

    public List<Movie> getTopMovies(int limit, LocalDateTime start, LocalDateTime end, List<Long> cinemaIds) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String query = "SELECT m FROM Ticket t JOIN t.showtime s JOIN s.movie m JOIN s.auditorium a " +
                    "WHERE t.createdAt BETWEEN :start AND :end";
            if (cinemaIds != null && !cinemaIds.isEmpty()) {
                query += " AND a.cinema.id IN :cinemaIds";
            }
            query += " GROUP BY m ORDER BY COUNT(t.id) DESC";
            Query jpql = em.createQuery(query);
            jpql.setParameter("start", start);
            jpql.setParameter("end", end);
            if (cinemaIds != null && !cinemaIds.isEmpty()) {
                jpql.setParameter("cinemaIds", cinemaIds);
            }
            jpql.setMaxResults(limit);
            return jpql.getResultList();
        } finally {
            em.close();
        }
    }

    public int getTicketsSoldForMovie(long movieId, LocalDateTime start, LocalDateTime end, List<Long> cinemaIds) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String query = "SELECT COUNT(t.id) FROM Ticket t JOIN t.showtime s JOIN s.movie m JOIN s.auditorium a " +
                    "WHERE m.id = :movieId AND t.createdAt BETWEEN :start AND :end";
            if (cinemaIds != null && !cinemaIds.isEmpty()) {
                query += " AND a.cinema.id IN :cinemaIds";
            }
            Query jpql = em.createQuery(query);
            jpql.setParameter("movieId", movieId);
            jpql.setParameter("start", start);
            jpql.setParameter("end", end);
            if (cinemaIds != null && !cinemaIds.isEmpty()) {
                jpql.setParameter("cinemaIds", cinemaIds);
            }
            Long result = (Long) jpql.getSingleResult();
            return result.intValue();
        } finally {
            em.close();
        }
    }

    public double getRevenueForMovie(long movieId, LocalDateTime start, LocalDateTime end, List<Long> cinemaIds) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String query = "SELECT SUM(t.price) FROM Ticket t JOIN t.showtime s JOIN s.movie m JOIN s.auditorium a " +
                    "WHERE m.id = :movieId AND t.createdAt BETWEEN :start AND :end";
            if (cinemaIds != null && !cinemaIds.isEmpty()) {
                query += " AND a.cinema.id IN :cinemaIds";
            }
            Query jpql = em.createQuery(query);
            jpql.setParameter("movieId", movieId);
            jpql.setParameter("start", start);
            jpql.setParameter("end", end);
            if (cinemaIds != null && !cinemaIds.isEmpty()) {
                jpql.setParameter("cinemaIds", cinemaIds);
            }
            Double result = (Double) jpql.getSingleResult();
            return result != null ? result : 0.0;
        } finally {
            em.close();
        }
    }

    public List<Ticket> getTicketByCustomer(long customerId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        String jpql = "SELECT t FROM Booking b " +
                "JOIN b.tickets t " +
                "JOIN t.showtime st " +
                "WHERE b.customer.id = :customerId " +
                "ORDER BY st.startTime DESC";
        TypedQuery<Ticket> q = em.createQuery(jpql, Ticket.class);
        q.setParameter("customerId", customerId);
        List<Ticket> result = q.getResultList();
        em.close();
        return result;
    }
}