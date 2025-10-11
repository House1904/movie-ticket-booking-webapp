package model;
import javax.persistence.*;

@Entity
@Table(name = "Admin")
@PrimaryKeyJoinColumn(name = "adminID")
public class Admin extends User {
    public Admin() {
    }
    public Admin(String fullName, String email, String phone) {
        super(fullName, email, phone);
    }
}
