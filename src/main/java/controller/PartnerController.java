package controller;

import service.PartnerService;
import model.Partner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/partner")
public class PartnerController extends HttpServlet {
    private PartnerService partnerService;

    @Override
    public void init() throws ServletException {
        partnerService = new PartnerService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Đặt mã hóa UTF-8 cho request
        response.setContentType("text/html; charset=UTF-8"); // Đặt mã hóa UTF-8 cho response
        String action = request.getParameter("action");
        if (action == null || action.equals("list")) {
            request.setAttribute("partners", partnerService.getAllPartners());
            request.getRequestDispatcher("/view/partner/index.jsp").forward(request, response);
        } else if (action.equals("edit")) {
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
        request.setCharacterEncoding("UTF-8"); // Đặt mã hóa UTF-8 cho request
        response.setContentType("text/html; charset=UTF-8"); // Đặt mã hóa UTF-8 cho response
        String action = request.getParameter("action");
        if (action.equals("add")) {
            Partner partner = new Partner();
            partner.setFullName(request.getParameter("fullName"));
            partner.setEmail(request.getParameter("email"));
            partner.setPhone(request.getParameter("phone"));
            partner.setBrand(request.getParameter("brand"));
            partner.setIs_activate(true); // Mặc định true
            partnerService.addPartner(partner);
        } else if (action.equals("update")) {
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
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đối tác");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID đối tác không hợp lệ");
            }
        } else if (action.equals("delete")) {
            try {
                long partnerId = Long.parseLong(request.getParameter("id"));
                partnerService.deletePartner(partnerId);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID đối tác không hợp lệ");
            }
        }
        response.sendRedirect("partner?action=list");
    }
}