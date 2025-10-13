package model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import model.Cinema;

@Entity
@Table(name = "partner")
@PrimaryKeyJoinColumn(name = "partnerID")
public class Partner extends User {
    private String brand;
    private List<Cinema> cinemas;

    public Partner() {}

    public Partner(String fullName, String email, String phone, String brand) {
        super(fullName, email, phone);
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Cinema> getCinemas() {
        return cinemas;
    }

    public void setCinemas(List<Cinema> cinemas) {
        this.cinemas = cinemas;
    }
}
