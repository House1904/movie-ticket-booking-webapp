package dao;

import model.Movie;
import model.Rating;
import model.Customer;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class RatingDAO {
    private EntityManager em;

    public RatingDAO(EntityManager em) {
        this.em = em;
    }

    public List<Rating> getRatingsByMovie(long movieId) {
        String jpql = "SELECT r FROM Rating r JOIN FETCH r.customer WHERE r.movie.id = :movieId ORDER BY r.created_at DESC";
        TypedQuery<Rating> query = em.createQuery(jpql, Rating.class);
        query.setParameter("movieId", movieId);
        return query.getResultList();
    }

    public void addRating(Rating rating) {
        try {
            em.getTransaction().begin();
            em.persist(rating);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    public Rating findByCustomerAndMovie(Customer customer, Movie movie) {
        String jpql = "SELECT r FROM Rating r WHERE r.customer = :customer AND r.movie = :movie";
        TypedQuery<Rating> query = em.createQuery(jpql, Rating.class);
        query.setParameter("customer", customer);
        query.setParameter("movie", movie);

        List<Rating> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}
