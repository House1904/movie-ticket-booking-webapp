package service;
import dao.CinemaDAO;
import dao.MovieDAO;
import model.Movie;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
public class MovieService {
    private MovieDAO movieDAO = new MovieDAO();
    public List<Movie> getMovies() throws SQLException {
        return movieDAO.getAllMovies();
    }
    public Movie getMovie(long movie_id) {
        return movieDAO.getMovieById(movie_id);
    }
    public List<Movie> getMoviesbyIsShowing() {
        return movieDAO.getMovieIsShowing();
    }
    public List<Movie> getMoviesbyCommingSoon() {
        return movieDAO.getMovieCommingSoon();
    }
}
