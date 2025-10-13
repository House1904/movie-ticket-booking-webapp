package controller;

import dao.FavoriteDAO;
import dao.MovieDAO;
import model.Favorite;
import model.Movie;
import model.User;
import service.FavoriteService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/favorite")
public class FavoriteController extends HttpServlet {

    private EntityManagerFactory emf;
    private EntityManager em;
    private FavoriteDAO favoriteDAO;
    private MovieDAO movieDAO;
    private FavoriteService favoriteService;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("ProjectLoad");
        em = emf.createEntityManager();

        favoriteDAO = new FavoriteDAO(em);
        movieDAO = new MovieDAO();
        favoriteService = new FavoriteService(favoriteDAO);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            String currentURL = request.getRequestURI() +
                    (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            session.setAttribute("redirectAfterLogin", currentURL);
            out.print("{\"status\":\"error\",\"message\":\"not_logged_in\"}");
            out.flush();
            return;
        }

        try {
            long movieId = Long.parseLong(request.getParameter("movieId"));
            Movie movie = movieDAO.getMovieById(movieId);

            if (movie == null) {
                out.print("{\"status\":\"error\",\"message\":\"movie_not_found\"}");
                out.flush();
                return;
            }

            boolean added = favoriteService.toggleFavorite(user, movie);

            if (added) {
                out.print("{\"status\":\"added\"}");
            } else {
                out.print("{\"status\":\"removed\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        String currentURL = "";
        if (user == null) {
            currentURL = request.getRequestURI() +
                    (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            session.setAttribute("redirectAfterLogin", currentURL);
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }

        List<Favorite> favorites = favoriteService.getFavoritesByUser(user);
        request.setAttribute("favorites", favorites);

        request.getRequestDispatcher("/view/customer/favorite.jsp").forward(request, response);
    }

    @Override
    public void destroy() {
        if (em != null && em.isOpen()) em.close();
        if (emf != null && emf.isOpen()) emf.close();
    }
}