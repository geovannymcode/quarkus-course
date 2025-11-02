package com.geovannycode.bookmarker.config;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Bookmarker API",
                version = "1.0.0",
                description = "API para gestión de bookmarks y categorías con autenticación JWT",
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
@SecurityScheme(
        securitySchemeName = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT token de autenticación. Obtén el token desde /api/auth/login"
)
public class OpenAPIConfig extends Application {
}
