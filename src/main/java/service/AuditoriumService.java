package service;

import dao.AuditoriumDAO;
import model.Auditorium;
import java.util.List;

public class AuditoriumService {
    private AuditoriumDAO auditoriumDAO = new AuditoriumDAO();

    public List<Auditorium> getAuditoriums() {
        return auditoriumDAO.getAllAuditoriums();
    }

    public Auditorium getAuditorium(long id) {
        return auditoriumDAO.getAuditoriumById(id);
    }
}