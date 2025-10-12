package controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.*;

import model.*;
import service.*;
import dao.FavoriteDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


@WebServlet("/showtime")
public class ShowtimePageController extends HttpServlet{
    private ShowtimeService showtimeService = new ShowtimeService();
    private PartnerService partnerService = new PartnerService();
    private MovieService movieService = new MovieService();

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProjectLoad");
    private EntityManager em = emf.createEntityManager();
    private FavoriteService favoriteService = new FavoriteService(new FavoriteDAO(em));

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
            doGet(req, resp);
        }
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
            HttpSession session = req.getSession();

            List<Partner> partners = null;
            partners = partnerService.getAllPartners();
            session.setAttribute("partners", partners);

            String movieIdParam = req.getParameter("movieId");
            Movie selectedMovie = null;
            if (movieIdParam != null && !movieIdParam.isEmpty()) {
                long movieId = Long.parseLong(movieIdParam);
                selectedMovie = movieService.getMovie(movieId);
            }

            req.setAttribute("selectedMovie", selectedMovie);

            // Kiểm tra yêu thích
            User user = (User) session.getAttribute("currentUser");
            boolean isFavorite = false;
            if (user != null && selectedMovie != null) {
                isFavorite = favoriteService.isFavorite(user, selectedMovie.getId());
            }
            req.setAttribute("isFavorite", isFavorite);

            String action = req.getParameter("action");
            if ("filter".equals(action)) {
                String idParam = req.getParameter("id");
                if (idParam == null || idParam.isEmpty()) {
                    req.getRequestDispatcher("/view/customer/showtime.jsp").forward(req, resp);
                    return;
                }
                long cinemaId = Long.parseLong(idParam);
                req.setAttribute("selectedCinemaId", cinemaId);
                String selectedDateStr = req.getParameter("selectedDate");
                LocalDate selectedDate = (selectedDateStr == null || selectedDateStr.isEmpty())
                        ? LocalDate.now()
                        : LocalDate.parse(selectedDateStr);
                req.setAttribute("selectedDate", selectedDate);
                List<Showtime> showtimes = null;
                try {
                    showtimes = showtimeService.getShowtimesByC(cinemaId, selectedDate);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // Gom các showtime theo từng movie
                Map<Movie, List<Showtime>> movieShowtimes = new LinkedHashMap<>();
                LocalDateTime now = LocalDateTime.now();
                for (Showtime s : showtimes) {
                    Movie movie = s.getMovie();
                    movieShowtimes.computeIfAbsent(movie, k -> new ArrayList<>()).add(s);
                }

                req.setAttribute("now", now);
                req.setAttribute("movieShowtimes", movieShowtimes);
            }
            req.getRequestDispatcher("/view/customer/showtime.jsp").forward(req, resp);
        }
    }
