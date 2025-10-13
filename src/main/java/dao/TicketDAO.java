package dao;

import model.BookingSeat;
import model.Ticket;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class TicketDAO {
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
