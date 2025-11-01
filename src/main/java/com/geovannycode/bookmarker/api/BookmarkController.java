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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@Path("/api/bookmarks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Bookmarks", description = "Operaciones CRUD para gestión de bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }


    @GET
    @Path("/all")
    @Operation(
            summary = "Obtener todos los bookmarks",
            description = "Retorna todos los bookmarks sin paginación"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Lista de bookmarks obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = BookmarkResponse.class))
            )
    })
    public Response getAllBookmarks() {
        List<BookmarkResponse> bookmarks = bookmarkService.getAllBookmarks()
                .stream()
                .map(BookmarkResponse::fromEntity)
                .collect(Collectors.toList());

        return Response.ok(bookmarks).build();
    }

    @GET
    @Operation(
            summary = "Obtener bookmarks paginados",
            description = "Retorna bookmarks con paginación"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Bookmarks paginados obtenidos exitosamente",
                    content = @Content(schema = @Schema(implementation = PagedResult.class))
            )
    })
    public Response getBookmarks(
            @Parameter(description = "Número de página (iniciando en 1)", example = "1")
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
    @Operation(
            summary = "Obtener bookmark por ID",
            description = "Retorna un bookmark específico por su identificador"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Bookmark encontrado",
                    content = @Content(schema = @Schema(implementation = BookmarkResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Bookmark no encontrado"
            )
    })
    public Response getBookmarkById(
            @Parameter(description = "ID del bookmark", required = true, example = "1")
            @PathParam("id") Long id) {
        Bookmark bookmark = bookmarkService.getBookmarkById(id);
        BookmarkResponse response = BookmarkResponse.fromEntity(bookmark);
        return Response.ok(response).build();
    }


    @POST
    @Operation(
            summary = "Crear nuevo bookmark",
            description = "Crea un nuevo bookmark con los datos proporcionados"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Bookmark creado exitosamente",
                    content = @Content(schema = @Schema(implementation = BookmarkResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Datos inválidos o URL duplicada"
            )
    })
    public Response createBookmark(@Valid BookmarkRequest request) {
        Bookmark bookmark = bookmarkService.createBookmark(request);
        BookmarkResponse response = BookmarkResponse.fromEntity(bookmark);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }


    @PUT
    @Path("/{id}")
    @Operation(
            summary = "Actualizar bookmark",
            description = "Actualiza un bookmark existente"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Bookmark actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = BookmarkResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Bookmark no encontrado"
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            )
    })
    public Response updateBookmark(
            @PathParam("id") Long id,
            @Valid BookmarkRequest request) {
        Bookmark bookmark = bookmarkService.updateBookmark(id, request);
        BookmarkResponse response = BookmarkResponse.fromEntity(bookmark);
        return Response.ok(response).build();
    }


    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Eliminar bookmark",
            description = "Elimina un bookmark por su ID"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Bookmark eliminado exitosamente"
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Bookmark no encontrado"
            )
    })
    public Response deleteBookmark(@PathParam("id") Long id) {
        bookmarkService.deleteBookmark(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}

