package dao;

import model.Customer;
import util.DBConnection;

import javax.persistence.EntityManager;
import java.util.List;

public class CustomerDAO {

    public Customer findById(Long id) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        Customer c = em.find(Customer.class, id);
        em.close();
        return c;
    }

    public void update(Customer customer) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(customer);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
