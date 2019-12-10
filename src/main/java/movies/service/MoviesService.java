package movies.service;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import movies.domain.Actor;
import movies.domain.Movie;
import movies.domain.dtos.ActorDto;
import movies.domain.dtos.MovieDto;
import org.hibernate.Session;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestScoped
public class MoviesService {

    @PersistenceContext
    private EntityManager em;

    public List<MovieDto> getMovies(QueryParameters query) {
        List<Movie> movies = JPAUtils.queryEntities(em, Movie.class, query);
        return movies.stream().map(Movie::toDto).collect(Collectors.toList());
    }

    public Long getMoviesCount(QueryParameters query) {
        return JPAUtils.queryEntitiesCount(em, Movie.class, query);
    }

    public MovieDto getMovie(Long movieId) {
        Movie movie = em.find(Movie.class, movieId);
        return movie != null ? movie.toDto() : null;
    }

    @Transactional
    public boolean addMovie(MovieDto movie) {
        if (movie != null) {
            Movie existingMovie = em.unwrap(Session.class).bySimpleNaturalId(Movie.class).load(movie.getImdbId());
            if (existingMovie == null) {
                em.merge(movie.toMovie());
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean updateMovie(Long movieId, MovieDto movie) {
        if (movie != null) {
            Movie existingMovie = em.find(Movie.class, movieId);
            if (existingMovie != null && existingMovie.getImdbId().equals(movie.getImdbId())) {
                existingMovie.setDescription(movie.getDescription());
                existingMovie.setTitle(movie.getTitle());
                existingMovie.setYear(movie.getYear());
                existingMovie.setPictureUrls(movie.getPictureUrls());
                Set<Actor> actorsToSet = null;
                if (movie.getActors() != null) {
                    actorsToSet = movie.getActors().stream().map(ActorDto::toActor).collect(Collectors.toSet());
                }
                existingMovie.setActors(actorsToSet);
                em.merge(existingMovie);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void deleteMovie(Long movieId) {
        Movie movie = em.find(Movie.class, movieId);
        if (movie != null) {
            em.remove(movie);
        }
    }

    @Transactional
    public Movie addActor(Long movieId, ActorDto actor) {
        Movie movie = em.find(Movie.class, movieId);
        if (movie != null) {
            movie.addActor(actor.toActor());
            em.merge(movie);
        }
        return movie;
    }

    @Transactional
    public void removeActor(Long movieId, Long actorId) {
        Movie movie = em.find(Movie.class, movieId);
        Actor actor = em.find(Actor.class, actorId);
        if (movie != null && actor != null) {
            movie.removeActor(actor);
            em.merge(movie);
        }
    }

    public List<MovieDto> searchByKeyword(String keyword) {
        if (keyword == null) {
            return Collections.emptyList();
        }
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Movie.class).get();
        var luceneQuery = qb
                .keyword()
                .onFields("title", "actors.name", "actors.surname")
                .matching(keyword)
                .createQuery();
        var jpaQuery =
                fullTextEntityManager.createFullTextQuery(luceneQuery, Movie.class);
        List<Movie> results = jpaQuery.getResultList();
        return results.stream().map(Movie::toDto).collect(Collectors.toList());
    }
}
