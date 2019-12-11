package movies.controllers;

import com.kumuluz.ee.rest.beans.QueryParameters;
import movies.annotations.Stats;
import movies.domain.dtos.ActorDto;
import movies.service.ActorsService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
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
    public Response getMovie(@PathParam("actorId") long actorId, @Context Request request) {
        ActorDto actor = actorService.getActor(actorId);
        return actor != null
                ? getResponseWithHTTPCache(request, actor)
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Stats
    public Response addNewActor(@Valid ActorDto actor) {
        actorService.addActor(actor);
        return Response.noContent().build();
    }

    @PUT
    @Path("{actorId}")
    @Stats
    public Response updateActor(@PathParam("actorId") long actorId, @Context Request request, @Valid ActorDto actor) {
        ActorDto existingActor = actorService.getActor(actorId);
        if (existingActor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        EntityTag etag = new EntityTag(Integer.toString(existingActor.hashCode()));
        Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
        // client is not up to date
        if (builder != null) {
            return builder.build();
        }
        actorService.updateActor(actorId, actor);
        builder = Response.noContent();
        return builder.build();
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

    private Response getResponseWithHTTPCache(Request request, ActorDto actor) {
        EntityTag etag = new EntityTag(Integer.toString(actor.hashCode()));
        Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
        if (builder == null) {
            builder = Response.ok(actor);
            builder.tag(etag);
        }
        return builder.build();
    }
}
