package model;

import javax.persistence.*;
import java.util.List;
import java.io.Serializable;
import java.time.LocalDateTime;
import model.Seat;
import model.Cinema;
import model.enums.AuditFormat;

@Entity
@Table(name="auditorium")
public class Auditorium implements Serializable {
    private long id;
    private String name;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private AuditFormat format;
    private List<Seat> seats;
    private Cinema cinema;
    public Auditorium() {
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public Auditorium(long id, String name, LocalDateTime createdAt, AuditFormat format, List<Seat> seats) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.format = format;
        this.seats = seats;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @ManyToOne
    @JoinColumn(name = "cinemaId")
    public Cinema getCinema() {
        return cinema;
    }

    @OneToMany(mappedBy = "auditorium", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public AuditFormat getFormat() {
        return format;
    }

    public void setFormat(AuditFormat format) {
        this.format = format;
    }

    public int quantity() {
        return seats.size();
    }
}
