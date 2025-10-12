package controller;

import dao.MovieDAO;
import dao.FavoriteDAO;
import model.Movie;
import model.User;
import util.DBConnection;
import service.FavoriteService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@WebServlet("/movieDetail")
public class MovieDetailController extends HttpServlet {
    private MovieDAO movieDAO = new MovieDAO() ;
    private FavoriteService favoriteService;

    @Override
    public void init() throws ServletException {
        EntityManager em = util.DBConnection.getEmFactory().createEntityManager();
        this.favoriteService = new FavoriteService(new FavoriteDAO(em));
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
            User user = (User) request.getSession().getAttribute("user");
            boolean isFavorited = false;

            if (user != null) {
                isFavorited = favoriteService.isFavorite(user, movieId);
            }

            request.setAttribute("isFavorited", isFavorited);
            request.getRequestDispatcher("/view/customer/movieDetail.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
