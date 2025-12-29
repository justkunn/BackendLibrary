package com.example.library.Log;

import org.springframework.stereotype.Service;

import com.example.library.DTO.Book.Response.DataBook.ResponseDataBook;
import com.example.library.Entity.BookEntity;
import com.example.library.Repository.BookRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogUtils {
    private final BookRepository lr;

    public BookEntity saveRequest(ResponseDataBook dataBook) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setBookName(dataBook.getBookName());
        bookEntity.setPage(dataBook.getTotalPage());
        bookEntity.setPublisher(dataBook.getPublisher());
        bookEntity.setAuthor(dataBook.getAuthor());
        bookEntity.setRelease(Integer.valueOf(dataBook.getReleaseYear()));
        log.info("Saving book: {}", bookEntity);
        return lr.save(bookEntity);
    }
}
