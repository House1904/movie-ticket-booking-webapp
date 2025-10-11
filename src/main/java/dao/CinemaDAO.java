package dao;

import model.Cinema;
import model.Partner;
import util.DBConnection;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class CinemaDAO {

    public Cinema findById(long id) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            return em.find(Cinema.class, id);
        } finally {
            em.close();
        }
    }

    public void update(Cinema cinema) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(cinema);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Cinema> getAllCinemas() {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Cinema c";
            TypedQuery<Cinema> query = em.createQuery(jpql, Cinema.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Cinema> getCinemasByPartnerId(long partnerId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Cinema c WHERE c.partner.id = :partnerId";
            TypedQuery<Cinema> query = em.createQuery(jpql, Cinema.class);
            query.setParameter("partnerId", partnerId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    public Cinema findCinemaById(long cinemaId) {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        Cinema cinema = entity.find(Cinema.class, cinemaId);
        entity.close();
        return cinema;
    }
}