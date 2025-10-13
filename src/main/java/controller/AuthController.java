package controller;

import model.Account;
import model.Admin;
import model.Customer;
import model.Partner;
import model.enums.Role;
import org.mindrot.jbcrypt.BCrypt;
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

@WebServlet("/auth")
public class  AuthController extends HttpServlet {
    private AccountService accountService =  new AccountService();
    private UserService userService =  new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if ("login".equals(action)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            try {
                Account account = accountService.login(username, password);

                if (account != null) {
                    session.setAttribute("account", account);

                    // Chuyển hướng theo role
                    if (account.getRole() == Role.ADMIN) {
                        Admin  admin = (Admin) account.getUser();
                        session.setAttribute("user", admin);
                        response.sendRedirect(request.getContextPath() + "/admin");
                    } else if (account.getRole() == Role.PARTNER) {
                        // Lưu Partner vào session
                        Partner partner = (Partner) account.getUser();
                        session.setAttribute("user", partner);
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                    } else {
                        // Truy cập user liên kết
                        Customer user = (Customer) account.getUser();
                        user.setMemberShip(true);
                        session.setAttribute("user", user);
                        response.sendRedirect(request.getContextPath() + "/home");
                    }
                }
                else {
                    request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
                    request.getRequestDispatcher("/common/login.jsp").forward(request, response);
                }
          } catch (SQLException e) {
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
                RequestDispatcher rd = request.getRequestDispatcher("/common/login.jsp");
                rd.forward(request, response);
          }
      }
        else if ("signup".equals(action)) {
            try {
                String fullname = request.getParameter("fullname");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String dobStr = request.getParameter("dateOfBirth");
                LocalDate dateOfBirth = LocalDate.parse(dobStr);
                String avatarUrl = request.getParameter("avatarUrl");

                Customer customer = new Customer(fullname, email, phone, dateOfBirth, avatarUrl);

                if (userService.userRegister(customer)) {
                    // Đăng ký Customer thành công
                    String username = request.getParameter("username");
                    String password = request.getParameter("password");
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                    Account account = new Account(username, hashedPassword, Role.CUSTOMER, LocalDateTime.now(), customer);

                    if (accountService.register(account)) {
                        // Thành công
                        RequestDispatcher rd = request.getRequestDispatcher("/common/login.jsp");
                        rd.forward(request, response);
                    } else {
                        // Tạo account thất bại
                        request.setAttribute("error", "Tên đăng nhập đã tồn tại hoặc tạo tài khoản thất bại!");
                        RequestDispatcher rd = request.getRequestDispatcher("/common/register.jsp");
                        rd.forward(request, response);
                    }
                } else {
                    // Đăng ký Customer thất bại (email đã tồn tại)
                    request.setAttribute("error", "Email đã được sử dụng!");
                    RequestDispatcher rd = request.getRequestDispatcher("/common/register.jsp");
                    rd.forward(request, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Đã xảy ra lỗi trong quá trình đăng ký!");
                RequestDispatcher rd = request.getRequestDispatcher("/common/register.jsp");
                rd.forward(request, response);
            }
        }
        else if ("changePassword".equals(action)) {
            Account account = (Account) session.getAttribute("account");
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");

            try {
                // Kiểm tra mật khẩu cũ
                if (!BCrypt.checkpw(oldPassword, account.getPassword())) {
                    request.setAttribute("error", "Mật khẩu hiện tại không đúng!");
                    request.getRequestDispatcher("/common/changePassword.jsp").forward(request, response);
                    return;
                }

                // Kiểm tra xác nhận mật khẩu
                if (!newPassword.equals(confirmPassword)) {
                    request.setAttribute("error", "Mật khẩu mới và xác nhận không khớp!");
                    request.getRequestDispatcher("/common/changePassword.jsp").forward(request, response);
                    return;
                }

                // Hash và update
                String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                account.setPassword(hashedNewPassword);
                boolean updated = accountService.updatePassword(account);

                if (updated) {
                    session.invalidate();
                    request.getRequestDispatcher("/common/login.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Đổi mật khẩu thất bại, vui lòng thử lại!");
                }

            } catch (Exception e) {
                e.printStackTrace(); // In ra log server
                request.setAttribute("error", "Lỗi hệ thống, vui lòng thử lại!");
                request.getRequestDispatcher("/common/changePassword.jsp").forward(request, response);
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
        } else if ("signup".equals(action)) {
            // Chuyển đến trang đăng ký
            rd = request.getRequestDispatcher("/common/register.jsp");
        } else {
            // Chuyển đến trang đăng nhập
            rd = request.getRequestDispatcher("/common/login.jsp");
        }
        rd.forward(request, response);
    }
}