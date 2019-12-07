package movies.service;

import movies.domain.Actor;
import movies.domain.Movie;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class ActorsService {

    @PersistenceContext
    private EntityManager em;

    public List<Actor> getActors() {
        List<Actor> actors = em
                .createNamedQuery("Actor.findActors", Actor.class)
                .getResultList();
        return actors;
    }

    public Actor getActor(long actorId) {
        return em.find(Actor.class, actorId);
    }

    @Transactional
    public boolean addActor(Actor actor) {
        if (actor != null) {
            em.persist(actor);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateActor(long actorId, Actor actor) {
        if (actor != null) {
            em.merge(actor);
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteActor(long actorId) {
        Actor actor = em.find(Actor.class, actorId);
        if (actor != null) {
            em.remove(actor);
        }
    }
}
