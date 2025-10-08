package model;

import model.enums.SeatBookedFormat;
import model.enums.SeatType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookingseat")
public class BookingSeat {
    private long id;
    private Auditorium auditorium;
    private Seat seat;
    private Showtime showtime;
    @Enumerated(EnumType.STRING)
    private SeatBookedFormat status;
    private LocalDateTime createdAt;

    public BookingSeat() {}
    public BookingSeat(Auditorium auditorium, Seat seat, Showtime showtime, SeatBookedFormat status, LocalDateTime createdAt) {
        this.auditorium = auditorium;
        this.seat = seat;
        this.showtime = showtime;
        this.status = status;
        this.createdAt = createdAt;
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
    @JoinColumn(name = "auditID")
    public Auditorium getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(Auditorium auditorium) {
        this.auditorium = auditorium;
    }

    @ManyToOne
    @JoinColumn(name = "seatID")
    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    @ManyToOne
    @JoinColumn(name = "showtimeID")
    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    public SeatBookedFormat getStatus() {
        return status;
    }

    public void setStatus(SeatBookedFormat status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
