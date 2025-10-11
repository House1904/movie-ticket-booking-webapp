package dao;

import model.Auditorium;
import util.DBConnection;
import javax.persistence.EntityManager;
import java.util.List;

public class AuditoriumDAO {
    public List<Auditorium> getAllAuditoriums() {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        List<Auditorium> auditoriums = null;

        try {
            auditoriums = entity.createQuery("SELECT a FROM Auditorium a", Auditorium.class)
                    .getResultList();
        } finally {
            entity.close();
        }

        return auditoriums;
    }

    public Auditorium getAuditoriumById(long id) {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        Auditorium auditorium = null;
        try {
            auditorium = entity.find(Auditorium.class, id);
        } finally {
            entity.close();
        }
        return auditorium;
    }
}