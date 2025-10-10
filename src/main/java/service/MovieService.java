package service;

import dao.MovieDAO;
import model.Movie;
import java.sql.SQLException;
import java.util.List;

public class MovieService {
    private MovieDAO movieDAO = new MovieDAO();

    public List<Movie> getMovies() throws SQLException {
        return movieDAO.getAllMovies();
    }

    public List<Movie> getNowShowingMovies() throws SQLException {
        return movieDAO.getNowShowingMovies();
    }

    public List<Movie> getUpcomingMovies() throws SQLException {
        return movieDAO.getUpcomingMovies();
    }

    public Movie getMovie(long movie_id) {
        return movieDAO.getMovieById(movie_id);
    }
}