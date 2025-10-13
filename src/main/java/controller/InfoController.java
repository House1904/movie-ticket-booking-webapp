package controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/page")
public class InfoController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String page = "/common/about-us.jsp"; // default

        switch (action) {
            case "about":
                page = "/common/about-us.jsp";
                break;
            case "privacy":
                page = "/common/privacy.jsp";
                break;
            case "support":
                page = "/common/support.jsp";
                break;
            default:
                page = "/common/about-us.jsp"; // fallback
                break;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }
}
