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
    public List<Cinema> getCinemasByPartner(long partnerId) {
        return cinemaDAO.getCinemasByPartner(partnerId);
    }
    public void addCinema(Cinema c) {
        cinemaDAO.addCinema(c);
    }

    public void updateCinema(Cinema c) {
        cinemaDAO.updateCinema(c);
    }

    public void deleteCinema(long id) {
        cinemaDAO.deleteCinema(id);
    }
}
