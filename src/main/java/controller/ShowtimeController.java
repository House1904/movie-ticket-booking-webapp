package controller;

import model.*;
import service.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
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

        String action = req.getParameter("action");

        List<Movie> movies = movieService.getMovies();
        List<Cinema> cinemas = cinemaService.getCinemas();
        List<Auditorium> auditoriums = auditoriumService.getAuditoriums();
        List<Showtime> showtimes = showtimeService.getShowtimes();

        req.setAttribute("movies", movies);
        req.setAttribute("cinemas", cinemas);
        req.setAttribute("auditoriums", auditoriums);
        req.setAttribute("showtimes", showtimes);

        if ("deleteShowtime".equals(action)) {
            long id = Long.parseLong(req.getParameter("id"));
            showtimeService.deleteShowtime(id);
            resp.sendRedirect(req.getContextPath() + "/manageShowtime");
            return;
        } else if ("editShowtime".equals(action)) {
            long id = Long.parseLong(req.getParameter("id"));
            Showtime showtime = showtimeService.getShowtime(id);
            req.setAttribute("showtime", showtime);
        }

        RequestDispatcher rd = req.getRequestDispatcher("/view/partner/manageShowtime.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String action = req.getParameter("action");
        if (!"save".equals(action)) {
            resp.sendRedirect(req.getContextPath() + "/manageShowtime");
            return;
        }

        Showtime showtime;
        String showtimeId = req.getParameter("showtimeId");
        if (showtimeId != null && !showtimeId.isEmpty()) {
            showtime = showtimeService.getShowtime(Long.parseLong(showtimeId));
        } else {
            showtime = new Showtime();
        }

        showtime.setMovie(movieService.getMovie(Long.parseLong(req.getParameter("movieId"))));
        showtime.setAuditorium(auditoriumService.getAuditorium(Long.parseLong(req.getParameter("auditoriumId"))));

        LocalDateTime startTime = LocalDateTime.parse(req.getParameter("startTime"));
        showtime.setStartTime(startTime);
        long duration = showtime.getMovie().getDuration();
        showtime.setEndTime(startTime.plusMinutes(duration));
        showtime.setLanguage(req.getParameter("language"));

        // ✅ Kiểm tra trùng suất chiếu
        List<Showtime> existingShowtimes = showtimeService.getShowtimesByAuditorium(showtime.getAuditorium().getId());
        boolean conflict = false;
        for (Showtime s : existingShowtimes) {
            if (showtimeId != null && !showtimeId.isEmpty() && s.getId() == Long.parseLong(showtimeId))
                continue; // bỏ qua chính nó khi sửa

            // Nếu giờ chiếu bị trùng trong cùng phòng chiếu
            boolean overlap = !showtime.getEndTime().isBefore(s.getStartTime()) &&
                    !showtime.getStartTime().isAfter(s.getEndTime());
            if (overlap) {
                conflict = true;
                break;
            }
        }

        if (conflict) {
            req.setAttribute("error", "❌ Phòng chiếu này đã có suất chiếu trong thời gian đó!");
            doGet(req, resp); // quay lại form kèm lỗi
            return;
        }

        if (showtimeId != null && !showtimeId.isEmpty()) {
            try {
                showtimeService.updateShowtime(showtime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                showtimeService.addShowtime(showtime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        resp.sendRedirect(req.getContextPath() + "/manageShowtime");
    }
}
