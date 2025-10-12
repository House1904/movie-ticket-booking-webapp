package dao;

import model.Cinema;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class CinemaDAO {

    public List<Cinema> getAllCinemas() {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        List<Cinema> cinemas = null;

        try {
            cinemas = entity.createQuery("SELECT c FROM Cinema c", Cinema.class)
                    .getResultList();
        } finally {
            entity.close();
        }
        return cinemas;
    }

    public Cinema getCinemaById(long id) {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        Cinema cinema = null;
        try {
            cinema = entity.find(Cinema.class, id);
        } finally {
            entity.close();
        }
        return cinema;
    }

    public List<Cinema> getCinemasByPartner(long partnerId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        List<Cinema> cinemas = null;
        try {
            cinemas = em.createQuery(
                            "SELECT c FROM Cinema c WHERE c.partner.id = :partnerId",
                            Cinema.class
                    )
                    .setParameter("partnerId", partnerId)
                    .getResultList();
        } finally {
            em.close();
        }
        return cinemas;
    }

    public void addCinema(Cinema c) {
        if (c.getPartner() == null || c.getPartner().getId() <= 0) {
            throw new IllegalArgumentException("Invalid partner for cinema");
        }
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(c);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to add cinema: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void updateCinema(Cinema c) {
        if (c.getPartner() == null || c.getPartner().getId() <= 0) {
            throw new IllegalArgumentException("Invalid partner for cinema");
        }
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(c);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to update cinema: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void deleteCinema(long id) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Cinema cinema = em.find(Cinema.class, id);
            if (cinema != null) {
                em.remove(cinema);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to delete cinema: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}