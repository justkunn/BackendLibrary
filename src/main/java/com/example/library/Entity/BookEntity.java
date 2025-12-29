package com.example.library.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book")
@Data


public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_book")    
    private Integer idBook;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "page")
    private String page;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "author")
    private String author;

    @Column(name = "release")
    private Integer release;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "cover", columnDefinition = "bytea")
    private byte[] cover;

    @Column(name = "cover_url")
    private String coverUrl;
}
