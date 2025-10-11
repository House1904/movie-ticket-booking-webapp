package model;

import javax.persistence.*;

@Entity
@Table(name = "seatpricing")
public class seatPricing {
    private long id;
    private String auditType;
    private String cinemaBrand;
    private String seatType;
    private double price;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getCinemaBrand() {
        return cinemaBrand;
    }

    public void setCinemaBrand(String cinemaBrand) {
        this.cinemaBrand = cinemaBrand;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
