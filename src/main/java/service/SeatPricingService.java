package service;

import dao.SeatPricingDAO;
import model.seatPricing;

public class SeatPricingService {
    private SeatPricingDAO seatPricingDAO = new SeatPricingDAO();

    public void insertSeatPrice (seatPricing seatPricing) {
        seatPricingDAO.insert(seatPricing);
    }

    public void updateSeatPrice (seatPricing seatPricing) {
        seatPricingDAO.update(seatPricing);
    }
}
