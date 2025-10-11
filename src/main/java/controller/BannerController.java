package controller;

import model.Banner;
import service.BannerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/admin/banners")
public class BannerController extends HttpServlet {
    private BannerService bannerService = new BannerService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            String linkUrl = request.getParameter("link_url");
            Banner banner = bannerService.getBannerByLinkUrl(linkUrl);
            request.setAttribute("banner", banner);
        }
        request.setAttribute("banners", bannerService.getAllBanners());
        request.getRequestDispatcher("/view/admin/banner.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            // Xử lý xóa banner, chỉ cần link_url
            String linkUrl = request.getParameter("link_url");
            if (linkUrl != null && !linkUrl.isEmpty()) {
                bannerService.deleteBanner(linkUrl);
                response.sendRedirect(request.getContextPath() + "/admin/banners");
            } else {
                request.setAttribute("errorMessage", "Link URL không hợp lệ!");
                request.setAttribute("banners", bannerService.getAllBanners());
                request.getRequestDispatcher("/view/admin/banner.jsp").forward(request, response);
            }
            return;
        }

        // Xử lý thêm hoặc sửa banner
        Banner banner = new Banner();
        banner.setTitle(request.getParameter("title"));
        banner.setImage_url(request.getParameter("image_url"));
        String linkUrl = request.getParameter("link_url");
        banner.setLink_url(linkUrl);

        // Validation cho link_url
        if (!linkUrl.matches("^(https?://.*|/[\\w/]+)$")) {
            request.setAttribute("errorMessage", "Link URL không hợp lệ! Vui lòng nhập URL bắt đầu bằng http://, https:// hoặc /path");
            request.setAttribute("banners", bannerService.getAllBanners());
            request.getRequestDispatcher("/view/admin/banner.jsp").forward(request, response);
            return;
        }

        // Validation cho thời gian (chỉ áp dụng cho add/update)
        try {
            banner.setStart_at(LocalDateTime.parse(request.getParameter("start_at")));
            banner.setEnd_at(LocalDateTime.parse(request.getParameter("end_at")));
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Định dạng thời gian không hợp lệ!");
            request.setAttribute("banners", bannerService.getAllBanners());
            request.getRequestDispatcher("/view/admin/banner.jsp").forward(request, response);
            return;
        }

        if ("add".equals(action)) {
            bannerService.addBanner(banner);
        } else if ("update".equals(action)) {
            bannerService.updateBanner(banner);
        }

        response.sendRedirect(request.getContextPath() + "/admin/banners");
    }
}