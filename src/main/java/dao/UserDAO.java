package dao;

import model.Customer;
import model.Partner;
import model.User;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class UserDAO {
    private EntityManager em = DBConnection.getEmFactory().createEntityManager();

    public User getUserById(long userId) {
        return em.find(User.class, userId);
    }

    public User getUserByEmail(String email) {
        try {
            return em.createQuery(
                            "SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Không tìm thấy user
        }
    }

    public void addUser(User user) {
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}