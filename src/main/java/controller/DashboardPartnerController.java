package controller;

import model.Cinema;
import model.Movie;
import model.Partner;
import service.CinemaService;
import service.TicketService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/dashboard")
public class DashboardPartnerController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DashboardPartnerController.class.getName());
    private CinemaService cinemaService = new CinemaService();
    private TicketService ticketService = new TicketService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession();
            Object user = session.getAttribute("user");

            if (user == null) {
                resp.sendRedirect(req.getContextPath() + "/auth?action=login&error=unauthorized");
                return;
            }

            Partner partner = (Partner) user;

            List<Long> cinemaIds = partner.getCinemas().stream().map(Cinema::getId).collect(Collectors.toList());
            if (cinemaIds.isEmpty()) {
                req.setAttribute("error", "Đối tác chưa có rạp nào được liên kết.");
                req.getRequestDispatcher("/view/partner/dashboard.jsp").forward(req, resp);
                return;
            }

            String dateRange = req.getParameter("dateRange");
            if (dateRange == null || !List.of("today", "week", "month").contains(dateRange)) {
                dateRange = "today";
            }

            String cinemaIdParam = req.getParameter("cinemaId");
            List<Long> filteredCinemaIds = cinemaIds; // Mặc định tất cả
            if (cinemaIdParam != null && !cinemaIdParam.isEmpty()) {
                try {
                    Long cinemaId = Long.parseLong(cinemaIdParam);
                    if (cinemaIds.contains(cinemaId)) {
                        filteredCinemaIds = List.of(cinemaId);
                    }
                } catch (NumberFormatException e) {}
            }


            double totalRevenue = ticketService.getTotalRevenue(dateRange, filteredCinemaIds);
            int totalTickets = ticketService.getTotalTickets(dateRange, filteredCinemaIds);
            List<Cinema> cinemas = partner.getCinemas();
            List<Map<String, Object>> revenueByDay = ticketService.getRevenueByDay(dateRange, filteredCinemaIds);
            List<Movie> topMovies = ticketService.getTopMovies(5, dateRange, filteredCinemaIds);
            List<Map<String, Object>> revenueByCinema = ticketService.getRevenueByCinema(dateRange, filteredCinemaIds);
            req.setAttribute("revenueByCinema", revenueByCinema);


            for (Movie movie : topMovies) {
                movie.setTicketService(ticketService);
                movie.setCinemaIds(filteredCinemaIds);
            }


            session.setAttribute("cinemas", cinemas);
            req.setAttribute("totalRevenue", totalRevenue);
            req.setAttribute("totalTickets", totalTickets);
            req.setAttribute("revenueByDay", revenueByDay);
            req.setAttribute("topMovies", topMovies);
            req.setAttribute("dateRange", dateRange);
            req.setAttribute("selectedCinemaId", cinemaIdParam);


            RequestDispatcher rd = req.getRequestDispatcher("/view/partner/dashboard.jsp");
            rd.forward(req, resp);
        } catch (Exception e) {
            LOGGER.severe("Error loading dashboard: " + e.getMessage());
            throw new ServletException("Lỗi khi tải dashboard", e);
        }
    }
}