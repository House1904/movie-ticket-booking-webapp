package controller;

import model.Account;
import model.Customer;
import model.Partner;
import model.Admin;
import model.enums.Role;
import service.AccountService;
import service.CustomerService;
import service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@WebServlet("/auth")
public class AuthController extends HttpServlet {
    private AccountService accountService =  new AccountService();
    private UserService userService =  new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String action = request.getParameter("action");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        if ("login".equals(action)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            try {
                Account account = accountService.login(username, password);

                if (account != null) {
                    // Truy cập user liên kết
                    HttpSession session = request.getSession();
                    session.setAttribute("account", account);

                    // Chuyển hướng theo role
                    if (account.getRole() == Role.ADMIN) {
                        Admin  admin = (Admin) account.getUser();
                        session.setAttribute("user", admin);
                        response.sendRedirect(request.getContextPath() + "/admin/home");
                    }

                    else if (account.getRole() == Role.PARTNER) {
                        Partner partner = (Partner) account.getUser();
                        session.setAttribute("user", partner);
                        response.sendRedirect(request.getContextPath() + "/partnerProfile");
                    }

                    else {
                        Customer user = (Customer) account.getUser();
                        user.setMemberShip(true);
                        session.setAttribute("user", user);
                        response.sendRedirect(request.getContextPath() + "/home");
                    }

                } else {
                    // Sai thông tin đăng nhập
                    request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
                    RequestDispatcher rd = request.getRequestDispatcher("/common/login.jsp");
                    rd.forward(request, response);
                }


          } catch (SQLException e) {
              throw new RuntimeException(e);
          }
      }
      else if ("signup".equals(action)){
          String fullname = request.getParameter("fullname");
          String email = request.getParameter("email");
          String phone = request.getParameter("phone");
            String dobStr = request.getParameter("dateOfBirth");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime dateOfBirth = LocalDateTime.parse(dobStr, formatter);
          String avatarUrl = request.getParameter("avatarUrl");

          Customer customer = new Customer(fullname, email, phone, dateOfBirth, avatarUrl);
          if (userService.userRegister(customer)){
              String username = request.getParameter("username");
              String password = request.getParameter("password");

              Account account = new Account(username, password, Role.CUSTOMER, LocalDateTime.now(), customer);
              if (accountService.register(account)){
                  RequestDispatcher rd =  request.getRequestDispatcher("/common/login.jsp");
                  rd.forward(request, response);
              }
          }
          else{
              request.setAttribute("error", "Tạo tài khoản thất bại!");
          }
      }
}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        RequestDispatcher rd;
        if ("logout".equals(action)) {
            // Hủy session khi người dùng đăng xuất
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            // Chuyển về trang home
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        else if ("signup".equals(action)) {
            // Chuyển đến trang đăng ký
            rd = request.getRequestDispatcher("/common/register.jsp");
        } else {
            // Chuyển đến trang đăng nhập
            rd = request.getRequestDispatcher("/common/login.jsp");
        }
        rd.forward(request, response);
    }

}
