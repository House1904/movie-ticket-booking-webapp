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
        // ✅ Parse an toàn
        double discountValue = Double.parseDouble(req.getParameter("discountValue"));
        double minTotal = Double.parseDouble(req.getParameter("minTotalPrice"));
        double maxTotal = req.getParameter("maxTotalPrice") != null && !req.getParameter("maxTotalPrice").isEmpty()
                ? Double.parseDouble(req.getParameter("maxTotalPrice"))
                : 0.0;
        p.setDiscountValue(discountValue);
        p.setMinTotalPrice(minTotal);
        p.setMaxTotalPrice(maxTotal);

        LocalDateTime startAt = LocalDateTime.parse(req.getParameter("startAt"), formatter);
        LocalDateTime endAt = LocalDateTime.parse(req.getParameter("endAt"), formatter);

        // ✅ 1. Kiểm tra ngày bắt đầu < ngày kết thúc
        if (startAt.isAfter(endAt)) {
            req.setAttribute("error", "⛔ Ngày bắt đầu phải trước ngày kết thúc!");
            req.setAttribute("promotion", p);
            req.getRequestDispatcher("view/admin/promotionForm.jsp").forward(req, resp);
            return;
        }
        // ✅ 2. Check giá trị giảm % hợp lệ
        if (p.getPromotionType() == PromotionType.PERCENT &&
                (discountValue <= 0 || discountValue > 100)) {
            req.setAttribute("error", "⚠️ Giá trị giảm không hợp lệ! Phải nằm trong khoảng 1% đến 100%.");
            req.setAttribute("promotion", p);
            req.getRequestDispatcher("view/admin/promotionForm.jsp").forward(req, resp);
            return;
        }

        p.setStartAt(startAt);
        p.setEndAt(endAt);
        p.setStatus(PromotionStatus.valueOf(req.getParameter("status")));

        // ✅ 2. Nếu là AMOUNT → set maxTotalPrice = discountValue
        String type = req.getParameter("promotionType");
        if (PromotionType.valueOf(type) == PromotionType.AMOUNT) {
            p.setMaxTotalPrice(p.getDiscountValue());
        } else {
            String maxStr = req.getParameter("maxTotalPrice");
            p.setMaxTotalPrice(maxStr != null && !maxStr.isEmpty()
                    ? Double.parseDouble(maxStr)
                    : 0.0);
        }

        // ✅ 3. Lưu hoặc cập nhật
        if (id == 0) {
            promotionService.createPromotion(p);
        } else {
            promotionService.editPromotion(p);
        }

        resp.sendRedirect("promotion");
    }

}
