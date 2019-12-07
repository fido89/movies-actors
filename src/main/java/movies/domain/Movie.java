package movies.domain;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(name = "Movie")
@Table(name = "movies")
@NamedQueries({
        @NamedQuery(
                name = "Movie.findMovies",
                query = "SELECT m " +
                        "FROM Movie m"
        )
})
public class Movie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @Column(nullable = false, unique = true)
    private String imdbId;

    @Column(name = "title")
    private String title;

    @Column(name = "year")
    private int year;

    @Column(name = "description")
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movies_pictures", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "picture_url")
    private List<String> pictureUrls;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(List<String> pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Movie movie = (Movie) o;
        return Objects.equals(imdbId, movie.imdbId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imdbId);
    }
}
