package com.desarrollo.laboratorio.ms_book_catalogue.repository;


import com.desarrollo.laboratorio.ms_book_catalogue.model.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByCategoryContainingIgnoreCase(String category);
    List<Book> findByIsbn(String isbn);
    List<Book> findByRating(Double rating);
    List<Book> findByVisibleTrue(); // Libros visibles

    boolean existsByIsbn(String isbn);
}
