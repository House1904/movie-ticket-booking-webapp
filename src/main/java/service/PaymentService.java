package service;

import dao.PaymentDAO;
import model.Payment;

public class PaymentService {
    private PaymentDAO paymentDAO = new PaymentDAO();

    public boolean insert(Payment payment) {
        return paymentDAO.insert(payment);
    }

    public Payment getByBookingId(long bookingId) {
        return paymentDAO.getByBookingId(bookingId);
    }

    public void updateStatus(long paymentId, String newStatus) {
        paymentDAO.updateStatus(paymentId, newStatus);
    }
}
