package controller;

import model.Cinema;
import model.Movie;
import model.Showtime;
import service.BannerService;
import model.Banner;
import service.CinemaService;
import service.MovieService;
import service.ShowtimeService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet({"/home", "/selectShowtime"})
public class HomeController extends HttpServlet {
    private MovieService movieService = new MovieService();
    private CinemaService cinemaService = new CinemaService();
    private ShowtimeService showtimeService = new ShowtimeService();
    private BannerService bannerService = new BannerService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        try {
            if ("/home".equals(servletPath)) {
                // Lấy danh sách phim và rạp
                List<Movie> nowShowingMovies = movieService.getMoviesbyIsShowing();
                List<Movie> upcomingMovies = movieService.getMoviesbyCommingSoon();
                List<Cinema> cinemas = cinemaService.getAllCinemas();
                // Lấy danh sách banner và sắp xếp theo created_at giảm dần
                List<Banner> banners = bannerService.getAllBanners()
                        .stream()
                        .sorted(
                                Comparator.comparing(
                                        Banner::getCreated_at,
                                        Comparator.nullsLast(Comparator.reverseOrder())
                                )
                        )
                        .collect(Collectors.toList());


                HttpSession session = req.getSession();
                session.setAttribute("nowShowingMovies", nowShowingMovies);
                session.setAttribute("upcomingMovies", upcomingMovies);
                session.setAttribute("cinemas", cinemas);
                session.setAttribute("banners", banners);
                session.setAttribute("now", LocalDateTime.now());

                RequestDispatcher rd = req.getRequestDispatcher("/view/customer/home.jsp");
                rd.forward(req, resp);
            } else if ("/selectShowtime".equals(servletPath)) {
                String movieIdStr = req.getParameter("movieId");
                String dateStr = req.getParameter("date");
                String cinemaIdStr = req.getParameter("cinemaId");

                LocalDate selectedDate = dateStr != null ? LocalDate.parse(dateStr) : LocalDate.now();

                if (movieIdStr != null) {
                    long movieId = Long.parseLong(movieIdStr);
                    Movie movie = movieService.getMovie(movieId);
                    if (movie != null) {
                        HttpSession session = req.getSession();
                        session.setAttribute("selectedMovie", movie);
                        List<Cinema> cinemas = cinemaService.getAllCinemas();

                        for (Cinema cinema : cinemas) {
                            List<Showtime> showtimes = showtimeService.getShowtimesByC(cinema.getId(), selectedDate);
                            cinema.setAuditoriums(null); // Tránh lazy loading
                            System.out.println("Cinema " + cinema.getId() + " - Showtimes for date " + selectedDate + ": " + showtimes.size());
                            for (Showtime showtime : showtimes) {
                                System.out.println("Showtime ID: " + showtime.getId() + ", Movie ID: " + showtime.getMovie().getId() + ", Start: " + showtime.getStartTime());
                            }
                            session.setAttribute("showtimes_" + cinema.getId(), showtimes);
                        }

                        if (cinemaIdStr != null && !cinemaIdStr.isEmpty()) {
                            req.setAttribute("selectedCinemaId", cinemaIdStr);
                        }

                        session.setAttribute("cinemas", cinemas);
                        session.setAttribute("selectedDate", selectedDate);
                        RequestDispatcher rd = req.getRequestDispatcher("/view/customer/selectShowtime.jsp");
                        rd.forward(req, resp);
                    } else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Phim không tồn tại");
                    }
                } else {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số movieId");
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Lỗi cơ sở dữ liệu", e);
        }
    }
}