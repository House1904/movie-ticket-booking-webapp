package service;

import model.BookingSeat;
import model.Seat;
import dao.BookingDAO;
import model.Showtime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private  BookingDAO bookingDAO = new BookingDAO();
    public List<Integer> getSeatID(long auditID, long showtimeID){
        List<BookingSeat> bookingSeats = bookingDAO.getBookingSeat(auditID, showtimeID);
        List<Integer> seatIDs = new ArrayList<>();
        for (BookingSeat bookingSeat : bookingSeats) {
            Seat seat = bookingSeat.getSeat();
            int seatID = seat.getId();
            seatIDs.add(seatID);
        }
        return seatIDs;
    }
    public void insert(BookingSeat bookingSeat) {
        bookingDAO.insertSeat(bookingSeat);
    }

    public void deletedBookingSeat() {
        bookingDAO.deleteBookingSeat();
    }
}
