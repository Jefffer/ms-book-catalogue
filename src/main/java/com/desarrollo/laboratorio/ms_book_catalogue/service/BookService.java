package com.desarrollo.laboratorio.ms_book_catalogue.service;

import com.desarrollo.laboratorio.ms_book_catalogue.exception.BookException;
import com.desarrollo.laboratorio.ms_book_catalogue.model.dto.BookDTO;
import com.desarrollo.laboratorio.ms_book_catalogue.model.dto.OrderDTO;
import com.desarrollo.laboratorio.ms_book_catalogue.model.entities.Book;
import com.desarrollo.laboratorio.ms_book_catalogue.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
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

    public Book createBook(BookDTO book) {
        Book bookEntity = new Book(book.getTitle(), book.getAuthor(), book.getIsbn(),
                book.getPublicationDate(), book.getCategory(), book.getPrice(),
                book.getStock());
        return bookRepository.save(bookEntity);
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
    public boolean updateStockAfterOrder(List<OrderDTO> orders) {
        for (OrderDTO order : orders) {
            Optional<Book> bookOpt = bookRepository.findById(order.getBookId());
            if (bookOpt.isEmpty()) {
                log.error("Libro no encontrado con ID: " + order.getBookId());
                return false;
            }
            Book book = bookOpt.get();
            if (book.getStock() < order.getQuantity()) {
                log.error("Libro sin stock ID: " + order.getBookId());
                return false;
            } else if (!book.getVisible()) {
                log.error("Libro no visible ID: " + order.getBookId());
                return false;
            }
            book.setStock(book.getStock() - order.getQuantity());
            bookRepository.save(book);
        }
        return true;
    }

    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }
}
