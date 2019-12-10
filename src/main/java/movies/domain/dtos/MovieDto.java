package movies.domain.dtos;

import movies.domain.Actor;
import movies.domain.Movie;

import java.util.Set;
import java.util.stream.Collectors;

public class MovieDto {
    private Long id;
    private String imdbId;
    private String title;
    private int year;
    private String description;
    private Set<String> pictureUrls;
    private Set<ActorDto> actors;

    public MovieDto() {
    }

    public MovieDto(Long id, String imdbId, String title, int year, String description, Set<String> pictureUrls, Set<ActorDto> actors) {
        this.id = id;
        this.imdbId = imdbId;
        this.title = title;
        this.year = year;
        this.description = description;
        this.pictureUrls = pictureUrls;
        this.actors = actors;
    }

    public Movie toMovie() {
        Set<Actor> actorsToSet = null;
        if (actors != null) {
            actorsToSet = actors.stream().map(ActorDto::toActor).collect(Collectors.toSet());
        }
        return new Movie(imdbId, title, year, description, pictureUrls, actorsToSet);
    }

    public Long getId() {
        return id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getPictureUrls() {
        return pictureUrls;
    }

    public Set<ActorDto> getActors() {
        return actors;
    }
}