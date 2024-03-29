package movies.service;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import movies.domain.Actor;
import movies.domain.dtos.ActorDto;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class ActorsService {

    @PersistenceContext
    private EntityManager em;

    public List<ActorDto> getActors(QueryParameters query) {
        List<Actor> actors = JPAUtils.queryEntities(em, Actor.class, query);
        return actors.stream().map(Actor::toDto).collect(Collectors.toList());
    }

    public Long getActorsCount(QueryParameters query) {
        return JPAUtils.queryEntitiesCount(em, Actor.class, query);
    }

    public ActorDto getActor(long actorId) {
        Actor actor = em.find(Actor.class, actorId);
        return actor != null ? actor.toDto() : null;
    }

    @Transactional
    public void addActor(ActorDto actorDto) {
        em.persist(actorDto.toActor());
    }

    @Transactional
    public void updateActor(long actorId, ActorDto actorDto) {
        Actor actor = em.find(Actor.class, actorId);
        if (actor != null) {
            actor.setName(actorDto.getName());
            actor.setSurname(actorDto.getSurname());
            em.merge(actor);
        }
    }

    @Transactional
    public void deleteActor(long actorId) {
        Actor actor = em.find(Actor.class, actorId);
        if (actor != null) {
            em.remove(actor);
        }
    }
}
