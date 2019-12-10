package movies.domain;

import movies.domain.dtos.ActorDto;
import movies.domain.dtos.MovieDto;
import org.hibernate.annotations.NaturalId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "Movie")
@Table(name = "movies")
@Indexed
public class Movie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @Column(nullable = false, unique = true)
    private String imdbId;

    @Column(name = "title")
    @Field
    private String title;

    @Column(name = "year")
    private int year;

    @Column(name = "description")
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movies_pictures", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "picture_url")
    private Set<String> pictureUrls;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    },
            fetch = FetchType.EAGER)
    @JoinTable(name = "movies_actors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    @IndexedEmbedded
    private Set<Actor> actors = new HashSet<>();

    public Movie() {
    }

    public Movie(String imdbId, String title, int year, String description, Set<String> pictureUrls, Set<Actor> actors) {
        this.imdbId = imdbId;
        this.title = title;
        this.year = year;
        this.description = description;
        this.pictureUrls = pictureUrls;
        this.actors = actors;
    }

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

    public Set<String> getPictureUrls() {
        return pictureUrls;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public void addActor(Actor actor) {
        actors.add(actor);
        actor.getMovies().add(this);
    }

    public void removeActor(Actor actor) {
        actors.remove(actor);
        actor.getMovies().remove(this);
    }

    public void setPictureUrls(Set<String> pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
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

    public MovieDto toDto() {
        Set<ActorDto> actorDtos = actors.stream().map(Actor::toDto).collect(Collectors.toSet());
        return new MovieDto(id, imdbId, title, year, description, pictureUrls, actorDtos);
    }

}
