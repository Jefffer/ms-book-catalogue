package com.desarrollo.laboratorio.ms_book_catalogue.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(
        name="books"
)
@Getter
@Setter
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100) @NotEmpty
    private String title;

    @NotEmpty
    private Timestamp publicationDate;

    @Column(length = 20) @NotEmpty
    private String category;

    @Column(length = 14) @NotEmpty
    private String isbn;

    @NotEmpty
    @Min(0) @Max(5)
    private int rating;

    @NotEmpty
    private boolean visibility;

    public Book(String title, String category, String isbn) {
        this.title = title;
        this.category = category;
        this.isbn = isbn;
        this.rating = 0;
        this.visibility = true;
        this.publicationDate = new Timestamp(System.currentTimeMillis());
    }

    public Book() {
    }
}
