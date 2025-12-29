package com.example.library.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.DTO.Book.Request.RequestDataBook;
import com.example.library.DTO.Book.Response.StatusResponse;
import com.example.library.DTO.Book.Response.StatusResponseList;
import com.example.library.Services.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor

@Tag(name = "Book", description = "Book API")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Add Book", description = "Add a new book to the Library Management System")
    // Book Contoller
    @PostMapping("/add")
    public ResponseEntity<StatusResponse> addBook(@RequestBody @Valid RequestDataBook book) {
        log.info("Adding book: {}", book);

        ResponseEntity<StatusResponse> saveBook = bookService.addBook(book);

        return saveBook;
    }

    @PutMapping("/edit/{idBook}")
    public ResponseEntity<StatusResponse> editBook(@PathVariable @Valid Integer idBook,
            @RequestBody RequestDataBook updateBook) {
        log.info("Editing Data Book with : {}", updateBook);

        ResponseEntity<StatusResponse> editedBook = bookService.editBook(idBook, updateBook);

        return editedBook;
    }

    @GetMapping("/get/{idBoook}")
    public ResponseEntity<StatusResponse> getBook(@PathVariable Integer idBoook) {
        log.info("Get Book with ID : {}", idBoook);

        ResponseEntity<StatusResponse> getBook = bookService.getBook(idBoook);

        return getBook;
    }

    @DeleteMapping("/delete/{idBook}")
    public ResponseEntity<StatusResponse> deleteBook(@PathVariable Integer idBook) {
        log.info("Preparing to delete book with ID : {}", idBook);

        ResponseEntity<StatusResponse> deleteDataBook = bookService.deleteBook(idBook);

        return deleteDataBook;
    }

    @GetMapping("/get/all")
    @Operation(summary = "Get All Books", description = "Retrieve all books from the Library Management System")
    public ResponseEntity<StatusResponseList> getAllBooks() {
        log.info("Get all books data");

        ResponseEntity<StatusResponseList> response = bookService.getAllBooks();

        return response;
    }

}
