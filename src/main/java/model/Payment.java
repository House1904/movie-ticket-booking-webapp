package model;

import java.time.LocalDateTime;
import javax.persistence.*;
import model.Booking;
import model.enums.PaymentStatus;

@Entity
@Table(name = "payment")
public class Payment {
    private long id;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private Booking booking;
    private LocalDateTime createAt;
    private LocalDateTime paidAt;
    private LocalDateTime expiredAt;


    public Payment() {
    }

    public Payment(long id, PaymentStatus status, Booking booking, LocalDateTime createAt, LocalDateTime paidAt, LocalDateTime expiredAt) {
        this.id = id;
        this.status = status;
        this.booking = booking;
        this.createAt = createAt;
        this.paidAt = paidAt;
        this.expiredAt = expiredAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "bookingID")
    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
