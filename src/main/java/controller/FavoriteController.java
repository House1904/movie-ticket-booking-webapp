package controller;

import dao.FavoriteDAO;
import dao.MovieDAO;
import model.Favorite;
import model.Movie;
import model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/favorite")
public class FavoriteController extends HttpServlet {
    private FavoriteDAO favoriteDAO = new FavoriteDAO();
    private MovieDAO movieDAO = new MovieDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            out.print("{\"status\":\"error\", \"message\":\"not_logged_in\"}");
            return;
        }

        int movieId = Integer.parseInt(request.getParameter("movieId"));
        Movie movie = movieDAO.getMovieById(movieId);

        boolean isFav = favoriteDAO.exists(user, movieId);

        if (isFav) {
            favoriteDAO.removeFavorite(user, movieId);
            out.print("{\"status\":\"removed\"}");
        } else {
            favoriteDAO.addFavorite(new Favorite(user, movie));
            out.print("{\"status\":\"added\"}");
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }

        // Lấy danh sách phim yêu thích của user
        List<Favorite> favorites = favoriteDAO.findByUser(user);

        request.setAttribute("favorites", favorites);

        // Chuyển tới trang JSP hiển thị
        request.getRequestDispatcher("/view/customer/favorite.jsp").forward(request, response);
    }
}
