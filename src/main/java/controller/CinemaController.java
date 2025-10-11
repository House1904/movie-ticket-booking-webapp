package controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Cinema;
import service.CinemaService;

@WebServlet("/CinemaController")
public class CinemaController extends HttpServlet {
    private CinemaService cinemaService = new CinemaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listCinemas(req, resp);
                break;
            case "getByPartner":
                getCinemasByPartner(req, resp);
                break;
            case "edit":
                showEditForm(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/view/partner/CinemaList.jsp");
        }
    }

    private void getCinemasByPartner(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            long partnerId = Long.parseLong(req.getParameter("partnerId"));
            List<Cinema> cinemas = cinemaService.getCinemasByPartnerId(partnerId);
            req.setAttribute("cinemas", cinemas);
            req.setAttribute("partnerId", partnerId); // Để hiển thị hoặc sử dụng trong JSP
            RequestDispatcher rd = req.getRequestDispatcher("/view/partner/CinemaList.jsp");
            rd.forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "partnerId không hợp lệ");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi lấy danh sách rạp");
        }
    }

    private void listCinemas(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Cinema> cinemas = cinemaService.getAllCinemas();
        req.setAttribute("cinemas", cinemas);
        RequestDispatcher rd = req.getRequestDispatcher("/view/partner/CinemaList.jsp");
        rd.forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            long id = Long.parseLong(req.getParameter("id"));
            Cinema cinema = cinemaService.findById(id);
            if (cinema == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Rạp không tồn tại");
                return;
            }
            req.setAttribute("cinema", cinema);
            RequestDispatcher rd = req.getRequestDispatcher("/view/partner/CinemaInfo.jsp");
            rd.forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID rạp không hợp lệ");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        if ("update".equals(action)) {
            try {
                long id = Long.parseLong(req.getParameter("id"));
                String address = req.getParameter("address");
                String phone = req.getParameter("phone");

                Cinema cinema = cinemaService.findById(id);
                if (cinema == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Rạp không tồn tại");return;
                }
                cinema.setAddress(address);
                cinema.setPhone(phone);

                cinemaService.update(cinema);
                resp.sendRedirect(req.getContextPath() + "/CinemaController?action=edit&id=" + id);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID rạp không hợp lệ");
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi cập nhật rạp");
            }
        }
    }
}