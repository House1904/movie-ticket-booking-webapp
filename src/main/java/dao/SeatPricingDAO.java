package dao;

import model.seatPricing;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class SeatPricingDAO {

    public void insert(seatPricing seatpricing) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.persist(seatpricing);
        et.commit();
        em.close();
    }

    public void update(seatPricing seatpricing) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.merge(seatpricing);
        et.commit();
    }
}
