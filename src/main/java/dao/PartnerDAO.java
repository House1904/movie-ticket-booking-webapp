package dao;

import model.Partner;
import util.DBConnection;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class PartnerDAO {

    public void addPartner(Partner partner) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(partner);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void updatePartner(Partner partner) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(partner);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void deletePartner(long partnerId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Partner partner = em.find(Partner.class, partnerId);
            if (partner != null) {
                em.remove(partner);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Partner findPartnerById(long partnerId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            return em.find(Partner.class, partnerId);
        } finally {
            em.close();
        }
    }

    public List<Partner> getAllPartners() {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            TypedQuery<Partner> query = em.createQuery("SELECT p FROM Partner p", Partner.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}