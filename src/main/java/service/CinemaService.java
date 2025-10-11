package service;
import dao.CinemaDAO;
import model.Cinema;

import java.sql.SQLException;
import java.util.List;

public class CinemaService {
    private CinemaDAO cinemaDAO = new CinemaDAO();

    public Cinema findById(long id) {
        return cinemaDAO.findById(id);
    }

    public void update(Cinema cinema) {
        cinemaDAO.update(cinema);
    }

    public List<Cinema> getAllCinemas() {
        return cinemaDAO.getAllCinemas();
    }

    public List<Cinema> getCinemasByPartnerId(long partnerId) {
        return cinemaDAO.getCinemasByPartnerId(partnerId);
    }
}
