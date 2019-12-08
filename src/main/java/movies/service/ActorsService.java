package movies.service;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
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

    public List<Actor> getActors(QueryParameters query) {
        List<Actor> actors = JPAUtils.queryEntities(em, Actor.class, query);
        return actors;
    }

    public Long getActorsCount(QueryParameters query) {
        return JPAUtils.queryEntitiesCount(em, Actor.class, query);
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
