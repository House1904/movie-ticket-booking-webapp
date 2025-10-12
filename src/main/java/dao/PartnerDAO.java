package dao;

import model.Partner;
import util.DBConnection;

import javax.persistence.EntityManager;
import java.util.List;

public class PartnerDAO {
    public boolean insert(Partner partner) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        boolean result = false;
        em.getTransaction().begin();
        try {
            em.persist(partner);
            em.getTransaction().commit();
            result = true;
        }
        catch (Exception e) {
            em.getTransaction().rollback();
        }
        em.close();
        return result;
    }
    public List<Partner> selectAll() {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        List<Partner> partners = null;
        partners = em.createQuery("SELECT p FROM Partner p", Partner.class).getResultList();
        return partners;
    }
}
