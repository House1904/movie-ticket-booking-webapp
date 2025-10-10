package controller;

import dao.CustomerDAO;
import model.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/profile")
public class CustomerProfileController extends HttpServlet {
    private CustomerDAO customerDAO = new CustomerDAO();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Customer customer = customerDAO.findById(userId);
        req.setAttribute("customer", customer);
        req.getRequestDispatcher("view/partner/profile.jsp").forward(req, resp);
    }
}
