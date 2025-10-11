package controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.sql.*;

import model.Showtime;
import model.Movie;
import model.Cinema;
import service.CinemaService;
import service.MovieService;
import service.ShowtimeService;

@WebServlet("/showtime")
public class ShowtimePageController extends HttpServlet{
    private CinemaService cinemaService = new CinemaService();
    private ShowtimeService showtimeService = new ShowtimeService();
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
            doGet(req, resp);
        }
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
            HttpSession session = req.getSession();
            List<Cinema> cinemas = null;
            cinemas = cinemaService.getAllCinemas();
            session.setAttribute("cinemas", cinemas);

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
                for (Showtime s : showtimes) {
                    Movie movie = s.getMovie();
                    movieShowtimes.computeIfAbsent(movie, k -> new ArrayList<>()).add(s);
                }

                req.setAttribute("movieShowtimes", movieShowtimes);
            }
            req.getRequestDispatcher("/view/customer/showtime.jsp").forward(req, resp);
        }
    }
