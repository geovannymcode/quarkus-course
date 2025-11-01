package com.geovannycode.bookmarker.config;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Bookmarker API",
                version = "1.0.0",
                description = "API para gestión de bookmarks y categorías",
                contact = @Contact(
                        name = "Geovannycode",
                        url = "https://geovannycode.com",
                        email = "contact@geovannycode.com"
                ),
                license = @License(
                        name = "MIT",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Development"),
                @Server(url = "https://api.bookmarker.com", description = "Production")
        }
)
public class OpenAPIConfig extends Application {
}
