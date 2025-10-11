package model;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "banner")
public class Banner {
    protected String title;
    protected String image_url;
    protected String link_url;
    protected LocalDateTime start_at;
    protected LocalDateTime end_at;
    protected LocalDateTime created_at;

    public Banner() {}

    public Banner(String title, String image_url, String link_url, LocalDateTime start_at, LocalDateTime end_at, LocalDateTime created_at) {
        this.title = title;
        this.image_url = image_url;
        this.link_url = link_url;
        this.start_at = start_at;
        this.end_at = end_at;
        this.created_at = created_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Id
    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public LocalDateTime getStart_at() {
        return start_at;
    }

    public void setStart_at(LocalDateTime start_at) {
        this.start_at = start_at;
    }

    public LocalDateTime getEnd_at() {
        return end_at;
    }

    public void setEnd_at(LocalDateTime end_at) {
        this.end_at = end_at;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

}
