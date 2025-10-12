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
            resp.sendRedirect("/common/login.jsp");
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
            resp.sendRedirect("/common/login.jsp");
            return;
        }

        // Nhận dữ liệu từ form
        String avatarUrl = req.getParameter("avatarUrl");
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String dateOfBirthStr = req.getParameter("dateOfBirth");
        boolean isMemberShip = req.getParameter("isMemberShip") != null;

        // 🔹 1. Kiểm tra tên không được để trống
        if (fullName == null || fullName.trim().isEmpty()) {
            req.setAttribute("error", "Không được để trống họ tên.");
            req.getRequestDispatcher("/view/customer/profile.jsp").forward(req, resp);
            return;
        }

        // 🔹 2. Kiểm tra số điện thoại hợp lệ
        String phoneRegex = "^(0[0-9]{9})$"; // 10 chữ số, bắt đầu bằng 0
        if (phone == null || phone.trim().isEmpty() || !phone.matches(phoneRegex)) {
            req.setAttribute("error", "Số điện thoại không hợp lệ. Vui lòng nhập đúng định dạng (10 chữ số, bắt đầu bằng 0).");
            req.getRequestDispatcher("/view/customer/profile.jsp").forward(req, resp);
            return;
        }

        // 🔹 3. Cập nhật thông tin user
        customer.setAvatarUrl(avatarUrl);
        customer.setFullName(fullName.trim());
        customer.setPhone(phone.trim());
        customer.setMemberShip(isMemberShip);

        // 🔹 4. Parse ngày sinh (nếu có)
        if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
            customer.setDateOfBirth(LocalDate.parse(dateOfBirthStr).atStartOfDay());
        }

        // 🔹 5. Cập nhật DB
        boolean res = customerService.updateCustomer(customer);
        if (res) {
            System.out.println("Customer updated successfully");
        } else {
            System.out.println("Customer update failed");
        }

        session.setAttribute("user", customer);
        req.setAttribute("success", "Cập nhật thông tin thành công!");
        req.getRequestDispatcher("/view/customer/profile.jsp").forward(req, resp);
    }

}
