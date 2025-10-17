package com.geovannycode.bookmarker.api;

import io.quarkus.logging.Log;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.time.Instant;

@Path("/api/health")
public class AppInfoController {

    public record AppInfo(String name, String version, String status) { }

    @GET
    @Path("")
    public AppInfo getAppInfo() {
        Log.infof("Getting app info at %s", Instant.now().toString());
        return new AppInfo("Bookmarker", "1.0.0", "UP");
    }
}
