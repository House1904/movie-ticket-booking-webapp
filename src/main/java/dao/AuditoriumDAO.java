package dao;

import model.Auditorium;
import util.DBConnection;
import javax.persistence.EntityManager;
import java.util.List;

public class AuditoriumDAO {
    public List<Auditorium> getAuditoriumsByPartner(long partnerId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        List<Auditorium> auditoriums = null;
        try {
            auditoriums = em.createQuery(
                            "SELECT a FROM Auditorium a WHERE a.cinema.partner.id = :partnerId",
                            Auditorium.class
                    )
                    .setParameter("partnerId", partnerId)
                    .getResultList();
        } finally {
            em.close();
        }
        return auditoriums;
    }
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