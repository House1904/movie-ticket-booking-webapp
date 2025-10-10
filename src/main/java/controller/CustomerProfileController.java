package controller;

import dao.CustomerDAO;
import model.Customer;
import service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/profile")
public class CustomerProfileController extends HttpServlet {
    private CustomerService customerService = new CustomerService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        Customer customer = (Customer) session.getAttribute("user");
        if (customer == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        req.getRequestDispatcher("view/customer/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        Customer customer = (Customer) session.getAttribute("user");
        if (customer == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        // Nhận dữ liệu từ form
        String avatarUrl = req.getParameter("avatarUrl");
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String dateOfBirthStr = req.getParameter("dateOfBirth");
        boolean isMemberShip = req.getParameter("isMemberShip") != null;

        // Cập nhật thông tin user
        customer.setAvatarUrl(avatarUrl);
        customer.setFullName(fullName);
        customer.setPhone(phone);
        customer.setMemberShip(isMemberShip);

        // Parse LocalDate từ input type="date"
        if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
            customer.setDateOfBirth(LocalDate.parse(dateOfBirthStr).atStartOfDay());
        }

        // Gọi service cập nhật database
        boolean res = customerService.updateCustomer(customer);
        if (res) {
            System.out.println("Customer updated successfully");
        }
        else System.out.println("Customer update failed");

        session.setAttribute("user", customer);
        req.getRequestDispatcher("/view/customer/profile.jsp").forward(req, resp);
    }
}
