package dao;

import model.Favorite;
import model.Movie;
import model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class FavoriteDAO {

    private EntityManager em;

    public FavoriteDAO(EntityManager em) {
        this.em = em;
    }

    public void add(Favorite favorite) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(favorite);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void remove(User user, long movieId) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String jpql = "DELETE FROM Favorite f WHERE f.user = :user AND f.movie.id = :movieId";
            em.createQuery(jpql)
                    .setParameter("user", user)
                    .setParameter("movieId", movieId)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public boolean exists(User user, long movieId) {
        String jpql = "SELECT COUNT(f) FROM Favorite f WHERE f.user = :user AND f.movie.id = :movieId";
        Long count = em.createQuery(jpql, Long.class)
                .setParameter("user", user)
                .setParameter("movieId", movieId)
                .getSingleResult();
        return count > 0;
    }

    public List<Favorite> findByUser(User user) {
        String jpql = "SELECT f FROM Favorite f " +
                "JOIN FETCH f.movie m " +
                "LEFT JOIN FETCH m.genre " + // <- thêm fetch genre
                "WHERE f.user = :user";
        TypedQuery<Favorite> query = em.createQuery(jpql, Favorite.class);
        query.setParameter("user", user);
        return query.getResultList();
    }
}