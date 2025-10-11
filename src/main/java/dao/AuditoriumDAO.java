package dao;

import model.Auditorium;
import util.DBConnection;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class AuditoriumDAO {

    public Auditorium findById(long id) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            return em.find(Auditorium.class, id);
        } finally {
            em.close();
        }
    }

    public List<Auditorium> getByCinemaId(long cinemaId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT a FROM Auditorium a WHERE a.cinema.id = :cinemaId";
            TypedQuery<Auditorium> query = em.createQuery(jpql, Auditorium.class);
            query.setParameter("cinemaId", cinemaId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public boolean nameExists(long cinemaId, String name) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(a) FROM Auditorium a WHERE a.cinema.id = :cinemaId AND a.name = :name";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("cinemaId", cinemaId);
            query.setParameter("name", name);
            Long count = query.getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public void save(Auditorium auditorium) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(auditorium);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void update(Auditorium auditorium) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(auditorium);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void delete(long id) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Auditorium auditorium = em.find(Auditorium.class, id);
            if (auditorium != null) {
                em.remove(auditorium);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Auditorium> getAuditoriumsByPartner(long partnerId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        List<Auditorium> auditoriums = null;
        try {
            auditoriums = em.createQuery(
                            "SELECT a FROM Auditorium a WHERE a.cinema.partner.id = :partnerId",
                            Auditorium.class
                    )
                    .setParameter("partnerId", partnerId)
                    .getResultList();
        } finally {
            em.close();
        }
        return auditoriums;
    }
    public List<Auditorium> getAllAuditoriums() {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        List<Auditorium> auditoriums = null;

        try {
            auditoriums = entity.createQuery("SELECT a FROM Auditorium a", Auditorium.class)
                    .getResultList();
        } finally {
            entity.close();
        }

        return auditoriums;
    }

    public Auditorium getAuditoriumById(long id) {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        Auditorium auditorium = null;
        try {
            auditorium = entity.find(Auditorium.class, id);
        } finally {
            entity.close();
        }
        return auditorium;
    }
}