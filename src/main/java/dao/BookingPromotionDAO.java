package dao;

import model.Booking;
import model.Promotion;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class BookingPromotionDAO {

    /**
     * Ghi quan hệ Booking <-> Promotion vào bảng trung gian bookingpromotion
     */
    public void linkBookingWithPromotion(long bookingId, long promotionId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // ✅ Lấy entity managed trong cùng transaction
            Booking booking = em.find(Booking.class, bookingId);
            Promotion promotion = em.find(Promotion.class, promotionId);

            if (booking != null && promotion != null) {
                // ⚡ Thêm promotion vào list (Hibernate tự insert vào bảng trung gian)
                booking.getPromotions().add(promotion);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
