package controller;

import model.Customer;
import model.Ticket;
import model.enums.Status;
import service.TicketService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@WebServlet("/tickets")
public class TicketController extends HttpServlet {
    private TicketService ticketService = new TicketService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/auth?action=login");
            return;
        }

        Customer user = (Customer) session.getAttribute("user");
        List<Ticket> tickets = ticketService.getTicketsByCustomer(user.getId());
        req.setAttribute("tickets", tickets);
        req.getRequestDispatcher("/view/customer/mytickets.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        long ticketId = Long.parseLong(req.getParameter("ticketId"));
        Ticket ticket = ticketService.getTicket(ticketId);
        if (ticket == null) {
            resp.sendRedirect(req.getContextPath() + "/tickets");
            return;
        }

        switch (action) {
            case "issue":
                ticket.setQrCode(UUID.randomUUID().toString());
                ticket.setIssuedAt(LocalDateTime.now());
                ticket.setStatus(Status.ISSUED);
                ticketService.updateTicket(ticket);
                break;
            case "check":
                ticket.setCheckedAt(LocalDateTime.now());
                ticket.setStatus(Status.USED);
                ticketService.updateTicket(ticket);
                break;
        }
        resp.sendRedirect(req.getContextPath() + "/tickets");
    }
}
