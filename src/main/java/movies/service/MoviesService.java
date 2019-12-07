package movies.service;

import movies.domain.Actor;
import movies.domain.Movie;
import org.hibernate.Session;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class MoviesService {

    @PersistenceContext
    private EntityManager em;

    public List<Movie> getMovies() {
        List<Movie> movies = em
                .createNamedQuery("Movie.findMovies", Movie.class)
                .getResultList();

        return movies;
    }

    public Movie getMovie(Long movieId) {
        return em.find(Movie.class, movieId);
    }

    @Transactional
    public boolean addMovie(Movie movie) {
        if (movie != null) {
            Movie existingMovie = em.unwrap(Session.class).bySimpleNaturalId(Movie.class).load(movie.getImdbId());
            if (existingMovie == null) {
                em.persist(movie);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean updateMovie(Long movieId, Movie movie) {
        if (movie != null) {
            Movie existingMovie = em.find(Movie.class, movieId);
            if (existingMovie != null && existingMovie.getImdbId().equals(movie.getImdbId())) {
                em.merge(movie);
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
    public Movie addActor(Long movieId, Actor actor) {
        Movie movie = em.find(Movie.class, movieId);
        if (movie != null) {
            movie.addActor(actor);
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
}
