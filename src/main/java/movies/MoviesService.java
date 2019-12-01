package movies;

import movies.Movies;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequestScoped
public class MoviesService {

    @PersistenceContext
    private EntityManager em;

    public List<Movies> getMovies() {
        List<Movies> movies = em
                .createNamedQuery("Movies.findMovies", Movies.class)
                .getResultList();

        return movies;
    }
}
