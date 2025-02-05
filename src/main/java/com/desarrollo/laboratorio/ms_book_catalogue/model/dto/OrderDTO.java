package com.desarrollo.laboratorio.ms_book_catalogue.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long bookId;
    private Integer quantity;
}
