package com.example.library.DTO.Book.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class RequestDataBook {
    @JsonProperty("id_book")
    private Integer bookId;

    @JsonProperty("book_name")
    private String bookName;

    @JsonProperty("total_page")
    private String page;

    @JsonProperty("publisher")
    private String publisher;

    @JsonProperty("author")
    private String author;

    @JsonProperty("release_year")
    @Min(value = 1)
    @Max(value = 9999)
    private Integer release;

    @JsonProperty("stock")
    private Integer stock;

    @JsonProperty("cover_base64")
    private String coverBase64;

    @JsonProperty("cover_url")
    private String coverUrl;

    @JsonProperty("cover")
    private String cover;
}
