package dao;

import model.Partner;
import util.DBConnection;

import javax.persistence.EntityManager;

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
}
