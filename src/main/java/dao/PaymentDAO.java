package dao;

import model.Payment;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class PaymentDAO {
    public boolean insert(Payment payment) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(payment);
            tx.commit();
            return true;
        } catch (Exception ex) {
            System.out.println("Error PaymentDAO: " + ex.getMessage());
            tx.rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public Payment getByBookingId(long bookingId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Payment p JOIN FETCH p.booking b WHERE b.id = :bookingId";
            return em.createQuery(jpql, Payment.class)
                    .setParameter("bookingId", bookingId)
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println("Payment not found for bookingId: " + bookingId);
            return null;
        } finally {
            em.close();
        }
    }

    public void updateStatus(long paymentId, String newStatus) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Payment payment = em.find(Payment.class, paymentId);
            if (payment != null) {
                payment.setStatus(Enum.valueOf(model.enums.PaymentStatus.class, newStatus));
                em.merge(payment);
            }
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            System.out.println("Error updating payment status: " + ex.getMessage());
        } finally {
            em.close();
        }
    }
}
