package model;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

import model.Auditorium;
import model.enums.SeatType;

@Entity
@Table(name="seat")
public class Seat {
    private int id;
    @Enumerated(EnumType.STRING)
    private SeatType seatType;
    private String rowLabel;
    private String seatNumber;
    private boolean isActive;
    private Auditorium auditorium;
    public Seat() {
    }

    public Seat(int id, SeatType seatType, String rowLabel, String seatNumber, boolean isActive) {
        this.id = id;
        this.seatType = seatType;
        this.rowLabel = rowLabel;
        this.seatNumber = seatNumber;
        this.isActive = isActive;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRowLabel() {
        return rowLabel;
    }

    public void setRowLabel(String rowLabel) {
        this.rowLabel = rowLabel;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    @ManyToOne
    @JoinColumn(name = "auditId")
    public Auditorium getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(Auditorium auditorium) {
        this.auditorium = auditorium;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }


}
