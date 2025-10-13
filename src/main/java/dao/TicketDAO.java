package dao;

import model.BookingSeat;
import model.Ticket;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class TicketDAO {
    public List<Ticket> getTicketsByCustomerId(long customerId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT t FROM Ticket t JOIN FETCH t.showtime s JOIN FETCH s.movie m JOIN FETCH t.seat seat " +
                    "WHERE t.booking.customer.id = :custId ORDER BY t.createdAt DESC";
            TypedQuery<Ticket> q = em.createQuery(jpql, Ticket.class);
            q.setParameter("custId", customerId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Ticket findById(long id) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            return em.find(Ticket.class, id);
        } finally {
            em.close();
        }
    }

    public boolean update(Ticket ticket) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(ticket);
            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tx.isActive()) tx.rollback();
            return false;
        } finally {
            em.close();
        }
    }
    public boolean insert(Ticket ticket) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(ticket);
            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tx.isActive()) tx.rollback();
            return false;
        } finally {
            em.close();
        }
    }
    public List<Ticket> getTicketByCustomer(long customerId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        String jpql = "SELECT t FROM Booking b " +
                "JOIN b.tickets t " +
                "JOIN t.showtime st " +
                "WHERE b.customer.id = :customerId " +
                "ORDER BY st.startTime DESC";
        TypedQuery<Ticket> q = em.createQuery(jpql, Ticket.class);
        q.setParameter("customerId", customerId);
        List<Ticket> result = q.getResultList();
        em.close();
        return result;
    }

    public List<Ticket> getTicketByCustomerAndMovie(long customerId, long movieId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        String jpql = "SELECT t FROM Booking b " +
                "JOIN b.tickets t " +
                "JOIN t.showtime st " +
                "WHERE b.customer.id = :customerId " +
                "AND st.movie.id = :movieId " +
                "ORDER BY st.startTime DESC";
        TypedQuery<Ticket> q = em.createQuery(jpql, Ticket.class);
        q.setParameter("customerId", customerId);
        q.setParameter("movieId", movieId);
        List<Ticket> result = q.getResultList();
        em.close();
        return result;
    }
}
