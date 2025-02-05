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
    private int id;

    @Column(length = 200) @NotEmpty
    private String title;

    @Column(length = 200) @NotEmpty
    private String author;

    @NotEmpty
    private Timestamp publicationDate;

    @Column(length = 20) @NotEmpty
    private String category;

    @Column(length = 14) @NotEmpty
    private String isbn;

    private Double price;

    private Integer stock;

    @NotEmpty
    private Double rating;

    @NotEmpty
    private Boolean visible;
}
