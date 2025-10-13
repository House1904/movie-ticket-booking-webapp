package dao;

import model.Partner;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class PartnerDAO {

    // 🔍 Tìm đối tác theo ID
    public Partner findById(Long id) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        Partner partner = em.find(Partner.class, id);
        em.close();
        return partner;
    }

    // 🔍 Tìm đối tác theo email (để đăng nhập)
    public Partner findByEmail(String email) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        Partner partner = null;
        try {
            TypedQuery<Partner> query = em.createQuery(
                    "SELECT p FROM Partner p WHERE p.email = :email", Partner.class);
            query.setParameter("email", email);
            partner = query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
        return partner;
    }

    // 💾 Cập nhật thông tin đối tác
    public void update(Partner partner) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(partner); // merge dùng để update entity có sẵn
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ➕ Thêm mới đối tác (nếu cần)
    public void insert(Partner partner) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(partner);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ❌ Xóa đối tác
    public void delete(Long id) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            Partner partner = em.find(Partner.class, id);
            if (partner != null) {
                em.getTransaction().begin();
                em.remove(partner);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // 🧾 Lấy danh sách tất cả đối tác (cho admin)

    public List<Partner> selectAll() {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        List<Partner> partners = null;
        partners = em.createQuery("SELECT p FROM Partner p", Partner.class).getResultList();
        return partners;
    }
}
