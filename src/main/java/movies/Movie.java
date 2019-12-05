package movies;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
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
    private String imdbId;
    @Column(name = "title")
    private String title;
    @Column(name = "year")
    private int year;
    @Column(name = "description")
    private String description;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movies_pictures", joinColumns = @JoinColumn(name = "movie_imdbID"))
    @Column(name = "picture_url")
    private List<String> pictureUrls;

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
}
