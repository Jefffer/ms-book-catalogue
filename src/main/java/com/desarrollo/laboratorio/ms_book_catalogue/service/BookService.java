package com.desarrollo.laboratorio.ms_book_catalogue.service;

import com.desarrollo.laboratorio.ms_book_catalogue.exception.BookException;
import com.desarrollo.laboratorio.ms_book_catalogue.model.dto.OrderDTO;
import com.desarrollo.laboratorio.ms_book_catalogue.model.entities.Book;
import com.desarrollo.laboratorio.ms_book_catalogue.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks(boolean includeHidden) {
        return includeHidden ? bookRepository.findAll() : bookRepository.findByVisibleTrue();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookException("Libro no encontrado con ID: " + id));
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = getBookById(id);
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setCategory(bookDetails.getCategory());
        book.setIsbn(bookDetails.getIsbn());
        book.setPrice(bookDetails.getPrice());
        book.setStock(bookDetails.getStock());
        book.setRating(bookDetails.getRating());
        book.setVisible(bookDetails.getVisible());
        return bookRepository.save(book);
    }

    // Aactualizar stock basado en lista de OrderDTO
    @Transactional
    public void updateStockAfterOrder(List<OrderDTO> orders) {
        for (OrderDTO order : orders) {
            Book book = bookRepository.findById(order.getBookId())
                    .orElseThrow(() -> new BookException("Libro no encontrado - ID: " + order.getBookId()));

            if (book.getStock() < order.getQuantity()) {
                throw new IllegalStateException("Stock insuficiente para el libro - ID: " + order.getBookId());
            }

            book.setStock(book.getStock() - order.getQuantity());
            bookRepository.save(book);
        }
    }

    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }
}
