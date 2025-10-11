package service;
import dao.CinemaDAO;
import dao.MovieDAO;
import model.Movie;

import java.sql.SQLException;
import java.util.List;
public class MovieService {
    private MovieDAO movieDAO = new MovieDAO();
    public List<Movie> getMovies() {
        return movieDAO.getAllMovies();
    }
    public Movie getMovie(long movie_id) {
        return movieDAO.getMovieById(movie_id);
    }
}
