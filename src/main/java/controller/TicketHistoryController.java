package controller;

import dao.BookingDAO;
import dao.TicketDAO;
import model.BookingSeat;
import model.Customer;
import service.TicketService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Ticket;

@WebServlet("/ticketHistory")
public class TicketHistoryController extends HttpServlet {

    private TicketService ticketService = new TicketService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ⚙️ Lấy customerId từ session (khi user đã đăng nhập)
        Customer customer = (Customer) req.getSession().getAttribute("user");

        if (customer == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        List<Ticket> tickets = ticketService.getTicketList(customer.getId());
        req.setAttribute("tickets", tickets);
        req.getRequestDispatcher("view/customer/ticketHistory.jsp").forward(req, resp);
    }
}
