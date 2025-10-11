package controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import model.Auditorium;
import model.Cinema;
import model.enums.AuditFormat;
import service.AuditoriumService;
import service.CinemaService;
import service.MovieService;
import service.ShowtimeService;


@WebServlet("/AuditoriumController")
public class AuditoriumController extends HttpServlet {
    private AuditoriumService auditoriumService = new AuditoriumService();
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
                listAuditoriums(req, resp);
                break;
            case "add":
                showAddForm(req, resp);
                break;
            case "edit":
                showEditForm(req, resp);
                break;
            case "delete":
                deleteAuditorium(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/view/partner/AuditoriumList.jsp");
        }
    }

    private void listAuditoriums(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long cinemaId = Long.parseLong(req.getParameter("cinemaId"));
        List<Auditorium> auditoriums = auditoriumService.getByCinemaId(cinemaId);
        Cinema cinema = cinemaService.findById(cinemaId);
        if (cinema != null && auditoriums != null) {
            req.setAttribute("auditoriums", auditoriums);
            req.setAttribute("cinema", cinema);
        }
        RequestDispatcher rd = req.getRequestDispatcher("/view/partner/AuditoriumList.jsp");
        rd.forward(req, resp);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long cinemaId = Long.parseLong(req.getParameter("cinemaId"));
        Cinema cinema = cinemaService.findById(cinemaId);
        req.setAttribute("cinema", cinema);
        RequestDispatcher rd = req.getRequestDispatcher("/view/partner/AuditoriumForm.jsp");
        rd.forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));
        long cinemaId = Long.parseLong(req.getParameter("cinemaId"));
        Auditorium auditorium = auditoriumService.findById(id);
        Cinema cinema = cinemaService.findById(cinemaId);
        req.setAttribute("auditorium", auditorium);
        req.setAttribute("cinema", cinema);
        RequestDispatcher rd = req.getRequestDispatcher("/view/partner/AuditoriumForm.jsp");
        rd.forward(req, resp);
    }

    private void deleteAuditorium(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        long id = Long.parseLong(req.getParameter("id"));
        long cinemaId = Long.parseLong(req.getParameter("cinemaId"));
        auditoriumService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/AuditoriumController?action=list&cinemaId=" + cinemaId);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        if ("save".equals(action)) {
            long cinemaId = Long.parseLong(req.getParameter("cinemaId"));
            String name = req.getParameter("name");
            String format = req.getParameter("format");
            String idStr = req.getParameter("id"); // thêm dòng này
            boolean isNew = (idStr == null || idStr.isEmpty());
            Cinema cinema = cinemaService.findById(cinemaId);

            String errorMessage = null;
            if (isNew) {
                boolean nameExists = auditoriumService.nameExists(cinemaId, name);
                if (nameExists) {
                    errorMessage = "Không thể thêm phòng chiếu vì tên phòng đã tồn tại.";
                }
            }

            if (errorMessage != null) {
                req.setAttribute("cinema", cinema);
                req.setAttribute("error", errorMessage);
                req.setAttribute("name", name); // Preserve name in case of error
                req.setAttribute("format", format); // Preserve format in case of error
                RequestDispatcher rd = req.getRequestDispatcher("/view/partner/AuditoriumForm.jsp");
                rd.forward(req, resp);
                return;
            }

            Auditorium auditorium;
            if (!isNew) {
                auditorium = auditoriumService.findById(Long.parseLong(idStr));
            } else {
                auditorium = new Auditorium();
                auditorium.setCinema(cinema);
                auditorium.setCreatedAt(LocalDateTime.now());
            }

            auditorium.setName(name);
            AuditFormat enumFormat = AuditFormat.valueOf(format);
            auditorium.setFormat(enumFormat);

            if (!isNew) {
                auditoriumService.update(auditorium);
            } else {
                auditoriumService.save(auditorium);
            }
            resp.sendRedirect(req.getContextPath() + "/AuditoriumController?action=list&cinemaId=" + cinemaId);
        }
    }
}