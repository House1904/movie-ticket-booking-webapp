package controller;

import dao.MovieDAO;
import model.Cinema;
import model.Movie;
import model.Showtime;
import model.User;
import model.Rating;
import service.ShowtimeService;
import service.CinemaService;
import service.MovieService;
import service.FavoriteService;
import service.RatingService;
import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet({"/home", "/selectShowtime"})
public class HomeController extends HttpServlet {
    private MovieService movieService = new MovieService();
    private CinemaService cinemaService = new CinemaService();
    private ShowtimeService showtimeService = new ShowtimeService();
    private MovieDAO movieDAO = new MovieDAO() ;
    private FavoriteService favoriteService;

    @Override
    public void init() throws ServletException {
        EntityManager em = util.DBConnection.getEmFactory().createEntityManager();
        this.favoriteService = new FavoriteService(new dao.FavoriteDAO(em));
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        try {
            if ("/home".equals(servletPath)) {
                List<Movie> nowShowingMovies = movieService.getMoviesbyIsShowing();
                List<Movie> upcomingMovies = movieService.getMoviesbyCommingSoon();
                List<Cinema> cinemas = cinemaService.getCinemas();

                HttpSession session = req.getSession();
                session.setAttribute("nowShowingMovies", nowShowingMovies);
                session.setAttribute("upcomingMovies", upcomingMovies);
                session.setAttribute("cinemas", cinemas);

                RequestDispatcher rd = req.getRequestDispatcher("/view/customer/home.jsp");
                rd.forward(req, resp);
            } else if ("/selectShowtime".equals(servletPath)) {
                String movieIdStr = req.getParameter("movieId");
                String dateStr = req.getParameter("date");
                String cinemaIdStr = req.getParameter("cinemaId"); // Lấy cinemaId từ yêu cầu

                LocalDate selectedDate = dateStr != null ? LocalDate.parse(dateStr) : LocalDate.now();

                if (movieIdStr != null) {
                    long movieId = Long.parseLong(movieIdStr);
                    Movie movie = movieService.getMovie(movieId);

                    if (movie != null) {
                        HttpSession session = req.getSession();
                        session.setAttribute("selectedMovie", movie);
                        List<Cinema> cinemas = cinemaService.getCinemas();

                        // Lưu danh sách suất chiếu cho tất cả rạp (tùy chọn, có thể bỏ nếu chỉ cần suất chiếu cho rạp đã chọn)
                        for (Cinema cinema : cinemas) {
                            List<Showtime> showtimes = showtimeService.getShowtimesByC(cinema.getId(), selectedDate);
                            cinema.setAuditoriums(null); // Tránh lazy loading
                            System.out.println("Cinema " + cinema.getId() + " - Showtimes for date " + selectedDate + ": " + showtimes.size());
                            for (Showtime showtime : showtimes) {
                                System.out.println("Showtime ID: " + showtime.getId() + ", Movie ID: " + showtime.getMovie().getId() + ", Start: " + showtime.getStartTime());
                            }
                            session.setAttribute("showtimes_" + cinema.getId(), showtimes);
                        }

                        // Thiết lập selectedCinemaId nếu có
                        if (cinemaIdStr != null && !cinemaIdStr.isEmpty()) {
                            req.setAttribute("selectedCinemaId", cinemaIdStr); // Đặt vào request scope để JSP sử dụng
                        }

                        session.setAttribute("cinemas", cinemas);
                        session.setAttribute("selectedDate", selectedDate);

                        User user = (User) req.getSession().getAttribute("user");                        boolean isFavorite = false;
                        if (user != null) {
                            isFavorite = favoriteService.isFavorite(user, movieId);
                        }

                        req.setAttribute("isFavorite", isFavorite);

                        EntityManager em = util.DBConnection.getEmFactory().createEntityManager();
                        try {
                            RatingService ratingService = new RatingService(new dao.RatingDAO(em));
                            List<Rating> ratings = ratingService.getRatingsByMovie(movieId);
                            req.setAttribute("ratings", ratings);
                        } finally {
                            em.close();
                        }

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

}