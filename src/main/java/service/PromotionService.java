package service;

import dao.PromotionDAO;
import model.Promotion;

import java.util.List;

public class PromotionService {
    private PromotionDAO promotionDAO = new PromotionDAO();
    public List<Promotion> getAllPromotions() {
        return promotionDAO.findAll();
    }
    public Promotion getPromotionById(long id) {
        return promotionDAO.findById(id);
    }
    public void createPromotion(Promotion promotion) {
        promotionDAO.insert(promotion);
    }

    public void editPromotion(Promotion promotion) {
        promotionDAO.update(promotion);
    }

    public void deletePromotion(Promotion promotion) {
        promotionDAO.delete(promotion);
    }

    // ✅ Lấy danh sách khuyến mãi hợp lệ theo tổng tiền đơn hàng
    public List<Promotion> getValidPromotions(double totalPrice) {
        return promotionDAO.findValidPromotions(totalPrice);
    }

    // ⚙️ Cập nhật trạng thái hết hạn tự động
    public void updateExpiredPromotions() {
        promotionDAO.expireOutdatedPromotions();
    }

    // 💰 Tính tổng tiền sau khi áp dụng nhiều khuyến mãi
    public double applyPromotions(double originalPrice, List<Promotion> promotions) {
        double total = originalPrice;
        for (Promotion p : promotions) {
            switch (p.getPromotionType()) {
                case PERCENT:
                    total -= total * (p.getDiscountValue() / 100);
                    break;
                case AMOUNT:
                    total -= p.getDiscountValue();
                    break;
            }
        }
        return Math.max(total, 0); // tránh âm
    }


}