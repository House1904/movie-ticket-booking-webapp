package controller;

import model.*;
import service.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/manageShowtime")
public class ShowtimeController extends HttpServlet {

    private final MovieService movieService = new MovieService();
    private final ShowtimeService showtimeService = new ShowtimeService();
    private final AuditoriumService auditoriumService = new AuditoriumService();
    private final CinemaService cinemaService = new CinemaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        Partner partner = (Partner) req.getSession().getAttribute("partner");
        if (partner == null) {
            resp.sendRedirect(req.getContextPath() + "/common/login.jsp");
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

        List<Cinema> cinemas = cinemaService.getCinemasByPartnerId(partnerId);
        List<Auditorium> auditoriums = auditoriumService.getAuditoriumsByPartner(partnerId);
        List<Showtime> showtimes = showtimeService.getShowtimesByPartner(partnerId);

        req.setAttribute("movies", movies);
        req.setAttribute("cinemas", cinemas);
        req.setAttribute("auditoriums", auditoriums);
        req.setAttribute("showtimes", showtimes);

        // ✅ Xử lý action
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
            long id = Long.parseLong(req.getParameter("id"));
            Showtime showtime = showtimeService.getShowtime(id);
            req.setAttribute("showtime", showtime);
        } else if ("mnShowtime".equals(action)) {
            long id = Long.parseLong(req.getParameter("auditoriumId"));
            req.setAttribute("auditoriumId", id);
        }

        RequestDispatcher rd = req.getRequestDispatcher("/view/partner/manageShowtime.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        Partner partner = (Partner) req.getSession().getAttribute("partner");
        if (partner == null) {
            resp.sendRedirect(req.getContextPath() + "/common/login.jsp");
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

            showtime.setMovie(movieService.getMovie(movieId));
            showtime.setAuditorium(auditoriumService.findById(auditoriumId));
            showtime.setLanguage(language);
            showtime.setStartTime(startTime);

            long duration = showtime.getMovie().getDuration();
            showtime.setEndTime(startTime.plusMinutes(duration));

            // ✅ Kiểm tra trùng suất chiếu
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

            // ✅ Luôn reload danh sách mới nhất sau khi lưu
            long partnerId = partner.getId();
            req.setAttribute("movies", movieService.getMovies());
            req.setAttribute("cinemas", cinemaService.getCinemasByPartnerId(partnerId));
            req.setAttribute("auditoriums", auditoriumService.getAuditoriumsByPartner(partnerId));
            req.setAttribute("showtimes", showtimeService.getShowtimesByPartner(partnerId));

            // ✅ Chuyển hướng về đúng JSP (không redirect, để giữ message)
            RequestDispatcher rd = req.getRequestDispatcher("/view/partner/manageShowtime.jsp");
            rd.forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "❌ Lỗi khi lưu suất chiếu: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
