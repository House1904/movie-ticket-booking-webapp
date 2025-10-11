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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/movie")
public class MoviePageController extends HttpServlet {
    private MovieService movieService = new MovieService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        List<String> genreList = null;
        List<Movie> movies = null;
        String h1 = "";
        String p = "";
        if ("showing".equals(action)) {
            h1 = "Phim đang chiếu";
            p = "Danh sách các phim đang chiếu trên toàn quốc";
            movies = movieService.getMoviesbyIsShowing();
        }
        else if ("comming".equals(action)) {
            h1 = "Phim sắp chiếu";
            p = "Danh sách các phim sắp chiếu trên toàn quốc";
            movies = movieService.getMoviesbyCommingSoon();
        }

        else if ("search".equals(action)) {
            h1 = "Phim theo tìm kiếm";
            String keyword = request.getParameter("q");
            p = "Từ khóa: " + keyword;
            if (keyword != null) {
                movies = movieService.getMoviesbyKeyWord(keyword);
            }
            else movies = movieService.getMovies();
        }

        try {
            genreList = movieService.getGenres();
            session.setAttribute("genreList", genreList);
        } catch (SQLException e) {
            session.setAttribute("error", "Có lỗi xảy ra khi tìm thể loại phim!");
            throw new RuntimeException(e);
        }
        session.setAttribute("h1", h1);
        session.setAttribute("p", p);
        session.setAttribute("movies", movies);
        request.getRequestDispatcher("/view/customer/movie.jsp").forward(request, response);

    }
}
