package controller;

import model.Partner;
import dao.PromotionDAO;
import model.Promotion;
import model.enums.PromotionStatus;
import model.enums.PromotionType;
import service.PromotionService;

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

    private PromotionService promotionService = new PromotionService();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        long id = req.getParameter("id") != null && !req.getParameter("id").isEmpty()
                ? Long.parseLong(req.getParameter("id")) : 0;
        Promotion promo = promotionService.getPromotionById(id);
        if (action == null) action = "list";

        switch (action) {
            case "cancel":
                resp.sendRedirect("promotion");
                break;
            case "new":
                req.getRequestDispatcher("view/admin/promotionForm.jsp").forward(req, resp);
                break;
            case "edit":
                req.setAttribute("promotion", promo);
                req.getRequestDispatcher("view/admin/promotionForm.jsp").forward(req, resp);
                break;
            case "delete":
                promotionService.deletePromotion(promo);
                resp.sendRedirect("promotion");
                break;
            case "list":
            default:
                List<Promotion> list = promotionService.getAllPromotions();
                req.getSession().setAttribute("promotions", list);
                req.getRequestDispatcher("view/admin/promotionList.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        long id = req.getParameter("id") != null && !req.getParameter("id").isEmpty()
                ? Long.parseLong(req.getParameter("id")) : 0;

        Promotion p = id > 0 ? promotionService.getPromotionById(id) : new Promotion();
        p.setName(req.getParameter("name"));
        p.setPromotionType(PromotionType.valueOf(req.getParameter("promotionType")));
        p.setDiscountValue(Double.parseDouble(req.getParameter("discountValue")));
        p.setMinTotalPrice(Double.parseDouble(req.getParameter("minTotalPrice")));
        p.setMaxTotalPrice(Double.parseDouble(req.getParameter("maxTotalPrice")));
        p.setStartAt(LocalDateTime.parse(req.getParameter("startAt"), formatter));
        p.setEndAt(LocalDateTime.parse(req.getParameter("endAt"), formatter));
        p.setStatus(PromotionStatus.valueOf(req.getParameter("status")));

        if(id == 0){
            promotionService.createPromotion(p);
        }
        else {
            promotionService.editPromotion(p);
        }
        resp.sendRedirect("promotion");
    }
}
