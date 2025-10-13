package model;
import java.time.LocalDateTime;
import java.util.List;
import java.time.ZoneId;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name = "rating")
public class Rating {
    private int id;
    private Customer customer;
    private Movie movie;
    private int rating;
    private String content;
    private LocalDateTime created_at;

    public Rating() {
    }

    public Rating(int id, Customer customer, Movie movie, int rating, String content, LocalDateTime created_at) {
        this.id = id;
        this.customer = customer;
        this.movie = movie;
        this.rating = rating;
        this.content = content;
        this.created_at = created_at;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "customerID")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @ManyToOne
    @JoinColumn(name = "movieID")
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Transient
    public java.util.Date getCreatedAtDate() {
        return java.util.Date.from(created_at.atZone(ZoneId.systemDefault()).toInstant());
    }

}
