package service;
import dao.CinemaDAO;
import model.Cinema;

import java.sql.SQLException;
import java.util.List;
public class CinemaService {
    private CinemaDAO cinemaDAO = new CinemaDAO();
    public List<Cinema> getCinemas() {
        return cinemaDAO.getAllCinemas();
    }
    public Cinema getCinema(long id) {
        return cinemaDAO.getCinemaById(id);
    }
}
