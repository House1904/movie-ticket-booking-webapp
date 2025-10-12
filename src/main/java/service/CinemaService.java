package service;
import dao.CinemaDAO;
import model.Cinema;

import java.sql.SQLException;
import java.util.List;
public class CinemaService {
    private CinemaDAO cinemaDAO = new CinemaDAO();

    public Cinema findById(long id) {
        return cinemaDAO.getCinemaById(id);
    }

    public List<Cinema> getAllCinemas() {
        return cinemaDAO.getAllCinemas();
    }

    public List<Cinema> getCinemasByPartnerId(long partnerId) {
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
    public boolean isCinemaNameExists(String name, long partnerId) {
        List<Cinema> cinemas = cinemaDAO.getCinemasByPartner(partnerId);
        for (Cinema c : cinemas) {
            if (c.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

}
