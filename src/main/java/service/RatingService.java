package service;

import dao.RatingDAO;
import model.Movie;
import model.Rating;
import model.Customer;

import java.util.List;

public class RatingService {
    private RatingDAO ratingDAO;

    public RatingService(RatingDAO ratingDAO) {
        this.ratingDAO = ratingDAO;
    }

    public List<Rating> getRatingsByMovie(long movieId) {
        return ratingDAO.getRatingsByMovie(movieId);
    }

    public void addRating(Rating rating) {
        ratingDAO.addRating(rating);
    }

    public boolean hasRated(Customer customer, Movie movie) {
        return ratingDAO.findByCustomerAndMovie(customer, movie) != null;
    }

    public Rating findByCustomerAndMovie(Customer customer, Movie movie) {
        return ratingDAO.findByCustomerAndMovie(customer, movie);
    }
}
