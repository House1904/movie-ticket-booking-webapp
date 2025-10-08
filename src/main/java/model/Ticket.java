package model;
import java.time.LocalDateTime;
import javax.persistence.*;
import model.Showtime;
import model.Seat;
import model.enums.Status;

@Entity
@Table(name = "ticket")
public class Ticket {
    private long id;
    private Showtime showtime;
    private Seat seat;
    private double price;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String qrCode;
    private LocalDateTime createdAt;
    private LocalDateTime issuedAt;
    private LocalDateTime checkedAt;
    private Booking booking;

    public Ticket() {}

    public Ticket(long id, Showtime showtime, Seat seat, double price, Status status, String qrCode, LocalDateTime createdAt, LocalDateTime issuedAt, LocalDateTime checkedAt, Booking booking) {
        this.id = id;
        this.showtime = showtime;
        this.seat = seat;
        this.price = price;
        this.status = status;
        this.qrCode = qrCode;
        this.createdAt = createdAt;
        this.issuedAt = issuedAt;
        this.checkedAt = checkedAt;
        this.booking = booking;
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
    @JoinColumn(name = "showtimeID")
    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    @ManyToOne
    @JoinColumn(name = "seatID")
    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(LocalDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }

    @ManyToOne
    @JoinColumn(name = "bookingID")
    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
