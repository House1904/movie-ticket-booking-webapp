package controller;

import model.Cinema;
import model.Partner;
import service.CinemaService;
import service.UserService;

import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import util.DBConnection;

@WebServlet("/manageCinema")
public class MnCinemaController extends HttpServlet {

    private final CinemaService cinemaService = new CinemaService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // Kiểm tra đăng nhập của partner
        Partner partner = (Partner) req.getSession().getAttribute("partner");
        if (partner == null) {
            resp.sendRedirect(req.getContextPath() + "/common/login.jsp");
            return;
        }

        // Xác minh partner tồn tại trong cơ sở dữ liệu
        partner = validatePartner(partner);
        if (partner == null) {
            req.getSession().invalidate();
            req.setAttribute("error", "Tài khoản đối tác không hợp lệ!");
            req.getRequestDispatcher("/common/login.jsp").forward(req, resp);
            return;
        }

        String action = req.getParameter("action");
        long partnerId = partner.getId();

        // Xử lý hành động xóa
        if ("delete".equals(action)) {
            try {
                long id = Long.parseLong(req.getParameter("id"));
                Cinema cinema = cinemaService.getCinema(id);
                if (cinema != null && cinema.getPartner().getId() == partnerId) {
                    cinemaService.deleteCinema(id);
                    req.setAttribute("message", "Xóa rạp thành công!");
                } else {
                    req.setAttribute("error", "Rạp không tồn tại hoặc bạn không có quyền xóa!");
                }
            } catch (NumberFormatException e) {
                req.setAttribute("error", "ID rạp không hợp lệ!");
            }
        }

        // Xử lý hành động chỉnh sửa hoặc thêm mới
        Cinema cinemaToEdit;
        if ("edit".equals(action)) {
            try {
                long id = Long.parseLong(req.getParameter("id"));
                cinemaToEdit = cinemaService.getCinema(id);
                if (cinemaToEdit == null || cinemaToEdit.getPartner().getId() != partnerId) {
                    req.setAttribute("error", "Rạp không tồn tại hoặc bạn không có quyền chỉnh sửa!");
                    cinemaToEdit = new Cinema();
                    cinemaToEdit.setPartner(partner);
                }
            } catch (NumberFormatException e) {
                req.setAttribute("error", "ID rạp không hợp lệ!");
                cinemaToEdit = new Cinema();
                cinemaToEdit.setPartner(partner);
            }
        } else {
            cinemaToEdit = new Cinema();
            cinemaToEdit.setPartner(partner);
        }
        req.setAttribute("cinema", cinemaToEdit);

        // Lấy danh sách rạp của partner
        List<Cinema> cinemas = cinemaService.getCinemasByPartner(partnerId);
        req.setAttribute("cinemas", cinemas);

        RequestDispatcher rd = req.getRequestDispatcher("/view/partner/manageCinema.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // Kiểm tra đăng nhập của partner
        Partner partner = (Partner) req.getSession().getAttribute("partner");
        if (partner == null) {
            resp.sendRedirect(req.getContextPath() + "/common/login.jsp");
            return;
        }

        // Xác minh partner tồn tại trong cơ sở dữ liệu
        partner = validatePartner(partner);
        if (partner == null) {
            req.getSession().invalidate();
            req.setAttribute("error", "Tài khoản đối tác không hợp lệ!");
            req.getRequestDispatcher("/common/login.jsp").forward(req, resp);
            return;
        }

        String idStr = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String phone = req.getParameter("phone");
        long partnerId = partner.getId();


        Cinema cinema;
        try {
            if (idStr != null && !idStr.trim().isEmpty()) {
                // Cập nhật rạp
                long id = Long.parseLong(idStr);
                cinema = cinemaService.getCinema(id);
                if (cinema != null && cinema.getPartner().getId() == partnerId) {
                    cinema.setName(name.trim());
                    cinema.setAddress(address.trim());
                    cinema.setPhone(phone.trim());
                    cinemaService.updateCinema(cinema);
                    req.setAttribute("message", "Cập nhật rạp thành công!");
                } else {
                    req.setAttribute("error", "Rạp không tồn tại hoặc bạn không có quyền chỉnh sửa!");
                    req.setAttribute("cinema", new Cinema());
                    req.setAttribute("cinemas", cinemaService.getCinemasByPartner(partnerId));
                    req.getRequestDispatcher("/view/partner/manageCinema.jsp").forward(req, resp);
                    return;
                }
            } else {
                // Thêm mới rạp
                cinema = new Cinema();
                cinema.setName(name.trim());
                cinema.setAddress(address.trim());
                cinema.setPhone(phone.trim());
                cinema.setCreateAt(LocalDateTime.now());
                cinema.setPartner(partner);
                cinemaService.addCinema(cinema);
                req.setAttribute("message", "Thêm rạp thành công!");
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID rạp không hợp lệ!");
            req.setAttribute("cinema", new Cinema());
            req.setAttribute("cinemas", cinemaService.getCinemasByPartner(partnerId));
            req.getRequestDispatcher("/view/partner/manageCinema.jsp").forward(req, resp);
            return;
        } catch (Exception e) {
            String errorMessage = e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException
                    ? "Lưu rạp thất bại: Dữ liệu trùng lặp hoặc không hợp lệ (ví dụ: tên rạp hoặc số điện thoại đã tồn tại)!"
                    : "Lưu rạp thất bại: " + e.getMessage();
            req.setAttribute("error", errorMessage);
            req.setAttribute("cinema", new Cinema());
            req.setAttribute("cinemas", cinemaService.getCinemasByPartner(partnerId));
            req.getRequestDispatcher("/view/partner/manageCinema.jsp").forward(req, resp);
            return;
        }

        // Chuyển hướng về trang quản lý rạp
        resp.sendRedirect(req.getContextPath() + "/manageCinema");
    }

    // Hàm xác minh partner tồn tại trong cơ sở dữ liệu
    private Partner validatePartner(Partner partner) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            Partner managedPartner = em.find(Partner.class, partner.getId());
            return managedPartner;
        } finally {
            em.close();
        }
    }
}