package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/home")
public class AdminHomeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Tạm thời chỉ forward đến trang JSP để test giao diện
        request.getRequestDispatcher("/view/admin/home.jsp").forward(request, response);
    }
}