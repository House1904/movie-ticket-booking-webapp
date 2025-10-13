package controller;

import model.Admin;
import model.Movie;
import service.MovieService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/manageMovie")
public class MovieController extends HttpServlet {
    private MovieService movieService = new MovieService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Admin admin = (Admin) session.getAttribute("user");
        if (admin == null){
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }
        List<Movie> movieList = movieService.getMovies();
        session.setAttribute("movies", movieList);
        response.sendRedirect(request.getContextPath() + "/view/admin/movie.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Lấy action từ form (add hoặc edit)
        String action = request.getParameter("action");
        System.out.println(action);
        String message = "";
        if ("add".equals(action) || "edit".equals(action)) {
            // Lấy dữ liệu từ popup
            String title = request.getParameter("title");
            String poster = request.getParameter("poster");
            String trailer = request.getParameter("trailer");
            String ageLimit = request.getParameter("ageLimit");
            String language = request.getParameter("language");
            String description = request.getParameter("description");
            String actor = request.getParameter("actor");
            String durationString = request.getParameter("duration");
            Long duration;
            if (durationString == null || !durationString.isEmpty()) {
                duration = Long.parseLong(durationString);
            } else duration = 0L;
            String genreString = request.getParameter("genre");
            List<String> genres = new ArrayList<>();
            if (genreString != null && !genreString.trim().isEmpty()) {
                genres = Arrays.stream(genreString.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty()) // bỏ mấy genre rỗng
                        .toList();
            } else {
                genres.add("Tất cả");
            }
            String releaseDateStr = request.getParameter("releaseDate");
            LocalDateTime releaseDate = null;
            if (releaseDateStr != null && !releaseDateStr.isEmpty()) {
                releaseDate = LocalDate.parse(releaseDateStr).atStartOfDay();
            }
            if ("add".equals(action)) {
                Movie movie = new Movie(title, description, genres, duration,ageLimit, releaseDate, language, poster, trailer, actor);
                boolean res = movieService.insertMovie(movie);
                if (res) {
                    message = "Movie added successfully";
                    System.out.println("succes");
                }
                else message = "Movie could not be added";
            }
            else if ("edit".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                Movie movie = movieService.getMovie(id);
                if (movie != null) {
                    movie.setDescription(description);
                    movie.setTitle(title);
                    movie.setGenre(genres);
                    movie.setDuration(duration);
                    movie.setAgeLimit(ageLimit);
                    movie.setLanguage(language);
                    movie.setActor(actor);
                    movie.setPosterUrl(poster);
                    movie.setTrailerUrl(trailer);
                    movie.setReleaseDate(releaseDate);
                    boolean res = movieService.updateMovie(movie);
                    if (res) {
                        message = "Movie updated successfully";
                        System.out.println("success");
                    }
                    else message = "Movie could not be updated";
                }
            }
        }
        else if ("delete".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            boolean res = movieService.deleteMovie(id);
            if (res) {
                message = "Movie deleted successfully";
                System.out.println("success");
            }
            else message = "Movie could not be deleted";
        }

        request.getSession().setAttribute("message", message);
        doGet(request, response);
    }
}
