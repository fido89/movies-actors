package movies.controller;

import movies.domain.Actor;
import movies.service.ActorsService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("actors")
public class ActorsController {

    @Inject
    private ActorsService actorService;

    @GET
    public Response getAllActors() {
        List<Actor> actors = actorService.getActors();
        return Response.ok(actors).build();
    }

    @GET
    @Path("{actorId}")
    public Response getMovie(@PathParam("actorId") long actorId) {
        Actor actor = actorService.getActor(actorId);
        return actor != null
                ? Response.ok(actor).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response addNewActor(Actor actor) {
        boolean success = actorService.addActor(actor);
        return success ? Response.noContent().build() : Response.status(Response.Status.CONFLICT).build();
    }

    @PUT
    @Path("{actorId}")
    public Response updateActor(@PathParam("actorId") long actorId, Actor actor) {
        boolean success = actorService.updateActor(actorId, actor);
        return success ? Response.ok(actor).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{actorId}")
    public Response deleteActor(@PathParam("actorId") long actorId) {
        actorService.deleteActor(actorId);
        return Response.noContent().build();
    }
}
