package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/rating")
public class RatingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        long movieId = Long.parseLong(req.getParameter("movieId"));

        String json = "[]";
        if (movieId == 2) {
            json = "["
                    + "{\"username\":\"David\",\"stars\":10,\"comment\":\"Một kiệt tác điện ảnh!\"},"
                    + "{\"username\":\"Emma\",\"stars\":9,\"comment\":\"Âm nhạc tuyệt vời.\"},"
                    + "{\"username\":\"Frank\",\"stars\":8,\"comment\":\"Kết thúc hơi buồn nhưng hay.\"}"
                    + "]";
        } else {
            json = "[{\"username\":\"Alice\",\"stars\":8,\"comment\":\"Phim rất cảm động!\"}]";
        }

        resp.getWriter().write(json);
    }
}
