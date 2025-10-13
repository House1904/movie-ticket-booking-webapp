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
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
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
        else switch (action) {
                case "list":
                    listCinemas(req, resp);
                    break;
                case "getByPartner":
                    getCinemasByPartner(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/view/partner/CinemaList.jsp");
            }
        rd.forward(req,resp);
    }

    private void getCinemasByPartner(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            long partnerId = Long.parseLong(req.getParameter("partnerId"));
            List<Cinema> cinemas = cinemaService.getCinemasByPartnerId(partnerId);
            req.setAttribute("cinemas", cinemas);
            req.setAttribute("partnerId", partnerId); // Để hiển thị hoặc sử dụng trong JSP
            RequestDispatcher rd = req.getRequestDispatcher("/view/partner/CinemaList.jsp");
            rd.forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "partnerId không hợp lệ");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi lấy danh sách rạp");
        }
    }

    private void listCinemas(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Cinema> cinemas = cinemaService.getAllCinemas();
        req.setAttribute("cinemas", cinemas);
        RequestDispatcher rd = req.getRequestDispatcher("/view/partner/CinemaList.jsp");
        rd.forward(req, resp);
    }
}
