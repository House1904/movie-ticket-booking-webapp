package model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "article")
public class Article {
    @Column(length = 1024)
    private String title;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
    private String url;
    private LocalDateTime created_at;
    @Column(length = 2048)
    private String image;

    public Article() {
    }

    public Article(String title, String content, String url, LocalDateTime created_at) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.created_at = created_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Id
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}