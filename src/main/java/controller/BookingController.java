package controller;

import model.*;
import model.enums.SeatBookedFormat;
import service.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Auditorium;

@WebServlet("/booking")
public class BookingController extends HttpServlet {
    private BookingService bookingService = new BookingService();
    private ShowtimeService showtimeService = new ShowtimeService();
    private MovieService movieService = new MovieService();
    private SeatService seatService = new SeatService();
    private PartnerService partnerService = new PartnerService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "";
        String action = request.getParameter("action");
        bookingService.deletedBookingSeat();

        if("showtimeSl".equals(action)){
            HttpSession session = request.getSession();
            long showtimeID = Long.parseLong(request.getParameter("showtimeID"));

            Showtime st = showtimeService.getShowtime(showtimeID);
            Auditorium auditorium = st.getAuditorium();
            long auditID = auditorium.getId();

            LocalDateTime dateTime = st.getStartTime();
            String day = dateTime.toLocalDate().toString();
            String time = dateTime.toLocalTime().toString();
            String title = st.getMovie().getTitle();
            String nameAuditorium = auditorium.getName();

            List<Seat> seatList = seatService.getSeatsByAu(auditID);
            List<Integer> bookedSeatIds = bookingService.getSeatID(auditID, showtimeID);

            List<Double> prices = new ArrayList<>();
            for(Seat seat : seatList){
                prices.add(seatService.getPrice(seat));
            }
            session.setAttribute("prices", prices);
            session.setAttribute("seatList", seatList);
            session.setAttribute("bookedSeatIds", bookedSeatIds);
            session.setAttribute("nameAuditorium", nameAuditorium);
            session.setAttribute("title", title);
            session.setAttribute("day", day);
            session.setAttribute("time", time);
            session.setAttribute("st", st);
            session.setAttribute("auditorium", auditorium);
            url = "/view/customer/booking.jsp";

        }
        else if("goPay".equals(action)){
            HttpSession session = request.getSession();
            String[] selectedSeatIds = request.getParameterValues("selectedSeats");
            if (selectedSeatIds == null || selectedSeatIds.length == 0) {
                request.setAttribute("errorMessage", "Vui lòng chọn ít nhất một ghế!");
                return;
            }

            Showtime sht = (Showtime) session.getAttribute("st");
            Auditorium au = (Auditorium) session.getAttribute("auditorium");

            long sid = sht.getId();
            long aid = au.getId();

            // In ra console để kiểm tra (debug)
            for (String id : selectedSeatIds) {
                Seat seat = seatService.getSeats(Integer.parseInt(id));
                BookingSeat bookingSeat = new BookingSeat(au, seat, sht, SeatBookedFormat.HOLD, LocalDateTime.now());
                bookingService.insert(bookingSeat);
            }

            session.setAttribute("selectedSeats", selectedSeatIds);
            session.setAttribute("showtime", sht);


            url = "/view/customer/payment.jsp";

        }
        request.getRequestDispatcher(url).forward(request, response);
    }
}
