package dao;

import model.Promotion;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class PromotionDAO {

    // 🧾 Lấy tất cả khuyến mãi
    public List<Promotion> findAll() {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        List<Promotion> list = em.createQuery("SELECT p FROM Promotion p ORDER BY p.startAt DESC", Promotion.class)
                .getResultList();
        em.close();
        return list;
    }

    // 🔍 Tìm theo ID
    public Promotion findById(long id) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        Promotion p = em.find(Promotion.class, id);
        em.close();
        return p;
    }

    // ➕ Thêm mới
    public void insert(Promotion promotion) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(promotion);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ✏️ Cập nhật
    public void update(Promotion promotion) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(promotion);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ❌ Xóa
    public void delete(long id) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            Promotion p = em.find(Promotion.class, id);
            if (p != null) {
                tx.begin();
                em.remove(p);
                tx.commit();
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Promotion> findByPartner(long partnerId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        String jpql = "SELECT p FROM Promotion p WHERE p.partner.id = :partnerId ORDER BY p.startAt DESC";
        TypedQuery<Promotion> q = em.createQuery(jpql, Promotion.class);
        q.setParameter("partnerId", partnerId);
        List<Promotion> list = q.getResultList();
        em.close();
        return list;
    }

}
