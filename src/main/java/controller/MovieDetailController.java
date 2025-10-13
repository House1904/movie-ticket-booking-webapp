package controller;

import dao.MovieDAO;
import dao.FavoriteDAO;
import dao.RatingDAO;
import model.Movie;
import model.User;
import model.Customer;
import model.Rating;
import model.Ticket;
import service.TicketService;
import service.FavoriteService;
import service.RatingService;
import util.DBConnection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/movieDetail")
public class MovieDetailController extends HttpServlet {
    private MovieDAO movieDAO = new MovieDAO();
    private FavoriteService favoriteService;
    private RatingService ratingService;
    private TicketService ticketService = new TicketService();

    @Override
    public void init() throws ServletException {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        this.favoriteService = new FavoriteService(new FavoriteDAO(em));
        this.ratingService = new RatingService(new RatingDAO(em));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idParam = request.getParameter("id");
            if (idParam == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số id phim");
                return;
            }

            long movieId = Long.parseLong(request.getParameter("id"));
            Movie movie = movieDAO.getMovieById(movieId);

            if (movie == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy phim");
                return;
            }

            request.setAttribute("movie", movie);

            List<Rating> ratings = ratingService.getRatingsByMovie(movieId);
            int ratingCount = ratings.size();
            double averageRating = ratings.stream()
                    .mapToInt(Rating::getRating)
                    .average()
                    .orElse(0.0);
            int averageRatingFloor = (int) averageRating;;
            boolean hasHalfStar = (averageRating - averageRatingFloor) >= 0.5;

            if (ratingCount > 0) {
                averageRating = ratings.stream()
                        .mapToInt(Rating::getRating)
                        .average()
                        .orElse(0.0);
                averageRatingFloor = (int) Math.floor(averageRating);
                hasHalfStar = (averageRating - averageRatingFloor) >= 0.5;
            }

            request.setAttribute("ratings", ratings);
            request.setAttribute("ratingCount", ratingCount);
            request.setAttribute("averageRating", averageRating);
            request.setAttribute("averageRatingFloor", averageRatingFloor);
            request.setAttribute("hasHalfStar", hasHalfStar);

            User user = (User) request.getSession().getAttribute("user");
            boolean isFavorited = false;
            if (user != null) {
                isFavorited = favoriteService.isFavorite(user, movieId);
            }

            request.setAttribute("isFavorited", isFavorited);

            Customer customer = (Customer) user; // ép kiểu sang Customer
            boolean canRate = false;

            if (customer != null) {
                // Lấy danh sách vé của user cho phim này
                List<Ticket> ticketsForMovie = ticketService.getTicketByCustomerAndMovie(customer.getId(), movieId);
                canRate = !ticketsForMovie.isEmpty();
            }

            request.setAttribute("canRate", canRate);
            request.setAttribute("ratings", ratingService.getRatingsByMovie(movie.getId()));

            request.getRequestDispatcher("/view/customer/movieDetail.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
