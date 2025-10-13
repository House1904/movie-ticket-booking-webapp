package service;

import dao.FavoriteDAO;
import model.Favorite;
import model.Movie;
import model.User;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Collections;

public class FavoriteService {

    private static final Logger logger = Logger.getLogger(FavoriteService.class.getName());
    private final FavoriteDAO favoriteDAO;

    public FavoriteService(FavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
    }

    // Toggle favorite: nếu đã thích thì xóa, chưa thích thì thêm
    public boolean toggleFavorite(User user, Movie movie) {
        long movieId = movie.getId();
        try {
            if (favoriteDAO.exists(user, movieId)) {
                favoriteDAO.remove(user, movieId);
                return false; // đã xóa
            } else {
                Favorite favorite = new Favorite();
                favorite.setUser(user);
                favorite.setMovie(movie);
                favoriteDAO.add(favorite);
                return true; // đã thêm
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error toggling favorite for user " + user.getId() + " and movie " + movieId, e);
            return false;
        }
    }

    // Lấy danh sách yêu thích của user
    public List<Favorite> getFavoritesByUser(User user) {
        List<Favorite> favorites = favoriteDAO.findByUser(user);
        // Ép load genre trước khi session đóng
        for (Favorite f : favorites) {
            f.getMovie().getGenre().size(); // chỉ để trigger load
        }
        return favorites;
    }

    // Kiểm tra phim đã thích chưa
    public boolean isFavorite(User user, long movieId) {
        try {
            return favoriteDAO.exists(user, movieId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error checking favorite for user " + user.getId() + " and movie " + movieId, e);
            return false;
        }
    }
}