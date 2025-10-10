package dao;

import model.Customer;
import model.User;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class UserDAO {
    private EntityManager em = DBConnection.getEmFactory().createEntityManager();

    public Customer getUserById(int userId) {
        Customer user = (Customer) em.find(User.class, userId);
        return user;
    }

    public Customer getUserByEmail(String email) {
        try {
            return em.createQuery(
                            "SELECT c FROM Customer c WHERE c.email = :email",
                            Customer.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // không tìm thấy user
        }
    }


    public void addUser(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }
}
