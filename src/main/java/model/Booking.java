package model;

import model.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "booking")
public class Booking {
    private long id;
    private List<Promotion> promotions;
    private List<Ticket> tickets;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Payment payment;
    private Customer customer;

    public Booking() {}
    public Booking(long id, List<Promotion> promotions, List<Ticket> tickets, Status status) {
        this.id = id;
        this.promotions = promotions;
        this.tickets = tickets;
        this.status = status;
    }
    public Booking(Status status, Customer customer) {
        this.status = status;
        this.customer = customer;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    @ManyToMany
    @JoinTable(
            name = "bookingPromotion",
            joinColumns = @JoinColumn(name = "bookingID"),
            inverseJoinColumns = @JoinColumn(name = "promotionID")
    )
    public List<Promotion> getPromotions() {
        return promotions;
    }

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @OneToOne(mappedBy = "booking",cascade = CascadeType.ALL, orphanRemoval = true)
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "customerID")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double totalPrice()
    {
        double total = 0;
        for (Ticket ticket : tickets){
            total += ticket.getPrice();
        }
        return total;
    }
}