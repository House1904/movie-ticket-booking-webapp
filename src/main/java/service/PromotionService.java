package service;

import dao.PromotionDAO;
import model.Promotion;

import java.util.List;

public class PromotionService {
    private PromotionDAO promotionDAO = new PromotionDAO();
    public List<Promotion> getAllPromotions() {
        return promotionDAO.findAll();
    }
    public Promotion getPromotionById(int id) {
        return promotionDAO.findById(id);
    }
    public void createPromotion(Promotion promotion) {
        promotionDAO.insert(promotion);
    }

    public void editPromotion(Promotion promotion) {
        promotionDAO.update(promotion);
    }

    public void deletePromotion(int id) {
        promotionDAO.delete(id);
    }
}
