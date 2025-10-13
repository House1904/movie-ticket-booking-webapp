package dao;

import model.Promotion;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
    public void delete(Promotion promotion) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // Lấy lại entity từ DB để đảm bảo nó được quản lý
            Promotion p = em.find(Promotion.class, promotion.getId());
            if (p != null) {
                em.remove(p);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
