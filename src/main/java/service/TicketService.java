package service;

import dao.TicketDAO;
import model.Movie;
import model.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketService {
    private TicketDAO ticketDAO = new TicketDAO();
    private Map<Long, Integer> ticketsSoldCache = new HashMap<>();
    private Map<Long, Double> revenueCache = new HashMap<>();
    private Map<Long, Double> revenueByCinemaCache = new HashMap<>();

    public List<Ticket> getTicketList(long customerID){
        return ticketDAO.getTicketByCustomer(customerID);
    }

    public double getTotalRevenue(String dateRange, List<Long> cinemaIds) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = switch (dateRange) {
            case "week" -> endDate.minusDays(7);
            case "month" -> endDate.minusMonths(1);
            default -> endDate; // today
        };
        return ticketDAO.getTotalRevenue(startDate.atStartOfDay(), endDate.atTime(23, 59, 59), cinemaIds);
    }

    public int getTotalTickets(String dateRange, List<Long> cinemaIds) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = switch (dateRange) {
            case "week" -> endDate.minusDays(7);
            case "month" -> endDate.minusMonths(1);
            default -> endDate; // today
        };
        return ticketDAO.getTotalTickets(startDate.atStartOfDay(), endDate.atTime(23, 59, 59), cinemaIds);
    }

    public List<Map<String, Object>> getRevenueByDay(String dateRange, List<Long> cinemaIds) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = switch (dateRange) {
            case "week" -> endDate.minusDays(7);
            case "month" -> endDate.minusMonths(1);
            default -> endDate; // today
        };
        return ticketDAO.getRevenueByDay(startDate.atStartOfDay(), endDate.atTime(23, 59, 59), cinemaIds);
    }

    public List<Map<String, Object>> getRevenueByCinema(String dateRange, List<Long> cinemaIds) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = switch (dateRange) {
            case "week" -> endDate.minusDays(7);
            case "month" -> endDate.minusMonths(1);
            default -> endDate; // today
        };
        List<Map<String, Object>> revenueByCinema = ticketDAO.getRevenueByCinema(startDate.atStartOfDay(), endDate.atTime(23, 59, 59), cinemaIds);
        for (Map<String, Object> cinemaData : revenueByCinema) {
            Long cinemaId = (Long) cinemaData.get("cinemaId");
            Double revenue = (Double) cinemaData.get("revenue");
            revenueByCinemaCache.put(cinemaId, revenue);
        }
        return revenueByCinema;
    }



    public List<Movie> getTopMovies(int limit, String dateRange, List<Long> cinemaIds) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = switch (dateRange) {
            case "week" -> endDate.minusDays(7);
            case "month" -> endDate.minusMonths(1);
            default -> endDate; // today
        };
        List<Movie> topMovies = ticketDAO.getTopMovies(limit, startDate.atStartOfDay(), endDate.atTime(23, 59, 59), cinemaIds);
        for (Movie movie : topMovies) {
            ticketsSoldCache.put(movie.getId(), getTicketsSoldForMovie(movie.getId(), dateRange, cinemaIds));
            revenueCache.put(movie.getId(), getRevenueForMovie(movie.getId(), dateRange, cinemaIds));
        }
        return topMovies;
    }

    public int getTicketsSoldForMovie(long movieId, String dateRange, List<Long> cinemaIds) {
        if (ticketsSoldCache.containsKey(movieId)) {
            return ticketsSoldCache.get(movieId);
        }
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = switch (dateRange) {
            case "week" -> endDate.minusDays(7);
            case "month" -> endDate.minusMonths(1);
            default -> endDate; // today
        };
        int tickets = ticketDAO.getTicketsSoldForMovie(movieId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59), cinemaIds);
        ticketsSoldCache.put(movieId, tickets);
        return tickets;
    }


    public double getRevenueForMovie(long movieId, String dateRange, List<Long> cinemaIds) {
        if (revenueCache.containsKey(movieId)) {
            return revenueCache.get(movieId);
        }
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = switch (dateRange) {
            case "week" -> endDate.minusDays(7);
            case "month" -> endDate.minusMonths(1);
            default -> endDate; // today
        };
        double revenue = ticketDAO.getRevenueForMovie(movieId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59), cinemaIds);
        revenueCache.put(movieId, revenue);
        return revenue;
    }

}