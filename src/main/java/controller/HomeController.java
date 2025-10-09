package controller;

import model.Article;
import model.Cinema;
import model.Movie;
import service.ArticleService;
import service.CinemaService;
import service.MovieService;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/home")
public class HomeController extends HttpServlet {
    private MovieService movieService = new MovieService();
    private CinemaService cinemaService = new CinemaService();
    private ArticleService articleService = new ArticleService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Movie> nowShowingMovies = movieService.getNowShowingMovies();
            List<Movie> upcomingMovies = movieService.getUpcomingMovies();
            List<Cinema> cinemas = cinemaService.getCinemas();
            List<Article> articles = articleService.getArticles();

            HttpSession session = req.getSession();
            session.setAttribute("nowShowingMovies", nowShowingMovies);
            session.setAttribute("upcomingMovies", upcomingMovies);
            session.setAttribute("cinemas", cinemas);
            session.setAttribute("articles", articles);

            RequestDispatcher rd = req.getRequestDispatcher("/view/customer/home.jsp");
            rd.forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}