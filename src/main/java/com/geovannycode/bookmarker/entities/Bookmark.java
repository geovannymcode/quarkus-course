package com.geovannycode.bookmarker.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bookmarks")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookmark_id_generator")
    @SequenceGenerator(name = "bookmark_id_generator", sequenceName = "bookmark_id_seq")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String url;

    @Column(length = 1000)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Bookmark() {
    }

    public Bookmark(String title, String url, String description) {
        this.title = title;
        this.url = url;
        this.description = description;
    }

    public static BookmarkBuilder builder() {
        return new BookmarkBuilder();
    }


    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }


    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters (package-private for better encapsulation)
    void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bookmark bookmark = (Bookmark) o;
        return Objects.equals(id, bookmark.id) &&
                Objects.equals(url, bookmark.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }

    @Override
    public String toString() {
        return String.format(
                "Bookmark{id=%d, title='%s', url='%s'}",
                id, title, url
        );
    }


    public static class BookmarkBuilder {
        private String title;
        private String url;
        private String description;

        private BookmarkBuilder() {
        }

        public BookmarkBuilder title(String title) {
            this.title = title;
            return this;
        }

        public BookmarkBuilder url(String url) {
            this.url = url;
            return this;
        }

        public BookmarkBuilder description(String description) {
            this.description = description;
            return this;
        }

        public Bookmark build() {
            return new Bookmark(title, url, description);
        }
    }
}
