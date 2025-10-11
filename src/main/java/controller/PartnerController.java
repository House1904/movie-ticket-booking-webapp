package controller;

import model.Account;
import model.Partner;
import model.enums.Role;
import service.AccountService;
import service.PartnerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/partner")
public class PartnerController extends HttpServlet {
    private PartnerService partnerService;
    private AccountService accountService;

    @Override
    public void init() throws ServletException {
        partnerService = new PartnerService();
        accountService = new AccountService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");

        if (action == null || action.equals("list")) {
            request.setAttribute("partners", partnerService.getAllPartners());
            request.getRequestDispatcher("/view/partner/index.jsp").forward(request, response);
        }
        else if (action.equals("edit")) {
            try {
                long partnerId = Long.parseLong(request.getParameter("id"));
                Partner partner = partnerService.findPartnerById(partnerId);
                if (partner != null) {
                    request.setAttribute("partner", partner);
                    request.getRequestDispatcher("/view/partner/index.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đối tác");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID đối tác không hợp lệ");
            }
        }
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
            partner.setIs_activate(true);

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
                response.sendRedirect("partner?action=list&error=" + errorCode);
                return;
            }

            // --- Nếu hợp lệ thì tạo tài khoản ---
            Account account = new Account();
            String username = partner.getEmail().split("@")[0];
            String rawPassword = "defaultPass123";

            account.setUserName(username);
            account.setPassword(rawPassword);
            account.setRole(Role.PARTNER);
            account.setCreatedAt(LocalDateTime.now());
            account.setUpdatedAt(LocalDateTime.now());
            account.setUser(partner);

            accountService.addAccount(account);

            response.sendRedirect("partner?action=list&message=added");
            return;
        }else if (action.equals("update")) {
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

            response.sendRedirect("partner?action=list");
        }

}
