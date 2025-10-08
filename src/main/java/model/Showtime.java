package model;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import javax.persistence.*;
import model.Movie;
import model.Auditorium;

@Entity
@Table(name = "showtime")
public class Showtime {
    private long id;
    private Movie movie;
    private Auditorium auditorium;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String language;

    public Showtime() {
    }

    public Showtime(long id, Movie movie, Auditorium auditorium, LocalDateTime startTime, LocalDateTime endTime, String language) {
        this.id = id;
        this.movie = movie;
        this.auditorium = auditorium;
        this.startTime = startTime;
        this.endTime = endTime;
        this.language = language;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "movieID")
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @ManyToOne
    @JoinColumn(name = "auditID")
    public Auditorium getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(Auditorium auditorium) {
        this.auditorium = auditorium;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}