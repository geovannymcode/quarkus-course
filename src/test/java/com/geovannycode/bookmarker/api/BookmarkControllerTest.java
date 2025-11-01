package com.geovannycode.bookmarker.api;

import com.geovannycode.bookmarker.exceptions.ResourceNotFoundException;
import com.geovannycode.bookmarker.services.BookmarkService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class BookmarkControllerTest {

    @Inject
    BookmarkService bookmarkService;

    @Inject
    Flyway flyway;

    @BeforeEach
    void migrate() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void shouldGetAllBookmarks() {
        given().when()
                .get("/api/bookmarks/all")
                .then()
                .statusCode(200)
                .body("size()", is(13));
    }

    @DisplayName("Get paginated bookmarks")
    @ParameterizedTest(name = "{index} - {displayName} with page={0}")
    @CsvSource(textBlock = """
        1,10,2,13,true,false
        2,3,2,13,false, true
    """)
    void shouldGetPagedBookmarks(int page, int dataSize,
                                 int totalPages, int totalElements,
                                 boolean hasNextPage, boolean hasPreviousPage) {
        given().when()
                .get("/api/bookmarks?page={page}", page)
                .then()
                .statusCode(200)
                .body("data", hasSize(dataSize))
                .body("totalElements", is(totalElements))
                .body("currentPageNo", is(page))
                .body("totalPages", is(totalPages))
                .body("hasNextPage", is(hasNextPage))
                .body("hasPreviousPage", is(hasPreviousPage));
    }

    @Test
    void shouldGetBookmarkById() {
        given().when()
                .get("/api/bookmarks/{id}", 1)
                .then()
                .statusCode(200)
                .body("title", is("Geovannycode Blog"))
                .body("url", is("https://geovannycode.com"))
                .body("description", is("Geovannycode Blog"));
    }

    @Test
    void shouldCreateBookmark() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "title": "Test Bookmark",
                        "url": "https://example.com",
                        "description": "A test"
                     }
                     """)
                .when()
                .post("/api/bookmarks")
                .then()
                .statusCode(201)
                .body("title", is("Test Bookmark"))
                .body("url", is("https://example.com"))
                .body("description", is("A test"));
    }

    @Test
    void shouldUpdateBookmark() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "title": "Test Bookmark - Updated",
                        "url": "https://example-updated.com",
                        "description": "A test - Updated"
                     }
                     """)
                .when()
                .put("/api/bookmarks/{id}", 1)
                .then()
                .statusCode(200)
                .body("title", is("Test Bookmark - Updated"))
                .body("url", is("https://example-updated.com"))
                .body("description", is("A test - Updated"));
    }

    @Test
    void shouldDeleteBookmark() {
        given()
                .when()
                .delete("/api/bookmarks/{id}", 1)
                .then()
                .statusCode(204);

        assertThrows(ResourceNotFoundException.class,
                () -> bookmarkService.getBookmarkById(1L));
    }


}
