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
    public boolean seatExists(long auditoriumId, String rowLabel, String seatNumber) {
        return seatDAO.seatExists(auditoriumId, rowLabel, seatNumber);
    }
    public void save(Seat seat) {
        seatDAO.save(seat);
    }

    public void update(Seat seat) {
        seatDAO.update(seat);
    }

    public void delete(int seatId) {
        seatDAO.delete(seatId);
    }
}

