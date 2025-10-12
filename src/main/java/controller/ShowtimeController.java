package controller;

import model.*;
import model.enums.Role;
import service.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/manageShowtime")
public class ShowtimeController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ShowtimeController.class.getName());
    private final MovieService movieService = new MovieService();
    private final ShowtimeService showtimeService = new ShowtimeService();
    private final AuditoriumService auditoriumService = new AuditoriumService();
    // Removed unused cinemaService

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // Kiểm tra đăng nhập của partner
        Partner partner = (Partner) req.getSession().getAttribute("user");
        Account account = (Account) req.getSession().getAttribute("account");

        if (partner == null || account == null || account.getRole() != Role.PARTNER) {
            LOGGER.warning("No valid partner session found. User: " + (partner != null ? partner.getId() : "null") +
                    ", Account: " + (account != null ? account.getUserName() : "null"));
            resp.sendRedirect(req.getContextPath() + "/auth?action=login&redirect=/manageShowtime");
            return;
        }

        long partnerId = partner.getId();
        String action = req.getParameter("action");

        List<Movie> movies;
        try {
            movies = movieService.getMovies();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Lấy thông tin auditorium và cinema dựa trên auditoriumId
        Auditorium auditorium = null;
        Cinema cinema = null;
        if ("mnShowtime".equals(action)) {
            try {
                long auditoriumId = Long.parseLong(req.getParameter("auditoriumId"));
                auditorium = auditoriumService.findById(auditoriumId);
                if (auditorium != null) {
                    cinema = auditorium.getCinema();
                    req.setAttribute("auditoriumId", auditoriumId);
                } else {
                    req.setAttribute("error", "Phòng chiếu không hợp lệ!");
                    resp.sendRedirect(req.getContextPath() + "/manageCinema");
                    return;
                }
            } catch (NumberFormatException e) {
                req.setAttribute("error", "ID phòng chiếu không hợp lệ!");
                resp.sendRedirect(req.getContextPath() + "/manageCinema");
                return;
            }
        }

        // Lấy danh sách showtimes của partner
        List<Showtime> showtimes = null;
        if (auditorium != null) {
            showtimes = showtimeService.getShowtimesByAuditorium(auditorium.getId());
        } else {
            showtimes = showtimeService.getShowtimesByPartner(partnerId);
        }
        req.setAttribute("showtimes", showtimes);

        req.setAttribute("movies", movies);
        req.setAttribute("cinema", cinema);
        req.setAttribute("auditorium", auditorium);
        req.setAttribute("showtimes", showtimes);

        // Xử lý action
        if ("deleteShowtime".equals(action)) {
            try {
                long id = Long.parseLong(req.getParameter("id"));
                showtimeService.deleteShowtime(id);
                req.setAttribute("message", "✅ Xóa suất chiếu thành công!");
                // Load lại danh sách sau khi xóa
                req.setAttribute("showtimes", showtimeService.getShowtimesByPartner(partnerId));
            } catch (Exception e) {
                req.setAttribute("error", "❌ Không thể xóa suất chiếu!");
            }
        } else if ("editShowtime".equals(action)) {
            try {
                long id = Long.parseLong(req.getParameter("id"));
                Showtime showtime = showtimeService.getShowtime(id);
                if (showtime != null) {
                    req.setAttribute("showtime", showtime);
                    req.setAttribute("auditorium", showtime.getAuditorium());
                    req.setAttribute("cinema", showtime.getAuditorium().getCinema());
                } else {
                    req.setAttribute("error", "Suất chiếu không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                req.setAttribute("error", "ID suất chiếu không hợp lệ!");
            }
        }

        // Nếu không có auditorium và không phải edit (tức là truy cập trực tiếp), redirect về manageCinema để chọn
        if (auditorium == null && !"editShowtime".equals(action) && !"deleteShowtime".equals(action)) {
            resp.sendRedirect(req.getContextPath() + "/manageCinema");
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher("/view/partner/manageShowtime.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // Kiểm tra đăng nhập của partner
        Partner partner = (Partner) req.getSession().getAttribute("user");
        Account account = (Account) req.getSession().getAttribute("account");

        if (partner == null || account == null || account.getRole() != Role.PARTNER) {
            LOGGER.warning("No valid partner session found. User: " + (partner != null ? partner.getId() : "null") +
                    ", Account: " + (account != null ? account.getUserName() : "null"));
            resp.sendRedirect(req.getContextPath() + "/auth?action=login&redirect=/manageShowtime");
            return;
        }

        String action = req.getParameter("action");
        if (!"save".equals(action)) {
            resp.sendRedirect(req.getContextPath() + "/manageShowtime");
            return;
        }

        try {
            String showtimeId = req.getParameter("showtimeId");
            Showtime showtime = (showtimeId != null && !showtimeId.isEmpty())
                    ? showtimeService.getShowtime(Long.parseLong(showtimeId))
                    : new Showtime();

            long movieId = Long.parseLong(req.getParameter("movieId"));
            long auditoriumId = Long.parseLong(req.getParameter("auditoriumId"));
            String language = req.getParameter("language");
            LocalDateTime startTime = LocalDateTime.parse(req.getParameter("startTime"));

            Auditorium auditorium = auditoriumService.findById(auditoriumId);
            if (auditorium == null) {
                req.setAttribute("error", "Phòng chiếu không hợp lệ!");
                doGet(req, resp);
                return;
            }

            showtime.setMovie(movieService.getMovie(movieId));
            showtime.setAuditorium(auditorium);
            showtime.setLanguage(language);
            showtime.setStartTime(startTime);

            long duration = showtime.getMovie().getDuration();
            showtime.setEndTime(startTime.plusMinutes(duration));

            // Kiểm tra trùng suất chiếu
            List<Showtime> existingShowtimes = showtimeService.getShowtimesByAuditorium(auditoriumId);
            boolean conflict = false;
            for (Showtime s : existingShowtimes) {
                if (showtimeId != null && !showtimeId.isEmpty() && s.getId() == Long.parseLong(showtimeId))
                    continue;

                boolean overlap = !showtime.getEndTime().isBefore(s.getStartTime())
                        && !showtime.getStartTime().isAfter(s.getEndTime());
                if (overlap) {
                    conflict = true;
                    break;
                }
            }

            if (conflict) {
                req.setAttribute("error", "❌ Phòng chiếu này đã có suất chiếu trong thời gian đó!");
            } else {
                if (showtimeId != null && !showtimeId.isEmpty()) {
                    showtimeService.updateShowtime(showtime);
                    req.setAttribute("message", "Cập nhật suất chiếu thành công!");
                } else {
                    showtimeService.addShowtime(showtime);
                    req.setAttribute("message", "Thêm suất chiếu thành công!");
                }
            }

            // Reload danh sách mới nhất sau khi lưu
            long partnerId = partner.getId();
            req.setAttribute("movies", movieService.getMovies());
            req.setAttribute("cinema", auditorium.getCinema());
            req.setAttribute("auditorium", auditorium);
            req.setAttribute("showtimes", showtimeService.getShowtimesByAuditorium(auditoriumId));

            // Chuyển hướng về đúng JSP (không redirect, để giữ message)
            RequestDispatcher rd = req.getRequestDispatcher("/view/partner/manageShowtime.jsp");
            rd.forward(req, resp);

        } catch (Exception e) {
            LOGGER.severe("Error saving showtime: " + e.getMessage());
            req.setAttribute("error", "❌ Lỗi khi lưu suất chiếu: " + e.getMessage());
            doGet(req, resp);
        }
    }
}