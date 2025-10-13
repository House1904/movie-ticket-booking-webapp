package controller;

import dao.PartnerDAO;
import model.Partner;
import service.PartnerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/partnerProfile")
public class PartnerProfileController extends HttpServlet {
    private PartnerService partnerService = new PartnerService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        Partner partner = (Partner) session.getAttribute("user");

        req.setAttribute("partner", partner);
        req.getRequestDispatcher("view/partner/profile.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Partner partner = (Partner) req.getSession().getAttribute("user");

        partner.setFullName(req.getParameter("fullName"));
        partner.setPhone(req.getParameter("phone"));
        partner.setBrand(req.getParameter("brand"));

        partnerService.updatePartner(partner);

        resp.sendRedirect("partnerProfile");
    }
}
