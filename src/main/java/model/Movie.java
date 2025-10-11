package model;


import java.util.List;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "movie")
public class Movie {
    private long id;
    private String title;
    private String description;
    private List<String> genre;
    private long duration;
    private String ageLimit;
    private LocalDateTime releaseDate;
    private String language;
    private String posterUrl;
    private String trailerUrl;
    private String actor;
    private List<Showtime> showtimes;
    private List<Rating> ratings;
    public Movie() {
    }

    public Movie(long id, String title, String description, List<String> genre, long duration, String ageLimit, LocalDateTime releaseDate, String language, String posterUrl, String trailerUrl, String actor, List<Showtime> showtimes, List<Rating> ratings) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.duration = duration;
        this.ageLimit = ageLimit;
        this.releaseDate = releaseDate;
        this.language = language;
        this.posterUrl = posterUrl;
        this.trailerUrl = trailerUrl;
        this.actor = actor;
        this.showtimes = showtimes;
        this.ratings = ratings;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ElementCollection (fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_genre", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "genre")
    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(String ageLimit) {
        this.ageLimit = ageLimit;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    @OneToMany(mappedBy = "movie",  cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}
