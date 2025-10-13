package controller;

import model.*;
import model.enums.SeatBookedFormat;
import model.enums.Status;
import service.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/booking")
public class BookingController extends HttpServlet {
    private BookingService bookingService = new BookingService();
    private ShowtimeService showtimeService = new ShowtimeService();
    private SeatService seatService = new SeatService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        bookingService.deletedBookingSeat();

        String url = "";
        String action = request.getParameter("action");
        if ("showtimeSl".equals(action)) {
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
            for (Seat seat : seatList) {
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

        else if ("goPay".equals(action)) {
            HttpSession session = request.getSession();
            String[] selectedSeatIds = request.getParameterValues("selectedSeats");

            if (selectedSeatIds == null || selectedSeatIds.length == 0) {
                request.setAttribute("errorMessage", "Vui lòng chọn ít nhất một ghế!");
                request.getRequestDispatcher("/view/customer/booking.jsp").forward(request, response);
                return;
            }

            Showtime sht = (Showtime) session.getAttribute("st");
            Auditorium au = (Auditorium) session.getAttribute("auditorium");

            // Lấy user đăng nhập từ session (được AuthController lưu với key "user")
            Customer customer = null;
            Object userObj = session.getAttribute("user");
            if (userObj instanceof Customer) {
                customer = (Customer) userObj;
            }

            // Kiểm tra user đã đăng nhập chưa
            if (customer == null) {
                String currentURL = request.getRequestURI() +
                        (request.getQueryString() != null ? "?" + request.getQueryString() : "");
                session.setAttribute("redirectAfterLogin", currentURL);
                request.setAttribute("errorMessage", "Bạn cần đăng nhập trước khi đặt vé!");
                request.getRequestDispatcher("/common/login.jsp").forward(request, response);
                return;
            }

            // Tạo Booking và liên kết Customer
            Booking booking = new Booking(Status.PENDING, customer);
            boolean res = bookingService.insertBooking(booking);
            if (res) {
                System.out.println("Booking inserted successfully for customer ID = " + customer.getId());
            } else {
                System.out.println("Booking insert failed");
            }

            // Giữ danh sách ghế và giá
            Map<Seat, BigDecimal> seatPrices = new LinkedHashMap<>();
            for (String id : selectedSeatIds) {
                Seat seat = seatService.getSeats(Integer.parseInt(id));
                String sID = "seat_" + id;
                String priceS = request.getParameter(sID);
                BigDecimal price = new BigDecimal(priceS);
                seatPrices.put(seat, price);

                // Giữ ghế tạm thời (HOLD)
                BookingSeat bookingSeat = new BookingSeat(au, seat, sht, SeatBookedFormat.HOLD, LocalDateTime.now());
                bookingService.insert(bookingSeat);
            }

            // Lưu session phục vụ bước thanh toán
            session.setAttribute("booking", booking);
            session.setAttribute("seatPrices", seatPrices);
            session.setAttribute("showtime", sht);

            url = "/view/customer/payment.jsp";
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/view/customer/booking.jsp").forward(request, response);
    }
}
