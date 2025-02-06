package com.desarrollo.laboratorio.ms_book_catalogue.controller;

import com.desarrollo.laboratorio.ms_book_catalogue.model.dto.BookDTO;
import com.desarrollo.laboratorio.ms_book_catalogue.model.dto.OrderDTO;
import com.desarrollo.laboratorio.ms_book_catalogue.model.entities.Book;
import com.desarrollo.laboratorio.ms_book_catalogue.service.BookService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@Validated
@Slf4j
public class BookController {

    public static final String OK = "OK";
    @Autowired
    private BookService bookService;

    // Obtener todos los libros (sin ocultos por defecto)
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(@RequestParam(defaultValue = "false") boolean includeHidden) {
        return ResponseEntity.ok(bookService.getAllBooks(includeHidden));
    }

    // Obtener un libro por ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // Buscar libros por atributos (título, autor, etc.)
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam(required = false) String title,
                                                  @RequestParam(required = false) String author,
                                                  @RequestParam(required = false) String category,
                                                  @RequestParam(required = false) String isbn,
                                                  @RequestParam(required = false) Double rating) {
        List<Book> books = bookService.searchBooks(title, author, category, isbn, rating);
        return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(books);
    }

    // Crear un nuevo libro
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody @Valid BookDTO book) {
        return ResponseEntity.ok(bookService.createBook(book));
    }

    // Modificar completamente un libro
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody @Valid BookDTO book) {
        return ResponseEntity.ok(bookService.updateBook(id, book));
    }

    // Modificar stock -> llamado desde ms-book-payments
    @PostMapping("/update-stock")
    public ResponseEntity<String> updateStock(@RequestBody @Valid List<OrderDTO> orders) {
        log.info("UpdateStock");
        boolean result = bookService.updateStockAfterOrder(orders);
        return result ? ResponseEntity.ok(OK) : ResponseEntity.badRequest().build();
    }

    // Modificar parcialmente
    @PatchMapping("/{id}")
    public ResponseEntity<Book> partialUpdateBook(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(bookService.partialUpdateBook(id, updates));
    }

    // Eliminar un libro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
