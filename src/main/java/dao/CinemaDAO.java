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

    public List<Cinema> getAllCinemas() {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        List<Cinema> cinemas = null;

        String sql = "SELECT c FROM Cinema c ORDER BY c.partner.brand DESC";
        try {
            cinemas = entity.createQuery("SELECT c FROM Cinema c", Cinema.class)
                    .getResultList();
        } finally {
            entity.close();
        }

        return cinemas;
    }
    public Cinema findCinemaById(long cinemaId) {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        Cinema cinema = entity.find(Cinema.class, cinemaId);
        entity.close();
        return cinema;
    }

}