package com.example.library.Services;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.library.DTO.Book.Request.RequestDataBook;
import com.example.library.DTO.Book.Response.StatusResponse;
import com.example.library.DTO.Book.Response.StatusResponseList;
import com.example.library.DTO.Book.Response.DataBook.ResponseDataBook;
import com.example.library.Entity.BookEntity;
import com.example.library.Repository.BookRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor

public class BookService {

    private final BookRepository bookRepository;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String formatDate = sdf.format(new Date());

    public ResponseEntity<StatusResponse> addBook(RequestDataBook book) {
        log.info("Starting the process to add a new book: {}", book);

        try {
            // Map RequestDataBook to BookEntity
            BookEntity bookEntity = new BookEntity();
            BookEntity addBook = mapToEntity(bookEntity, book);
            bookRepository.save(addBook);
            log.info("Mapped BookEntity: {}", addBook);

            // Map saved BookEntity to StatusResponse
            StatusResponse response = mapToResponse(addBook, HttpStatus.OK);
            log.info("Book added successfully: {}", response);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {  
            log.error("An error occurred while adding the book: {}", e.getMessage());

            // Map error response
            StatusResponse errorResponse = mapToResponse(null, HttpStatus.BAD_REQUEST);
            log.info("Error response: {}", errorResponse);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (NullPointerException e) {
            log.error("An error occurred while adding the book: {}", e.getMessage());

            // Map error response
            StatusResponse errorResponse = mapToResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
            log.info("Error response: {}", errorResponse);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    public ResponseEntity<StatusResponse> editBook(Integer idBook, RequestDataBook updateBook) {
        log.info("Editing book: {} : {}", updateBook, idBook);

        try {
            // Map ReqestData for Edit
            BookEntity bookEntity = bookRepository.findById(idBook).orElse(null);

            mapToEntity(bookEntity, updateBook);
            bookRepository.save(bookEntity);
            log.info("Book updated in database: {}", updateBook);

            StatusResponse response = mapToResponse(bookEntity, HttpStatus.OK);
            log.info("Book updated successfully: {}", response);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("An error occurred while adding the book: {}", e.getMessage());

            // Map error response
            StatusResponse errorResponse = mapToResponse(null, HttpStatus.BAD_REQUEST);
            log.info("Error response: {}", errorResponse);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }
    }

    public ResponseEntity<StatusResponse> getBook(Integer idBook) {
        log.info("getting data with ID : {}", idBook);

        // get data from database
        Optional<BookEntity> getBook = bookRepository.findByIdBook(idBook);
        log.info("Data Book With ID : {}", getBook);
        if (getBook.isPresent()) {
            BookEntity bookEntity = getBook.get();
            StatusResponse response = mapToResponse(bookEntity, HttpStatus.OK);
            log.info("Book found: {}", response);
            return ResponseEntity.ok(response);
        } else {
            log.warn("Book with ID {} not found", idBook);
            StatusResponse errResponse = mapToResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errResponse);
        }
    }

    public ResponseEntity<StatusResponse> deleteBook(Integer idBook) {
        log.info("Deleting book with ID : {}", idBook);

        // get data from database

        Optional<BookEntity> bookEntityOptional = bookRepository.findByIdBook(idBook);
        log.info("Data Book With ID {}: {}", bookEntityOptional, idBook);

        if (bookEntityOptional.isPresent()) {
            BookEntity bookEntity = bookEntityOptional.get();
            log.info("before delete : {}", bookEntity);
            bookRepository.delete(bookEntity);
            log.info("Book deleted succesfully: {}", bookEntity);
            StatusResponse response = mapToResponse(bookEntity, HttpStatus.OK);
            log.info("Book deleted successfully: {}", response);
            return ResponseEntity.ok(response);
        } else {
            log.warn("Book with ID {} not found", idBook);

            StatusResponse errResponse = mapToResponse(null, HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
        }
    }

    public ResponseEntity<StatusResponseList> getAllBooks() {
        log.info("Getting all books data");

        try {
            List<BookEntity> books = bookRepository.findAll();
            StatusResponseList response = mapToListResponse(books, HttpStatus.OK);
            log.info("All books retrieved: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("An error occurred while retrieving all books: {}", e.getMessage());

            StatusResponseList errorResponse = mapToListResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private BookEntity mapToEntity(BookEntity bookEntity, RequestDataBook book) {
        log.info("Mapping RequestDataBook to BookEntity: {}", book);
        bookEntity.setBookName(book.getBookName());
        bookEntity.setPage(book.getPage());
        bookEntity.setPublisher(book.getPublisher());
        bookEntity.setAuthor(book.getAuthor());
        bookEntity.setRelease(book.getRelease());
        bookEntity.setStock(book.getStock());
        String coverBase64 = book.getCoverBase64();
        String coverUrl = book.getCoverUrl();
        if ((coverBase64 == null || coverBase64.isBlank())
                && (coverUrl == null || coverUrl.isBlank())
                && book.getCover() != null && !book.getCover().isBlank()) {
            if (book.getCover().startsWith("http://") || book.getCover().startsWith("https://")) {
                coverUrl = book.getCover();
            } else {
                coverBase64 = book.getCover();
            }
        }

        bookEntity.setCoverUrl(coverUrl);
        if (coverBase64 != null && !coverBase64.isBlank()) {
            byte[] coverBytes = Base64.getDecoder().decode(coverBase64);
            bookEntity.setCover(coverBytes);
        }
        log.info("Mapped BookEntity: {}", bookEntity);
        return bookEntity;
    }

    private StatusResponse mapToResponse(BookEntity savedEntity, HttpStatus statusCode) {
        StatusResponse statusResponse = new StatusResponse();

        if (savedEntity != null) {
            ResponseDataBook responseData = mapToResponseData(savedEntity);

            statusResponse.setData(responseData);
        } else {
            statusResponse.setData(null);
        }

        if (statusCode == HttpStatus.OK) {
            statusResponse.setStatus("Success");
            statusResponse.setMessage("Book operation successful");
            statusResponse.setResponseCode("200");
        } else if (statusCode == HttpStatus.BAD_REQUEST) {
            statusResponse.setStatus("Bad Request");
            statusResponse.setMessage("Book operation failed");
            statusResponse.setResponseCode("400");
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
            statusResponse.setStatus("Internal Server Error");
            statusResponse.setMessage("Unknown Error");
            statusResponse.setResponseCode("500");
        } else {
            statusResponse.setStatus("Error");
            statusResponse.setMessage("Book Not Found");
            statusResponse.setResponseCode("404");
        }

        statusResponse.setTimestamp(formatDate);

        return statusResponse;
    }

    private StatusResponseList mapToListResponse(List<BookEntity> books, HttpStatus statusCode) {
        StatusResponseList statusResponse = new StatusResponseList();
        List<ResponseDataBook> responseDataList = new ArrayList<>();

        if (books != null) {
            for (BookEntity book : books) {
                responseDataList.add(mapToResponseData(book));
            }
        }

        statusResponse.setData(responseDataList);

        if (statusCode == HttpStatus.OK) {
            statusResponse.setStatus("Success");
            statusResponse.setMessage("Books retrieved successfully");
            statusResponse.setResponseCode("200");
        } else if (statusCode == HttpStatus.BAD_REQUEST) {
            statusResponse.setStatus("Bad Request");
            statusResponse.setMessage("Failed to retrieve books");
            statusResponse.setResponseCode("400");
        } else {
            statusResponse.setStatus("Internal Server Error");
            statusResponse.setMessage("Unknown Error");
            statusResponse.setResponseCode("500");
        }

        statusResponse.setTimestamp(formatDate);

        return statusResponse;
    }

    private ResponseDataBook mapToResponseData(BookEntity savedEntity) {
        ResponseDataBook responseData = new ResponseDataBook();
        responseData.setIdBook(savedEntity.getIdBook());
        responseData.setBookName(savedEntity.getBookName());
        responseData.setAuthor(savedEntity.getAuthor());
        responseData.setPublisher(savedEntity.getPublisher());
        responseData.setTotalPage(savedEntity.getPage());
        responseData.setReleaseYear(savedEntity.getRelease());
        responseData.setStock(savedEntity.getStock());
        if (savedEntity.getCover() != null && savedEntity.getCover().length > 0) {
            String coverBase64 = Base64.getEncoder().encodeToString(savedEntity.getCover());
            responseData.setCoverBase64(coverBase64);
        }
        responseData.setCoverUrl(savedEntity.getCoverUrl());
        return responseData;
    }

}
