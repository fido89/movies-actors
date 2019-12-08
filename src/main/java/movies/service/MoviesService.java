package movies.service;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import movies.domain.Actor;
import movies.domain.Movie;
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

@RequestScoped
public class MoviesService {

    @PersistenceContext
    private EntityManager em;

    public List<Movie> getMovies(QueryParameters query) {
        List<Movie> movies = JPAUtils.queryEntities(em, Movie.class, query);
        return movies;
    }

    public Long getMoviesCount(QueryParameters query) {
        return JPAUtils.queryEntitiesCount(em, Movie.class, query);
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

    public List<Movie> searchByKeyword(String keyword) {
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
        return jpaQuery.getResultList();
    }
}
