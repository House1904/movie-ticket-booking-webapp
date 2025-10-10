package controller;

import dao.PartnerDAO;
import model.Partner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/partnerProfile")
public class PartnerProfileController extends HttpServlet {
    private PartnerDAO partnerDAO = new PartnerDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long id = (Long) req.getSession().getAttribute("partnerId");
        Partner partner = partnerDAO.findById(id);

        req.setAttribute("partner", partner);
        req.getRequestDispatcher("view/partnerProfile.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long id = (Long) req.getSession().getAttribute("partnerId");
        Partner partner = partnerDAO.findById(id);

        partner.setFullName(req.getParameter("fullName"));
        partner.setPhone(req.getParameter("phone"));
        partner.setBrand(req.getParameter("brand"));
        partner.setIs_activate(req.getParameter("is_activate") != null);

        partnerDAO.update(partner);
        resp.sendRedirect("partnerProfile");
    }
}
