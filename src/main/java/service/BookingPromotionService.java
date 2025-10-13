package service;

import dao.BookingPromotionDAO;

public class BookingPromotionService {
    private final BookingPromotionDAO dao = new BookingPromotionDAO();

    /**
     * Thêm 1 khuyến mãi vào 1 booking cụ thể
     */
    public void addPromotionToBooking(long bookingId, long promotionId) {
        dao.linkBookingWithPromotion(bookingId, promotionId);
    }
}
