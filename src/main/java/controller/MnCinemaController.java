package controller;

import model.Account;
import model.Cinema;
import model.Partner;
import model.enums.Role;
import service.CinemaService;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/manageCinema")
public class MnCinemaController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MnCinemaController.class.getName());
    private final CinemaService cinemaService = new CinemaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // Kiểm tra đăng nhập của partner
        Partner partner = (Partner) req.getSession().getAttribute("user");
        Account account = (Account) req.getSession().getAttribute("account");

        if (partner == null || account == null || account.getRole() != Role.PARTNER) {
            LOGGER.warning("No valid partner session found. User: " + (partner != null ? partner.getId() : "null") +
                    ", Account: " + (account != null ? account.getUserName() : "null"));
            req.setAttribute("error", "Vui lòng đăng nhập để tiếp tục!");
            resp.sendRedirect(req.getContextPath() + "/auth?action=login&redirect=/manageCinema");
            return;
        }

        String action = req.getParameter("action");
        long partnerId = partner.getId();

        // Xử lý hành động xóa
        if ("delete".equals(action)) {
            try {
                long id = Long.parseLong(req.getParameter("id"));
                Cinema cinema = cinemaService.findById(id);
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
                cinemaToEdit = cinemaService.findById(id);
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
        List<Cinema> cinemas = cinemaService.getCinemasByPartnerId(partnerId);
        req.setAttribute("cinemas", cinemas);

        RequestDispatcher rd = req.getRequestDispatcher("/view/partner/manageCinema.jsp");
        if ("mnAudit".equals(action)) {
            Long cinemaId = Long.parseLong(req.getParameter("cinemaId"));
            req.getSession().setAttribute("cinemaId", cinemaId);
            rd = req.getRequestDispatcher("/AuditoriumController");
        }
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        Partner partner = (Partner) req.getSession().getAttribute("user");
        Account account = (Account) req.getSession().getAttribute("account");

        if (partner == null || account == null || account.getRole() != Role.PARTNER) {
            LOGGER.warning("No valid partner session found. User: " + (partner != null ? partner.getId() : "null") +
                    ", Account: " + (account != null ? account.getUserName() : "null"));
            req.setAttribute("error", "Vui lòng đăng nhập để tiếp tục!");
            resp.sendRedirect(req.getContextPath() + "/auth?action=login&redirect=/manageCinema");
            return;
        }

        String idStr = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String phone = req.getParameter("phone");
        long partnerId = partner.getId();
        boolean isNew = (idStr == null || idStr.trim().isEmpty());

        // Kiểm tra rỗng
        if (name == null || name.trim().isEmpty() ||
                address == null || address.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty()) {

            req.setAttribute("error", "Tên, địa chỉ và số điện thoại không được để trống!");
            Cinema cinemaToShow = new Cinema();
            cinemaToShow.setId(isNew ? 0 : Long.parseLong(idStr));
            cinemaToShow.setName(name);
            cinemaToShow.setAddress(address);
            cinemaToShow.setPhone(phone);
            cinemaToShow.setPartner(partner);
            req.setAttribute("cinema", cinemaToShow);
            req.setAttribute("cinemas", cinemaService.getCinemasByPartnerId(partnerId));
            req.getRequestDispatcher("/view/partner/manageCinema.jsp").forward(req, resp);
            return;
        }

        // Kiểm tra số điện thoại hợp lệ
        if (!phone.matches("\\d{1,10}")) {
            req.setAttribute("error", "Số điện thoại không hợp lệ! Chỉ được nhập số và tối đa 10 chữ số.");
            Cinema cinemaToShow = new Cinema();
            cinemaToShow.setId(isNew ? 0 : Long.parseLong(idStr));
            cinemaToShow.setName(name);
            cinemaToShow.setAddress(address);
            cinemaToShow.setPhone(phone);
            cinemaToShow.setPartner(partner);
            req.setAttribute("cinema", cinemaToShow);
            req.setAttribute("cinemas", cinemaService.getCinemasByPartnerId(partnerId));
            req.getRequestDispatcher("/view/partner/manageCinema.jsp").forward(req, resp);
            return;
        }

        // Kiểm tra trùng tên
        Cinema existingCinema = cinemaService.getCinemasByPartnerId(partnerId).stream()
                .filter(c -> c.getName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null);

        if (existingCinema != null && (!isNew && Long.parseLong(idStr) != existingCinema.getId() || isNew)) {
            req.setAttribute("error", "Tên rạp đã tồn tại, vui lòng chọn tên khác!");
            Cinema cinemaToShow = new Cinema();
            cinemaToShow.setId(isNew ? 0 : Long.parseLong(idStr));
            cinemaToShow.setName(name);
            cinemaToShow.setAddress(address);
            cinemaToShow.setPhone(phone);
            cinemaToShow.setPartner(partner);
            req.setAttribute("cinema", cinemaToShow);
            req.setAttribute("cinemas", cinemaService.getCinemasByPartnerId(partnerId));
            req.getRequestDispatcher("/view/partner/manageCinema.jsp").forward(req, resp);
            return;
        }

        try {
            Cinema cinema;
            if (!isNew) {
                // Cập nhật rạp
                long id = Long.parseLong(idStr);
                cinema = cinemaService.findById(id);
                if (cinema != null && cinema.getPartner().getId() == partnerId) {
                    cinema.setName(name.trim());
                    cinema.setAddress(address.trim());
                    cinema.setPhone(phone.trim());
                    cinemaService.updateCinema(cinema);
                    req.setAttribute("message", "Cập nhật rạp thành công!");
                } else {
                    req.setAttribute("error", "Rạp không tồn tại hoặc bạn không có quyền chỉnh sửa!");
                    Cinema cinemaToShow = new Cinema();
                    cinemaToShow.setId(id);
                    cinemaToShow.setName(name);
                    cinemaToShow.setAddress(address);
                    cinemaToShow.setPhone(phone);
                    cinemaToShow.setPartner(partner);
                    req.setAttribute("cinema", cinemaToShow);
                    req.setAttribute("cinemas", cinemaService.getCinemasByPartnerId(partnerId));
                    req.getRequestDispatcher("/view/partner/manageCinema.jsp").forward(req, resp);
                    return;
                }
            } else {
                // Thêm mới rạp
                cinema = new Cinema();
                cinema.setName(name.trim());
                cinema.setAddress(address.trim());
                cinema.setPhone(phone.trim());
                cinema.setPartner(partner);
                cinemaService.addCinema(cinema);
                req.setAttribute("message", "Thêm rạp thành công!");
            }

        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID rạp không hợp lệ!");
            Cinema cinemaToShow = new Cinema();
            cinemaToShow.setId(0);
            cinemaToShow.setName(name);
            cinemaToShow.setAddress(address);
            cinemaToShow.setPhone(phone);
            cinemaToShow.setPartner(partner);
            req.setAttribute("cinema", cinemaToShow);
            req.setAttribute("cinemas", cinemaService.getCinemasByPartnerId(partnerId));
            req.getRequestDispatcher("/view/partner/manageCinema.jsp").forward(req, resp);
            return;
        } catch (Exception e) {
            String errorMessage = (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException)
                    ? "Lưu rạp thất bại: Dữ liệu trùng lặp hoặc không hợp lệ!"
                    : "Lưu rạp thất bại: " + e.getMessage();
            req.setAttribute("error", errorMessage);
            Cinema cinemaToShow = new Cinema();
            cinemaToShow.setId(isNew ? 0 : Long.parseLong(idStr));
            cinemaToShow.setName(name);
            cinemaToShow.setAddress(address);
            cinemaToShow.setPhone(phone);
            cinemaToShow.setPartner(partner);
            req.setAttribute("cinema", cinemaToShow);
            req.setAttribute("cinemas", cinemaService.getCinemasByPartnerId(partnerId));
            req.getRequestDispatcher("/view/partner/manageCinema.jsp").forward(req, resp);
            return;
        }

        // Nếu thành công, giữ session và chuyển hướng
        resp.sendRedirect(req.getContextPath() + "/manageCinema");
    }
}