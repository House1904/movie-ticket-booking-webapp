package controller;

import service.AdminReportService;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@WebServlet("/admin")
public class AdminController extends HttpServlet {
    private final AdminReportService adminReportService = new AdminReportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Map<String, Double> revenueMap = adminReportService.getTodayRevenueByPartner();
        req.setAttribute("revenueMap", revenueMap);

        String date = req.getParameter("date");
        Double totalRevenue = adminReportService.totalRevenueByDate(LocalDate.now().toString());
        req.setAttribute("totalRevenue", totalRevenue);

        List<Object[]> topPartners = adminReportService.getTop3PartnersToday();
        req.setAttribute("topPartners", topPartners);

        List<Object[]> topMovies = adminReportService.getTop5MoviesToday();
        req.setAttribute("topMovies", topMovies);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/view/admin/dashboard.jsp");
        dispatcher.forward(req, resp);
    }
}
