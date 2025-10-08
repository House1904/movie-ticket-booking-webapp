package controller;

import model.*;
import model.enums.SeatBookedFormat;
import service.BookingService;
import service.MovieService;
import service.SeatService;
import service.ShowtimeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import model.Auditorium;

@WebServlet("/booking")
public class BookingController extends HttpServlet {
    private BookingService bookingService = new BookingService();
    private ShowtimeService showtimeService = new ShowtimeService();
    private MovieService movieService = new MovieService();
    private SeatService seatService = new SeatService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "";
        String action = request.getParameter("action");
        System.out.println(action);
        if("showtimeSl".equals(action)){
            HttpSession session = request.getSession();
            long showtimeID = Long.parseLong(request.getParameter("showtimeID"));

            Showtime st = showtimeService.getShowtime(showtimeID);
            Auditorium auditorium = st.getAuditorium();
            long auditID = auditorium.getId();
            Movie movie = st.getMovie();

            LocalDateTime dateTime = st.getStartTime();
            String day = dateTime.toLocalDate().toString();
            String time = dateTime.toLocalTime().toString();
            String title = movie.getTitle();
            String nameAuditorium = auditorium.getName();
            List<Seat> seatList = seatService.getSeatsByAu(auditID);
            List<Integer> bookedSeatIds = bookingService.getSeatID(auditID, showtimeID);

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

            System.out.println(sid);
            System.out.println(aid);
            // In ra console để kiểm tra (debug)
            for (String id : selectedSeatIds) {
                System.out.println("Ghế được chọn: " + id);
                Seat seat = seatService.getSeats(Integer.parseInt(id));
                BookingSeat bookingSeat = new BookingSeat(au, seat, sht, SeatBookedFormat.HOLD, LocalDateTime.now());
                bookingService.insert(bookingSeat);
            }

            // Sau này mày có thể chuyển list ghế này qua trang thanh toán
            request.setAttribute("selectedSeats", selectedSeatIds);
            url = "/view/customer/payment.jsp";

        }
        request.getRequestDispatcher(url).forward(request, response);
    }
}
