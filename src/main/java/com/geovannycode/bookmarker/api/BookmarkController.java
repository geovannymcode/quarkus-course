package com.geovannycode.bookmarker.api;

import com.geovannycode.bookmarker.entities.Bookmark;
import com.geovannycode.bookmarker.models.BookmarkRequest;
import com.geovannycode.bookmarker.models.BookmarkResponse;
import com.geovannycode.bookmarker.models.PagedResult;
import com.geovannycode.bookmarker.services.BookmarkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/api/bookmarks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }


    @GET
    @Path("/all")
    public Response getAllBookmarks() {
        List<BookmarkResponse> bookmarks = bookmarkService.getAllBookmarks()
                .stream()
                .map(BookmarkResponse::fromEntity)
                .collect(Collectors.toList());

        return Response.ok(bookmarks).build();
    }

    @GET
    public Response getBookmarks(
            @QueryParam("page") @DefaultValue("1") @Min(1) int page) {
        PagedResult<Bookmark> pagedBookmarks = bookmarkService.getBookmarks(page);

        var response = new PagedResult<>(
                pagedBookmarks.data().stream()
                        .map(BookmarkResponse::fromEntity)
                        .collect(Collectors.toList()),
                pagedBookmarks.currentPageNo(),
                pagedBookmarks.totalPages(),
                pagedBookmarks.totalElements(),
                pagedBookmarks.hasNextPage(),
                pagedBookmarks.hasPreviousPage()
        );

        return Response.ok(response).build();
    }


    @GET
    @Path("/{id}")
    public Response getBookmarkById(@PathParam("id") Long id) {
        Bookmark bookmark = bookmarkService.getBookmarkById(id);
        BookmarkResponse response = BookmarkResponse.fromEntity(bookmark);
        return Response.ok(response).build();
    }


    @POST
    public Response createBookmark(@Valid BookmarkRequest request) {
        Bookmark bookmark = bookmarkService.createBookmark(request);
        BookmarkResponse response = BookmarkResponse.fromEntity(bookmark);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }


    @PUT
    @Path("/{id}")
    public Response updateBookmark(
            @PathParam("id") Long id,
            @Valid BookmarkRequest request) {
        Bookmark bookmark = bookmarkService.updateBookmark(id, request);
        BookmarkResponse response = BookmarkResponse.fromEntity(bookmark);
        return Response.ok(response).build();
    }


    @DELETE
    @Path("/{id}")
    public Response deleteBookmark(@PathParam("id") Long id) {
        bookmarkService.deleteBookmark(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}

