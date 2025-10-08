package model;
import model.Auditorium;

import model.Partner;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name="cinema")
public class Cinema implements Serializable {
    private long id;
    private String name;
    private String address;
    private String phone;
    private LocalDateTime createAt;
    private List<Auditorium> auditoriums;
    private Partner partner;
    public Cinema() {
    }

    public Cinema(long id, String name, String address, String phone, LocalDateTime createAt, List<Auditorium> auditoriums, Partner partner) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.createAt = createAt;
        this.auditoriums = auditoriums;
        this.partner = partner;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Auditorium> getAuditoriums() {
        return auditoriums;
    }

    public void setAuditoriums(List<Auditorium> auditoriums) {
        this.auditoriums = auditoriums;
    }

    @ManyToOne
    @JoinColumn(name = "partnerID")
    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }
}
