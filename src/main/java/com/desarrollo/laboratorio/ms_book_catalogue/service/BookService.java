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
import java.util.Map;

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
        if(bookRepository.existsByIsbn(book.getIsbn())) {
            throw new BookException("Libro con ISBN ya existe: " + book.getIsbn());
        }
        Book bookEntity = new Book(book.getTitle(), book.getAuthor(), book.getIsbn(),
                book.getPublicationDate(), book.getCategory(), book.getPrice(),
                book.getStock());
        return bookRepository.save(bookEntity);
    }

    public Book updateBook(Long id, BookDTO bookDetails) {
        Book book = getBookById(id);
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setCategory(bookDetails.getCategory());
        book.setIsbn(bookDetails.getIsbn());
        book.setPrice(bookDetails.getPrice());
        book.setStock(bookDetails.getStock());
        book.setVisible(bookDetails.getVisible());
        book.setRating(bookDetails.getRating());
        return bookRepository.save(book);
    }

    // Actualizar stock basado en lista de OrderDTO
    @Transactional
    public boolean updateStockAfterOrder(List<OrderDTO> orders) {
        for (OrderDTO order : orders) {
            Book book = bookRepository.findById(order.getBookId()).orElseThrow(()->
                    new BookException("Libro no encontrado con ID: " + order.getBookId()));
            if (book.getStock() < order.getQuantity()) {
                throw new BookException("Libro sin stock ID: " + order.getBookId());
            } else if (!book.getVisible()) {
                throw new BookException("Libro no visible ID: " + order.getBookId());
            }
            book.setStock(book.getStock() - order.getQuantity());
            bookRepository.save(book);
        }
        return true;
    }
    //Actualizar parcialmente un libro
    @Transactional
    public Book partialUpdateBook(Long id, Map<String, Object> updates) {
        Book book = getBookById(id);
        updates.forEach((key, value) -> {
            switch (key) {
                case "title" -> book.setTitle((String) value);
                case "author" -> book.setAuthor((String) value);
                case "category" -> book.setCategory((String) value);
                case "isbn" -> book.setIsbn((String) value);
                case "price" -> book.setPrice((Double) value);
                case "stock" -> book.setStock((Integer) value);
                case "rating" -> book.setRating((Double) value);
                case "visible" -> book.setVisible((Boolean) value);
                default -> throw new BookException("Campo no válido: " + key);
            }
        });
        return bookRepository.save(book);
    }
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }


    public List<Book> searchBooks(String title, String author, String category, String isbn, Double rating) {
       //Iniciar busqueda con libros visibles
        List <Book> books = bookRepository.findByVisibleTrue();
        //Filtrar por título si esta presente
        if (title != null) {
         books = books.stream().filter(book -> book.getTitle() != null && book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();}
        //Filtrar por categoria si esta presente
        if (category != null) {
            books = books.stream().filter(book -> book.getCategory() != null && book.getCategory().toLowerCase().
                            contains(category.toLowerCase())).toList();}
        //Filtrar por autor si esta presente
        if (author != null) {
            books = books.stream().filter(book -> book.getAuthor()!=null && book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                    .toList();}
        //Filtrar por ISBN si esta presente
        if (isbn != null) {
            books = books.stream().filter(book -> book.getIsbn() != null && book.getIsbn().toLowerCase().contains(isbn.toLowerCase()))
                    .toList();}
        //Filtrar por rating si esta presente
        if (rating != null) {
            books = books.stream().filter(book -> book.getRating()!= null && book.getRating().equals(rating)).toList();}
        return books;
        }

}
