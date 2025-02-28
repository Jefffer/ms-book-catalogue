package com.desarrollo.laboratorio.ms_book_catalogue.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private String title;
    private String author;
    private String isbn;
    private Timestamp publicationDate;
    private String category;
    private Double price;
    private int stock;
    @Min(0) @Max(5)
    private Double rating;
    private String cover;
    private Boolean visible;
}
