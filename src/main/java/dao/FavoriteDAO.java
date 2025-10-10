package dao;

import model.Favorite;
import model.User;
import util.DBConnection;

import javax.persistence.*;
import java.util.List;

public class FavoriteDAO {

    public void addFavorite(Favorite fav) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(fav);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Favorite> findByUser(User user) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        List<Favorite> list = null;
        try {
            TypedQuery<Favorite> query = em.createQuery(
                    "SELECT DISTINCT f FROM Favorite f " +
                            "JOIN FETCH f.movie m " +
                            "LEFT JOIN FETCH m.genre " +
                            "WHERE f.user = :user", Favorite.class
            );
            query.setParameter("user", user);
            list = query.getResultList();
        } finally {
            em.close();
        }
        return list;
    }

    public boolean exists(User user, int movieId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        boolean exists;
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(f) FROM Favorite f WHERE f.user = :user AND f.movie.id = :movieId", Long.class);
            query.setParameter("user", user);
            query.setParameter("movieId", movieId);
            exists = query.getSingleResult() > 0;
        } finally {
            em.close();
        }
        return exists;
    }

    public void removeFavorite(User user, int movieId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("DELETE FROM Favorite f WHERE f.user = :user AND f.movie.id = :movieId");
            query.setParameter("user", user);
            query.setParameter("movieId", movieId);
            query.executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
