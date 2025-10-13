package controller;

import model.Customer;
import model.Movie;
import model.Rating;
import service.RatingService;
import dao.RatingDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.time.format.DateTimeFormatter;

@WebServlet("/rating")
public class    RatingController extends HttpServlet {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("ProjectLoad");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");
        Customer customer = (Customer) req.getSession().getAttribute("user");
        if (customer == null) {
            HttpSession session = req.getSession();
            String currentURL = req.getRequestURI() +
                    (req.getQueryString() != null ? "?" + req.getQueryString() : "");
            session.setAttribute("redirectAfterLogin", currentURL);
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Chưa đăng nhập\"}");
            return;
        }

        try {
            long movieId = Long.parseLong(req.getParameter("movieId"));
            int ratingValue = Integer.parseInt(req.getParameter("rating"));
            String comment = req.getParameter("comment");

            EntityManager em = emf.createEntityManager();
            try {
                RatingDAO ratingDAO = new RatingDAO(em);
                RatingService ratingService = new RatingService(ratingDAO);

                Movie movie = em.find(Movie.class, movieId);
                if(movie == null){
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\": \"Phim không tồn tại\"}");
                    return;
                }

                if(!ratingService.hasRated(customer, movie)){
                    Rating rating = new Rating();
                    rating.setMovie(movie);
                    rating.setCustomer(customer);
                    rating.setRating(ratingValue); // int ok
                    rating.setContent(comment);
                    rating.setCreated_at(LocalDateTime.now());

                    ratingService.addRating(rating);
                }

                resp.getWriter().write("{\"success\": true}");
            } finally {
                em.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Server error\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        try {
            long movieId = Long.parseLong(req.getParameter("movieId"));

            EntityManager em = emf.createEntityManager();
            try {
                RatingDAO ratingDAO = new RatingDAO(em);
                RatingService ratingService = new RatingService(ratingDAO);

                List<Rating> ratings = ratingService.getRatingsByMovie(movieId);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                StringBuilder json = new StringBuilder();
                json.append("[");

                for (int i = 0; i < ratings.size(); i++) {
                    Rating r = ratings.get(i);
                    json.append("{")
                            .append("\"username\":\"").append(r.getCustomer().getFullName()).append("\",")
                            .append("\"stars\":").append(r.getRating()).append(",")
                            .append("\"comment\":\"").append(r.getContent() == null ? "" : r.getContent().replace("\"","\\\"")).append("\",")
                            .append("\"createdAt\":\"").append(r.getCreated_at().format(formatter)).append("\"")
                            .append("}");
                    if (i < ratings.size() - 1) json.append(",");
                }

                json.append("]");
                resp.getWriter().write(json.toString());

            } finally {
                em.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Server error\"}");
        }
    }
}
