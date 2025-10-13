package dao;

import util.DBConnection;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class AdminReportDAO {

    public List<Map<String, Object>> getTodayPartnerRevenue() {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();

        try {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

            String jpql = "SELECT p.id, p.brand, COALESCE(SUM(t.price), 0) "+
                "FROM Ticket t "+
                "JOIN t.showtime s "+
                "JOIN s.auditorium a "+
                "JOIN a.cinema c "+
                "JOIN c.partner p "+
                "WHERE t.createdAt BETWEEN :startOfDay AND :endOfDay "+
                "AND t.status = model.enums.Status.ISSUED "+
                "GROUP BY p.id, p.brand "+
                "ORDER BY SUM(t.price) DESC";

            Query query = em.createQuery(jpql);
            query.setParameter("startOfDay", startOfDay);
            query.setParameter("endOfDay", endOfDay);

            List<Object[]> results = query.getResultList();
            List<Map<String, Object>> revenueByPartner = new ArrayList<>();

            for (Object[] row : results) {
                Map<String, Object> map = new HashMap<>();
                map.put("partnerId", row[0]);
                map.put("partnerName", row[1]);
                map.put("revenue", ((Number) row[2]).doubleValue());
                revenueByPartner.add(map);
            }

            return revenueByPartner;

        } finally {
            em.close();
        }
    }

    public double getTotalRevenueByDate(LocalDate date) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        double totalRevenue = 0.0;

        try {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

            String jpql = "SELECT COALESCE(SUM(t.price), 0) "+
                "FROM Ticket t "+
                "WHERE t.status = model.enums.Status.ISSUED "+
                "AND t.issuedAt BETWEEN :startOfDay AND :endOfDay";

            TypedQuery<Double> query = em.createQuery(jpql, Double.class);
            query.setParameter("startOfDay", startOfDay);
            query.setParameter("endOfDay", endOfDay);

            totalRevenue = query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return totalRevenue;
    }

    public List<Object[]> getTop3PartnersToday() {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            LocalDate today = LocalDate.now();
            LocalDateTime start = today.atStartOfDay();
            LocalDateTime end = today.plusDays(1).atStartOfDay();

            String jpql = "SELECT p.brand, SUM(t.price)"+
                    "FROM Ticket t "+
                    "JOIN t.showtime s "+
                    "JOIN s.auditorium a "+
                    "JOIN a.cinema c "+
                    " JOIN c.partner p "+
                    "WHERE t.status = model.enums.Status.ISSUED AND t.createdAt BETWEEN :start AND :end "+
                    "GROUP BY p.brand "+
                    "ORDER BY SUM(t.price) DESC";
            return em.createQuery(jpql, Object[].class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .setMaxResults(3)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Object[]> getTop5MoviesToday() {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            LocalDate today = LocalDate.now();
            LocalDateTime start = today.atStartOfDay();
            LocalDateTime end = today.plusDays(1).atStartOfDay();

            String jpql = "SELECT m.title, SUM(t.price) "+
                    "FROM Ticket t "+
                    "JOIN t.showtime s "+
                    "JOIN s.movie m "+
                    "GROUP BY m.title "+
                    "ORDER BY SUM(t.price) DESC";
            return em.createQuery(jpql, Object[].class)
                    .setMaxResults(5)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
