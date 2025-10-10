package model;
import model.enums.PromotionStatus;
import model.enums.PromotionType;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "promotion")
public class Promotion {
    private long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private PromotionType promotionType;
    private double discountValue;
    private double minTotalPrice;
    private double maxTotalPrice;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    @Enumerated(EnumType.STRING)
    private PromotionStatus status;
    private List<Booking> bookings;

    public Promotion() {
    }

    public Promotion(long id, String name, PromotionType promotionType, double discountValue, double minTotalPrice, double maxTotalPrice, LocalDateTime startAt, LocalDateTime endAt, PromotionStatus status, List<Booking> bookings) {
        this.id = id;
        this.name = name;
        this.promotionType = promotionType;
        this.discountValue = discountValue;
        this.minTotalPrice = minTotalPrice;
        this.maxTotalPrice = maxTotalPrice;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.bookings = bookings;
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


    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public double getMinTotalPrice() {
        return minTotalPrice;
    }

    public void setMinTotalPrice(double minTotalPrice) {
        this.minTotalPrice = minTotalPrice;
    }

    public double getMaxTotalPrice() {
        return maxTotalPrice;
    }

    public void setMaxTotalPrice(double maxTotalPrice) {
        this.maxTotalPrice = maxTotalPrice;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    @ManyToMany(mappedBy = "promotions")
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(PromotionType promotionType) {
        this.promotionType = promotionType;
    }

    public PromotionStatus getStatus() {
        return status;
    }

    public void setStatus(PromotionStatus status) {
        this.status = status;
    }

}
