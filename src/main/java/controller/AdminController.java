package controller;

import model.Account;
import model.Partner;
import model.enums.Role;
import org.mindrot.jbcrypt.BCrypt;
import service.AccountService;
import service.PartnerService;
import service.AdminReportService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@WebServlet("/admin")
public class AdminController extends HttpServlet {
    private AdminReportService adminReportService = new AdminReportService();
    private PartnerService partnerService = new PartnerService();;
    private AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");
        RequestDispatcher rd = request.getRequestDispatcher("/view/admin/dashboard.jsp");

        if (action == null || action.equals("statistic")) {
            Map<String, Double> revenueMap = adminReportService.getTodayRevenueByPartner();
            request.setAttribute("revenueMap", revenueMap);

            String date = request.getParameter("date");
            Double totalRevenue = adminReportService.totalRevenueByDate(LocalDate.now().toString());
            request.setAttribute("totalRevenue", totalRevenue);

            List<Object[]> topPartners = adminReportService.getTop3PartnersToday();
            request.setAttribute("topPartners", topPartners);

            List<Object[]> topMovies = adminReportService.getTop5MoviesToday();
            request.setAttribute("topMovies", topMovies);
        }
        else if (action.equals("list")) {
            request.setAttribute("partners", partnerService.getAllPartners());
            rd = request.getRequestDispatcher("/view/admin/managePartner.jsp");
        }
        else if (action.equals("edit")) {
            try {
                long partnerId = Long.parseLong(request.getParameter("id"));
                Partner partner = partnerService.findPartnerById(partnerId);
                if (partner != null) {
                    request.setAttribute("partner", partner);
                    rd = request.getRequestDispatcher("/view/admin/managePartner.jsp");
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đối tác");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID đối tác không hợp lệ");
            }
        }
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            Partner partner = new Partner();
            partner.setFullName(request.getParameter("fullName"));
            partner.setEmail(request.getParameter("email"));
            partner.setPhone(request.getParameter("phone"));
            partner.setBrand(request.getParameter("brand"));

            String errorCode = null;

            // --- Kiểm tra hợp lệ ---
            if (partnerService.isEmailExists(partner.getEmail())) {
                errorCode = "email_exists";
            } else if (partnerService.isPhoneExists(partner.getPhone())) {
                errorCode = "phone_exists";
            } else if (!partner.getPhone().matches("\\d{10}")) {
                errorCode = "invalid_phone";
            } else if (!partner.getEmail().matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
                errorCode = "invalid_email";
            }

            if (errorCode != null) {
                response.sendRedirect("admin?action=list&error=" + errorCode);
                return;
            }

            try {
                // 🔹 1. Lưu Partner trước
                partnerService.addPartner(partner);

                // 🔹 2. Sau đó tạo Account
                Account account = new Account();
                String username = partner.getEmail().split("@")[0];
                String password = "defaultPass123";
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                account.setUserName(username);
                account.setPassword(hashedPassword);
                account.setRole(Role.PARTNER);
                account.setCreatedAt(LocalDateTime.now());
                account.setUpdatedAt(LocalDateTime.now());
                account.setUser(partner); // gán partner đã lưu (đã có ID)

                // 🔹 3. Lưu Account
                accountService.addAccount(account);

                response.sendRedirect("admin?action=list&message=added");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("admin?action=list&error=server_error");
            }
            return;
        }
        else if (action.equals("update")) {
            try {
                long partnerId = Long.parseLong(request.getParameter("id"));
                Partner partner = partnerService.findPartnerById(partnerId);
                if (partner != null) {
                    partner.setFullName(request.getParameter("fullName"));
                    partner.setEmail(request.getParameter("email"));
                    partner.setPhone(request.getParameter("phone"));
                    partner.setBrand(request.getParameter("brand"));
                    partnerService.updatePartner(partner);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "KhĂ´ng tĂ¬m tháº¥y Ä‘á»‘i tĂ¡c");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID Ä‘á»‘i tĂ¡c khĂ´ng há»£p lá»‡");
            }
        }
        else if (action.equals("delete")) {
            try {
                long partnerId = Long.parseLong(request.getParameter("id"));
                partnerService.deletePartner(partnerId);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID Ä‘á»‘i tĂ¡c khĂ´ng há»£p lá»‡");
            }
        }

        response.sendRedirect("admin?action=list");
    }
}
