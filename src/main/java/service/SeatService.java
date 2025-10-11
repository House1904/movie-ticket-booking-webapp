package service;

import dao.SeatDAO;
import model.Seat;

import java.util.List;

public class SeatService {
    private SeatDAO seatDAO = new SeatDAO();
    public Seat getSeats(int seatID) {
        return seatDAO.getSeatbyID(seatID);
    }
    public List<Seat> getSeatsByAu(long auditID) {
        return seatDAO.getSeatByShowtime(auditID);
    }
    public double getPrice(Seat seat) {
        return seatDAO.getSeatPrice(seat);
    }
}
