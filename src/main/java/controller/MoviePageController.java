package controller;

import model.Movie;
import service.MovieService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/movie")
public class MoviePageController extends HttpServlet {
    private MovieService movieService = new MovieService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        List<Movie> movies = null;
        System.out.println(action);
        if ("showing".equals(action)) {
            movies = movieService.getMoviesbyIsShowing();
            session.setAttribute("movies", movies);
        }
        else if ("comming".equals(action)) {
            movies = movieService.getMoviesbyCommingSoon();
            session.setAttribute("movies", movies);
        }

        request.getRequestDispatcher("/view/customer/movie.jsp").forward(request, response);

    }
}
