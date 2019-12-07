package movies.service;

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

    public Movie getMovie(long movieId) {
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
    public boolean updateMovie(long movieId, Movie movie) {
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
    public void deleteMovie(long movieId) {
        Movie movie = em.find(Movie.class, movieId);
        if (movie != null) {
            em.remove(movie);
        }
    }
}
