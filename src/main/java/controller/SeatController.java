package controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Auditorium;
import model.Seat;
import model.enums.SeatType;
import service.AuditoriumService;
import service.SeatService;

@WebServlet("/SeatController")
public class SeatController extends HttpServlet {
    private SeatService seatService = new SeatService();
    private AuditoriumService auditoriumService = new AuditoriumService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listSeats(req, resp);
                break;
            case "add":
                showAddForm(req, resp);
                break;
            case "edit":
                showEditForm(req, resp);
                break;
            case "delete":
                deleteSeat(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/view/partner/SeatList.jsp");
        }
    }

    private void listSeats(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long auditoriumId = Long.parseLong(req.getParameter("auditoriumId"));
        List<Seat> seats = seatService.getSeatsByAu(auditoriumId);
        Auditorium auditorium = auditoriumService.findById(auditoriumId);
        req.setAttribute("seats", seats);
        req.setAttribute("auditorium", auditorium);
        RequestDispatcher rd = req.getRequestDispatcher("/view/partner/SeatList.jsp");
        rd.forward(req, resp);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long auditoriumId = Long.parseLong(req.getParameter("auditoriumId"));
        Auditorium auditorium = auditoriumService.findById(auditoriumId);
        req.setAttribute("auditorium", auditorium);
        RequestDispatcher rd = req.getRequestDispatcher("/view/partner/SeatForm.jsp");
        rd.forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int seatId = Integer.parseInt(req.getParameter("seatId"));
        long auditoriumId = Long.parseLong(req.getParameter("auditoriumId"));
        Seat seat = seatService.getSeats(seatId);
        Auditorium auditorium = auditoriumService.findById(auditoriumId);
        req.setAttribute("seat", seat);
        req.setAttribute("auditorium", auditorium);
        RequestDispatcher rd = req.getRequestDispatcher("/view/partner/SeatForm.jsp");
        rd.forward(req, resp);
    }

    private void deleteSeat(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int seatId = Integer.parseInt(req.getParameter("seatId"));
        long auditoriumId = Long.parseLong(req.getParameter("auditoriumId"));
        seatService.delete(seatId);
        resp.sendRedirect(req.getContextPath() + "/SeatController?action=list&auditoriumId=" + auditoriumId);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        if ("save".equals(action)) {
            long auditoriumId = Long.parseLong(req.getParameter("auditoriumId"));
            String rowLabel = req.getParameter("rowLabel");
            String seatNumber = req.getParameter("seatNumber");
            String seatType = req.getParameter("seatType");
            boolean isActive = Boolean.parseBoolean(req.getParameter("isActive"));
            Auditorium auditorium = auditoriumService.findById(auditoriumId);

            String seatIdStr = req.getParameter("seatId");
            boolean isNew = (seatIdStr == null || seatIdStr.isEmpty());

            String errorMessage = null;
            if (isNew) {
                boolean exists = seatService.seatExists(auditoriumId, rowLabel, seatNumber);
                if (exists) {
                    errorMessage = "Không thể thêm ghế vì vị trí ghế đã tồn tại.";
                }
            }

            if (errorMessage != null) {
                req.setAttribute("auditorium", auditorium);
                req.setAttribute("error", errorMessage);
                RequestDispatcher rd = req.getRequestDispatcher("/view/partner/SeatForm.jsp");
                rd.forward(req, resp);
                return;
            }

            Seat seat;
            String seatId = req.getParameter("seatId");
            if (!isNew) {
                seat = seatService.getSeats(Integer.parseInt(seatIdStr));
            } else {
                seat = new Seat();
                seat.setAuditorium(auditorium);
            }
            seat.setRowLabel(rowLabel);
            seat.setSeatNumber(seatNumber);
            seat.setSeatType(SeatType.valueOf(seatType));
            seat.setActive(true);

            if (!isNew) {
                seatService.update(seat);
            } else {
                seatService.save(seat);
            }
            resp.sendRedirect(req.getContextPath() + "/SeatController?action=list&auditoriumId=" + auditoriumId);
        }
    }
}