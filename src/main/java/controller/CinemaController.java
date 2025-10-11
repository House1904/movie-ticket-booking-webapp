package controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.sql.*;
import java.util.*;
import model.Cinema;
import service.CinemaService;

@WebServlet("/cinema")
public class CinemaController extends HttpServlet{
    private CinemaService cinemaService = new CinemaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        List<Cinema> cinemas = null;
        cinemas = cinemaService.getCinemas();
        session.setAttribute("cinemas", cinemas);
        RequestDispatcher rd = req.getRequestDispatcher("/view/customer/showtime.jsp");
        rd.forward(req,resp);
    }
}
