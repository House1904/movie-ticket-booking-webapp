package service;
import dao.CinemaDAO;
import model.Cinema;

import java.sql.SQLException;
import java.util.List;
public class CinemaService {
    private CinemaDAO cinemaDAO = new CinemaDAO();
    public List<Cinema> getCinemas() throws SQLException{
        return cinemaDAO.getAllCinemas();
    }
    public Cinema findCinemaById(long cinemaId) {
        return cinemaDAO.findCinemaById(cinemaId);
    }
}
