package model;
import javax.persistence.*;

@Entity
@Table(name = "Admin")
@PrimaryKeyJoinColumn(name = "adminID")
public class Admin extends User{
    public Admin() {
    }
    public Admin(long id, String fullName, String email, String phone) {
        super(id, fullName, email, phone);
    }
}
