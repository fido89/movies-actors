package movies.controllers;

import com.kumuluz.ee.rest.beans.QueryParameters;
import movies.annotations.Stats;
import movies.domain.Movie;
import movies.domain.dtos.ActorDto;
import movies.domain.dtos.MovieDto;
import movies.service.MoviesService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("movies")
public class MoviesController {

    @Inject
    private MoviesService moviesService;
    @Context
    protected UriInfo uriInfo;

    @GET
    @Stats
    public Response getAllMovies() {
        QueryParameters query = createQuery();
        List<MovieDto> movies = moviesService.getMovies(query);
        Long allMoviesCount = moviesService.getMoviesCount(query);
        return Response.ok(movies).header("X-Total-Count", allMoviesCount).build();
    }

    @GET
    @Path("{movieId}")
    @Stats
    public Response getMovie(@PathParam("movieId") Long movieId, @Context Request request) {
        MovieDto movie = moviesService.getMovie(movieId);
        return movie != null
                ? getResponseWithHTTPCache(request, movie)
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Stats
    public Response addNewMovie(MovieDto movie) {
        boolean success = moviesService.addMovie(movie);
        return success ? Response.noContent().build() : Response.status(Response.Status.CONFLICT).build();
    }

    @PUT
    @Path("{movieId}")
    @Stats
    public Response updateMovie(@PathParam("movieId") Long movieId, @Context Request request, MovieDto movie) {
        MovieDto existingMovie = moviesService.getMovie(movieId);
        if (existingMovie == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        EntityTag etag = new EntityTag(Integer.toString(existingMovie.hashCode()));
        Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
        // client is not up to date
        if (builder != null) {
            return builder.build();
        }
        moviesService.updateMovie(movieId, movie);
        builder = Response.noContent();
        return builder.build();
    }

    @DELETE
    @Path("{movieId}")
    @Stats
    public Response deleteMovie(@PathParam("movieId") Long movieId) {
        moviesService.deleteMovie(movieId);
        return Response.noContent().build();
    }

    @GET
    @Path("{movieId}/actors")
    @Stats
    public Response getActors(@PathParam("movieId") Long movieId) {
        MovieDto movie = moviesService.getMovie(movieId);
        return movie != null
                ? Response.ok(movie.getActors()).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("{movieId}/actors")
    @Stats
    public Response addActor(@PathParam("movieId") Long movieId, ActorDto actor) {
        Movie movie = moviesService.addActor(movieId, actor);
        return movie != null ? Response.ok(movie).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{movieId}/actors/{actorId}")
    @Stats
    public Response removeActor(@PathParam("movieId") Long movieId, @PathParam("actorId") Long actorId) {
        moviesService.removeActor(movieId, actorId);
        return Response.noContent().build();
    }

    @GET
    @Path("search")
    @Stats
    public Response searchMovies(@QueryParam("keyword") String keyword) {
        List<MovieDto> movies = moviesService.searchByKeyword(keyword);
        return Response.ok(movies).build();
    }

    private QueryParameters createQuery() {
        return QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0).defaultLimit(10).build();
    }

    private Response getResponseWithHTTPCache(Request request, MovieDto movie) {
        EntityTag etag = new EntityTag(Integer.toString(movie.hashCode()));
        Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
        if (builder == null) {
            builder = Response.ok(movie);
            builder.tag(etag);
        }
        return builder.build();
    }
}
