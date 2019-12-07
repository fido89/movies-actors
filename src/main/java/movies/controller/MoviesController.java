package movies.controller;

import movies.domain.Actor;
import movies.domain.Movie;
import movies.service.MoviesService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("movies")
public class MoviesController {

    @Inject
    private MoviesService moviesService;

    @GET
    public Response getAllMovies() {
        List<Movie> movies = moviesService.getMovies();
        return Response.ok(movies).build();
    }

    @GET
    @Path("{movieId}")
    public Response getMovie(@PathParam("movieId") Long movieId) {
        Movie movie = moviesService.getMovie(movieId);
        return movie != null
                ? Response.ok(movie).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response addNewMovie(Movie movie) {
        boolean success = moviesService.addMovie(movie);
        return success ? Response.noContent().build() : Response.status(Response.Status.CONFLICT).build();
    }

    @PUT
    @Path("{movieId}")
    public Response updateMovie(@PathParam("movieId") Long movieId, Movie movie) {
        boolean success = moviesService.updateMovie(movieId, movie);
        return success ? Response.ok(movie).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{movieId}")
    public Response deleteMovie(@PathParam("movieId") Long movieId) {
        moviesService.deleteMovie(movieId);
        return Response.noContent().build();
    }

    @GET
    @Path("/{movieId}/actors")
    public Response getActors(@PathParam("movieId") Long movieId) {
        Movie movie = moviesService.getMovie(movieId);
        return movie != null
                ? Response.ok(movie.getActors()).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{movieId}/actors")
    public Response addActor(@PathParam("movieId") Long movieId, Actor actor) {
        Movie movie = moviesService.addActor(movieId, actor);
        return movie != null ? Response.ok(movie).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{movieId}/actors/{actorId}")
    public Response removeActor(@PathParam("movieId") Long movieId, @PathParam("actorId") Long actorId) {
        moviesService.removeActor(movieId, actorId);
        return Response.noContent().build();
    }
}
