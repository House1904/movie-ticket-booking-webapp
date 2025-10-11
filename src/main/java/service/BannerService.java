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

    public void deleteBanner(String linkUrl) {
        bannerDAO.deleteBanner(linkUrl);
    }

    public Banner getBannerByLinkUrl(String linkUrl) {
        return bannerDAO.getBannerByLinkUrl(linkUrl);
    }

    public List<Banner> getAllBanners() {
        return bannerDAO.getAllBanners();
    }
}