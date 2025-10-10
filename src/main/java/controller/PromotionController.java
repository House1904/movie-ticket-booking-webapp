package controller;

import model.Partner;
import dao.PromotionDAO;
import model.Promotion;
import model.enums.PromotionStatus;
import model.enums.PromotionType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/promotion")
public class PromotionController extends HttpServlet {

    private PromotionDAO promotionDAO = new PromotionDAO();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                req.getRequestDispatcher("view/promotionForm.jsp").forward(req, resp);
                break;
            case "edit":
                long id = Long.parseLong(req.getParameter("id"));
                Promotion promo = promotionDAO.findById(id);
                req.setAttribute("promotion", promo);
                req.getRequestDispatcher("view/promotionForm.jsp").forward(req, resp);
                break;
            case "delete":
                promotionDAO.delete(Long.parseLong(req.getParameter("id")));
                resp.sendRedirect("promotion");
                break;
            case "list":
            default:
                Long partnerId = (Long) req.getSession().getAttribute("partnerId"); // lấy từ session đăng nhập
                List<Promotion> list = promotionDAO.findByPartner(partnerId);
                req.setAttribute("promotions", list);
                req.getRequestDispatcher("view/promotionList.jsp").forward(req, resp);
                break;

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        long id = req.getParameter("id") != null && !req.getParameter("id").isEmpty()
                ? Long.parseLong(req.getParameter("id")) : 0;

        Promotion p = id > 0 ? promotionDAO.findById(id) : new Promotion();
        p.setName(req.getParameter("name"));
        p.setPromotionType(PromotionType.valueOf(req.getParameter("promotionType")));
        p.setDiscountValue(Double.parseDouble(req.getParameter("discountValue")));
        p.setMinTotalPrice(Double.parseDouble(req.getParameter("minTotalPrice")));
        p.setMaxTotalPrice(Double.parseDouble(req.getParameter("maxTotalPrice")));
        p.setStartAt(LocalDateTime.parse(req.getParameter("startAt"), formatter));
        p.setEndAt(LocalDateTime.parse(req.getParameter("endAt"), formatter));
        p.setStatus(PromotionStatus.valueOf(req.getParameter("status")));


        resp.sendRedirect("promotion");
    }

}
