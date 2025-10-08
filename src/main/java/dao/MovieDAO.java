package dao;

import model.Movie;
import util.DBConnection;
import java.sql.*;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class MovieDAO {

    public List<Movie> getAllMovies() throws SQLException {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        List<Movie> movies = null;

        try {
            movies = entity.createQuery("SELECT m FROM Movie m", Movie.class)
                    .getResultList();
        } finally {
            entity.close();
        }

        return movies;
    }
    public Movie getMovieById(long movieId) {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        Movie movie = null;
        try {
            movie = entity.find(Movie.class, movieId);
        }
        finally {
            entity.close();
        }
        return movie;
    }

}