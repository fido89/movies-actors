package movies.controllers;

import com.kumuluz.ee.rest.beans.QueryParameters;
import movies.annotations.Stats;
import movies.domain.dtos.ActorDto;
import movies.service.ActorsService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("actors")
public class ActorsController {

    @Inject
    private ActorsService actorService;
    @Context
    protected UriInfo uriInfo;

    @GET
    @Stats
    public Response getAllActors() {
        QueryParameters query = createQuery();
        List<ActorDto> actors = actorService.getActors(query);
        Long allActorsCount = actorService.getActorsCount(query);
        return Response.ok(actors).header("X-Total-Count", allActorsCount).build();
    }

    @GET
    @Path("{actorId}")
    @Stats
    public Response getMovie(@PathParam("actorId") long actorId) {
        ActorDto actor = actorService.getActor(actorId);
        return actor != null
                ? Response.ok(actor).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Stats
    public Response addNewActor(ActorDto actor) {
        boolean success = actorService.addActor(actor);
        return success ? Response.noContent().build() : Response.status(Response.Status.CONFLICT).build();
    }

    @PUT
    @Path("{actorId}")
    @Stats
    public Response updateActor(@PathParam("actorId") long actorId, ActorDto actor) {
        boolean success = actorService.updateActor(actorId, actor);
        return success ? Response.ok(actor).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{actorId}")
    @Stats
    public Response deleteActor(@PathParam("actorId") long actorId) {
        actorService.deleteActor(actorId);
        return Response.noContent().build();
    }

    private QueryParameters createQuery() {
        return QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0).defaultLimit(10).build();
    }
}
