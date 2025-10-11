package service;

import dao.BannerDAO;
import model.Banner;
import java.util.List;

public class BannerService {
    private BannerDAO bannerDAO = new BannerDAO();

    public void addBanner(Banner banner) {
        bannerDAO.addBanner(banner);
    }

    public void updateBanner(Banner banner) {
        bannerDAO.updateBanner(banner);
    }

    public void deleteBanner(Long id) {
        bannerDAO.deleteBanner(id);
    }

    public Banner getBannerById(Long id) {
        return bannerDAO.getBannerById(id);
    }

    public List<Banner> getAllBanners() {
        return bannerDAO.getAllBanners();
    }
}