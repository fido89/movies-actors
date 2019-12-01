package movies;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "movies")
@NamedQueries({
        @NamedQuery(
                name = "Movies.findMovies",
                query = "SELECT m " +
                        "FROM Movies m"
        )
})
public class Movies implements Serializable {
    @Id
    private String imdbId;
    @Column(name = "title")
    private String title;
    @Column(name = "year")
    private int year;
    @Column(name = "description")
    private String description;

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
}
