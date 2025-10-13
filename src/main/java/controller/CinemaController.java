package controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.sql.*;
import java.util.*;
import model.Cinema;
import model.Movie;
import service.CinemaService;
import service.MovieService;

@WebServlet("/cinema")
public class CinemaController extends HttpServlet{
    private CinemaService cinemaService = new CinemaService();
    private MovieService movieService = new MovieService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        RequestDispatcher rd = req.getRequestDispatcher("/view/customer/showtime.jsp");
        if ("cinemas".equals(action)) {
            List<Cinema> cinemas = null;
            try {
                cinemas = cinemaService.getCinemas();
                session.setAttribute("cinemas", cinemas);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if ("detail".equals(action)) {
            try {
                long cinemaId = Long.parseLong(req.getParameter("cinemaId"));
                Cinema cinema = cinemaService.findCinemaById(cinemaId);
                List<Movie> movies = movieService.getMoviesByCinemaId(cinemaId);
                session.setAttribute("cinema",  cinema);
                session.setAttribute("movies", movies);
                rd = req.getRequestDispatcher("/view/customer/cinemaDetails.jsp");
            } catch (SQLException e) {
                req.setAttribute("error", e.getMessage());
                throw new RuntimeException(e);
            }
        }

        rd.forward(req,resp);
    }
}
