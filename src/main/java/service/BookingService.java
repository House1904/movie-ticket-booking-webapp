package service;

import model.Booking;
import model.BookingSeat;
import model.Seat;
import dao.BookingDAO;
import model.Showtime;
import model.enums.SeatBookedFormat;

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

    public boolean insertBooking (Booking booking){
        return bookingDAO.insertBooking(booking);
    }

    public void updateBooking(Booking booking) {
        bookingDAO.updateBooking(booking);
    }
    public Booking getBookingById(long id) {
        return bookingDAO.getBookingById(id);
    }

    public BookingSeat findBookingSeatBySeatAndShowtime(int seatId, long showtimeId) {
        return bookingDAO.findBookingSeatBySeatAndShowtime(seatId, showtimeId);
    }

    public void updateBookingSeatStatus(BookingSeat bookingSeat, SeatBookedFormat newStatus) {
        bookingDAO.updateBookingSeatStatus(bookingSeat, newStatus);
    }

}
