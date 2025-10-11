package service;

import dao.CinemaDAO;
import dao.ShowtimeDAO;
import model.Cinema;
import model.Showtime;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ShowtimeService {
    private ShowtimeDAO showtimeDAO = new ShowtimeDAO();
    public List<Showtime> getShowtimes() {
        return showtimeDAO.getshowtimeList();
    }
    public List<Showtime> getShowtimesByPartner(long partnerId) {
        return showtimeDAO.getShowtimesByPartner(partnerId);
    }

    public Showtime getShowtime(long id) {
        return showtimeDAO.showtime(id);
    }
    public List<Showtime> getShowtimesByC(long id, LocalDate date) throws SQLException {
        return showtimeDAO.getShowtimesByCinema(id, date);
    }
    public void addShowtime(Showtime showtime) {
        showtimeDAO.addShowtime(showtime);
    }

    public void updateShowtime(Showtime showtime) {
        showtimeDAO.updateShowtime(showtime);
    }

    public void deleteShowtime(long id) {
        showtimeDAO.deleteShowtime(id);
    }

    public List<Showtime> getShowtimesByAuditorium(long auditoriumId) {
        return showtimeDAO.getShowtimesByAuditorium(auditoriumId);
    }

}
