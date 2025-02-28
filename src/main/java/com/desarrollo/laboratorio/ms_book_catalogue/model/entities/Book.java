package com.desarrollo.laboratorio.ms_book_catalogue.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200) @NotEmpty
    private String title;

    @Column(length = 200) @NotEmpty
    private String author;


    private Timestamp publicationDate;

    @Column(length = 20) @NotEmpty
    private String category;

    @Column(length = 14, unique = true) @NotEmpty
    private String isbn;

    private Double price;

    private Integer stock;

    private Double rating;

    private String cover;

    private Boolean visible;

    public Book(String title, String author, String isbn, Timestamp publicationDate,
                String category, Double price, Integer stock) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.visible = true;
        this.rating = 0.0;
    }
}
