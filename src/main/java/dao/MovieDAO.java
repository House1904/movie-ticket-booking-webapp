package dao;

import model.Movie;
import util.DBConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
    public List<Movie> getMovieIsShowing(){
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        List<Movie> movies = null;
        LocalDateTime now = LocalDateTime.now();
        String query = "SELECT m FROM Movie m WHERE m.releaseDate < :dateNow";
        TypedQuery<Movie> query1 = entity.createQuery(query, Movie.class);

        query1.setParameter("dateNow", now);
        movies = query1.getResultList();
        return movies;
    }
    public List<Movie> getMovieCommingSoon(){
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        List<Movie> movies = null;
        LocalDateTime now = LocalDateTime.now();
        String query = "SELECT m FROM Movie m WHERE m.releaseDate > :dateNow";
        TypedQuery<Movie> query1 = entity.createQuery(query, Movie.class);

        query1.setParameter("dateNow", now);
        movies = query1.getResultList();
        return movies;
    }
}