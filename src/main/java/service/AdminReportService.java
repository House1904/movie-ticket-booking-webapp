package service;

import dao.AdminReportDAO;

import java.time.LocalDate;
import java.util.*;

public class AdminReportService {
    private final AdminReportDAO adminReportDAO = new AdminReportDAO();

    public Map<String, Double> getTodayRevenueByPartner() {
        List<Map<String, Object>> data = adminReportDAO.getTodayPartnerRevenue();
        Map<String, Double> result = new LinkedHashMap<>();

        for (Map<String, Object> item : data) {
            String partnerName = (String) item.get("partnerName");
            Double revenue = (Double) item.get("revenue");
            result.put(partnerName, revenue);
        }
        return result;
    }

    public double totalRevenueByDate(String date) {
        LocalDate today = LocalDate.parse(date);
        return adminReportDAO.getTotalRevenueByDate(today);
    }

    public List<Object[]> getTop3PartnersToday() {
        return adminReportDAO.getTop3PartnersToday();
    }

    public List<Object[]> getTop5MoviesToday() {
        return adminReportDAO.getTop5MoviesToday();
    }
}
