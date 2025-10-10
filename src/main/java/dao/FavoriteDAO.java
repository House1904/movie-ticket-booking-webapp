package dao;

import model.Favorite;
import model.Movie;
import model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;


public class FavoriteDAO {

    private EntityManager em;

    public FavoriteDAO(EntityManager em) {
        this.em = em;
    }

    // Kiểm tra xem user đã thêm phim này vào yêu thích chưa
    public boolean exists(User user, long movieId) {
        String jpql = "SELECT COUNT(f) FROM Favorite f WHERE f.user = :user AND f.movie.id = :movieId";
        Long count = em.createQuery(jpql, Long.class)
                .setParameter("user", user)
                .setParameter("movieId", movieId)
                .getSingleResult();
        return count > 0;
    }

    // Thêm phim vào danh sách yêu thích
    public void addFavorite(Favorite favorite) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(favorite);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    // Xóa khỏi danh sách yêu thích
    public void removeFavorite(User user, int movieId) {
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
            e.printStackTrace();
        }
    }

    // Lấy danh sách phim yêu thích theo user
    public List<Favorite> findByUser(User user) {
        String jpql = "SELECT f FROM Favorite f WHERE f.user = :user";
        TypedQuery<Favorite> query = em.createQuery(jpql, Favorite.class);
        query.setParameter("user", user);
        return query.getResultList();
    }

    public boolean toggleFavorite(User user, Movie movie) {
        long movieId = movie.getId();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (exists(user, movieId)) {
                // Xóa khỏi yêu thích
                String jpql = "DELETE FROM Favorite f WHERE f.user = :user AND f.movie.id = :movieId";
                em.createQuery(jpql)
                        .setParameter("user", user)
                        .setParameter("movieId", movieId)
                        .executeUpdate();
                tx.commit();
                return false; // đã xóa
            } else {
                // Thêm mới
                Favorite favorite = new Favorite();
                favorite.setUser(user);
                favorite.setMovie(movie);
                em.persist(favorite);
                tx.commit();
                return true; // đã thêm
            }
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return false;
        }
    }

}
