package model;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "customer")
@PrimaryKeyJoinColumn(name = "customerID")
public class Customer extends User {
    private boolean isMemberShip;
    private LocalDate dateOfBirth;
    private String avatarUrl;
    private List<Rating> ratings;
    private List<Booking> bookings;

    public Customer() {}

    public Customer(String fullName, String email, String phone, LocalDate dateOfBirth, String avatarUrl) {
        super(fullName, email, phone);
        this.dateOfBirth = dateOfBirth;
        this.avatarUrl = avatarUrl;
        this.isMemberShip= true;
    }

    public boolean isMemberShip() {
        return isMemberShip;
    }

    public void setMemberShip(boolean memberShip) {
        isMemberShip = memberShip;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    @OneToMany(mappedBy = "customer",  cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
