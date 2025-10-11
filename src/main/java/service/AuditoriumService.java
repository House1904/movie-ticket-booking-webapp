package service;

import dao.AuditoriumDAO;
import model.Auditorium;
import java.util.List;

public class AuditoriumService {
    private AuditoriumDAO auditoriumDAO = new AuditoriumDAO();

    public Auditorium findById(long id) {
        return auditoriumDAO.findById(id);
    }

    public List<Auditorium> getByCinemaId(long cinemaId) {
        return auditoriumDAO.getByCinemaId(cinemaId);
    }

    public boolean nameExists(long cinemaId, String name) {
        return auditoriumDAO.nameExists(cinemaId, name);
    }

    public void save(Auditorium auditorium) {
        auditoriumDAO.save(auditorium);
    }

    public void update(Auditorium auditorium) {
        auditoriumDAO.update(auditorium);
    }

    public void delete(long id) {
        auditoriumDAO.delete(id);
    }

    public List<Auditorium> getAuditoriumsByPartner(long partnerId) {
        return auditoriumDAO.getAuditoriumsByPartner(partnerId);
    }

}