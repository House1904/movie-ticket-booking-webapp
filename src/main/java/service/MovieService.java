package service;
import dao.CinemaDAO;
import dao.MovieDAO;
import model.Movie;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import java.util.List;

public class MovieService {
    private MovieDAO movieDAO = new MovieDAO();
    public List<Movie> getMovies() {
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
    public List<String> getGenres() throws SQLException {
        List<Movie> movies = movieDAO.getAllMovies();
        Set<String> genres = new HashSet<>();
        for (Movie m : movies) {
            if (m.getGenre() != null) {
                for (String g : m.getGenre()) {
                    genres.add(g.trim());
                }
            }
        }
        return new ArrayList<>(genres);
    }
    public List<Movie> getMoviesbyKeyWord(String keyword) {
        return movieDAO.getMovieByKeyword(keyword);
    }
}
