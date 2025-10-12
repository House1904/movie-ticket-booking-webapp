package dao;

import model.Cinema;
import model.Movie;
import service.CinemaService;
import util.DBConnection;
import java.sql.*;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CinemaDAO {
    EntityManager entity = DBConnection.getEmFactory().createEntityManager();

    public List<Cinema> getAllCinemas() {
        List<Cinema> cinemas = null;

        cinemas = entity.createQuery("SELECT c FROM Cinema c", Cinema.class)
                .getResultList();

        return cinemas;
    }

    public Cinema getCinemaById(long cinemaId) {
        Cinema cinema = entity.find(Cinema.class, cinemaId);
        return cinema;
    }
}