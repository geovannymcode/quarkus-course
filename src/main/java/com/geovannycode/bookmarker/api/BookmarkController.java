package com.geovannycode.bookmarker.api;

import com.geovannycode.bookmarker.entities.Bookmark;
import com.geovannycode.bookmarker.exceptions.ResourceNotFoundException;
import com.geovannycode.bookmarker.models.PagedResult;
import com.geovannycode.bookmarker.services.BookmarkService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @GET
    @Path("/all")
    public List<Bookmark> getAllBookmarks() {
        return bookmarkService.getAllBookmarks();
    }

    @GET
    public PagedResult<Bookmark> getBookmarks(
            @QueryParam("page") @DefaultValue("1") int page) {
        return bookmarkService.getBookmarks(page);
    }

    @GET
    @Path("/{id}")
    public Bookmark getBookmarksById(@PathParam("id") Long id) {
        return bookmarkService.getBookmarkById(id)
                .orElseThrow(()->new ResourceNotFoundException("Bookmark not found"));
    }

    @POST
    public Response saveBookmark(BookmarkPayload payload) {
        var b = new Bookmark(
                payload.title(),
                payload.url(),
                payload.description()
        );
        Bookmark bookmark = bookmarkService.saveBookmark(b);
        return Response.status(Response.Status.CREATED).entity(bookmark).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBookmark(@PathParam("id") Long id, BookmarkPayload payload) {
        var b = new Bookmark(
                payload.title(),
                payload.url(),
                payload.description()
        );
        Bookmark bookmark = bookmarkService.updateBookmark(id, b);
        return Response.status(Response.Status.OK).entity(bookmark).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBookmark(@PathParam("id") Long id) {
        bookmarkService.deleteBookmark(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    public record BookmarkPayload(
            @NotEmpty(message = "Title is required")
            String title,
            @NotEmpty(message = "URL is required")
            String url,
            String description) {}

}
