package controller;

import dao.MovieDAO;
import model.Movie;
import util.DBConnection;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.IOException;

@WebServlet("/movieDetail")
public class MovieDetailController extends HttpServlet {
    private MovieDAO movieDAO;

    @Override
    public void init() throws ServletException {
        movieDAO = new MovieDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            long movieId = Long.parseLong(request.getParameter("id"));
            Movie movie = movieDAO.getMovieById(movieId);

            if (movie == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy phim");
                return;
            }

            request.setAttribute("movie", movie);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/view/customer/movieDetail.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
