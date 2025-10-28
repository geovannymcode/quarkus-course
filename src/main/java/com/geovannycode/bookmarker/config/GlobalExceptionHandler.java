package com.geovannycode.bookmarker.config;

import com.geovannycode.bookmarker.exceptions.ResourceNotFoundException;
import io.quarkus.logging.Log;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class GlobalExceptionHandler {

    @ServerExceptionMapper(ResourceNotFoundException.class)
    public Response handle(ResourceNotFoundException e) {
        Log.error("ResourceNotFoundException occurred", e);
        return Response.status(Response.Status.NOT_FOUND)
                .entity(e.getMessage())
                .build();
    }

    @ServerExceptionMapper(BadRequestException.class)
    public Response handle(BadRequestException e) {
        Log.error("BadRequestException occurred", e);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage())
                .build();
    }

    @ServerExceptionMapper(Exception.class)
    public Response handle(Exception e) {
        Log.error("Exception occurred", e);
        return Response.serverError().entity("Internal Server Error").build();
    }
}
