package com.geovannycode.bookmarker.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookmarks")
public class Bookmark extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookmark_id_generator")
    @SequenceGenerator(name = "bookmark_id_generator", sequenceName = "bookmark_id_seq")
    public Long id;

    @Column(name = "title", nullable = false, length = 150)
    public String title;

    @Column(name = "url", nullable = false)
    public String url;

    @Column(name = "description")
    public String description;

    public Bookmark() {}

    public Bookmark(String title, String url, String description) {
        this.title = title;
        this.url = url;
        this.description = description;
    }
}
