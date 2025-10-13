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

@WebServlet("/admin/banners")
public class BannerController extends HttpServlet {
    private BannerService bannerService = new BannerService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            String idStr = request.getParameter("id");
            try {
                Long id = Long.parseLong(idStr);
                Banner banner = bannerService.getBannerById(id);
                request.setAttribute("banner", banner);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "ID không hợp lệ!");
            }
        }
        request.setAttribute("banners", bannerService.getAllBanners());
        request.getRequestDispatcher("/view/admin/banner.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            // Xử lý xóa banner, dùng id
            String idStr = request.getParameter("id");
            try {
                Long id = Long.parseLong(idStr);
                bannerService.deleteBanner(id);
                response.sendRedirect(request.getContextPath() + "/admin/banners");
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "ID không hợp lệ!");
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

        // Validation cho link_url (hỗ trợ anchor links)
        if (!linkUrl.matches("^(https?://.*|/[\\w/\\?&=#]+)$")) {
            request.setAttribute("errorMessage", "Link URL không hợp lệ! Vui lòng nhập URL bắt đầu bằng http://, https:// hoặc /path (có thể kèm query parameters và anchor links)");
            request.setAttribute("banners", bannerService.getAllBanners());
            request.getRequestDispatcher("/view/admin/banner.jsp").forward(request, response);
            return;
        }

        // Validation cho thời gian
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
            banner.setCreated_at(LocalDateTime.now()); // Set created_at khi thêm banner
            bannerService.addBanner(banner);
        } else if ("update".equals(action)) {
            try {
                Long id = Long.parseLong(request.getParameter("id"));
                banner.setId(id);
                bannerService.updateBanner(banner);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "ID không hợp lệ!");
                request.setAttribute("banners", bannerService.getAllBanners());
                request.getRequestDispatcher("/view/admin/banner.jsp").forward(request, response);
                return;
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/banners");
    }
}