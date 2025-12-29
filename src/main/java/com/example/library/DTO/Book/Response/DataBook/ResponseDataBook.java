package com.example.library.DTO.Book.Response.DataBook;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data

public class ResponseDataBook {
    @JsonProperty("id_book")
    private Integer IdBook;
    @JsonProperty("book_name")
    private String BookName;
    @JsonProperty("total_page")
    private String TotalPage;
    @JsonProperty("publisher")
    private String Publisher;
    @JsonProperty("author")
    private String Author;
    @JsonProperty("release_year")
    private Integer ReleaseYear;
    @JsonProperty("stock")
    private Integer Stock;
    @JsonProperty("cover_base64")
    private String CoverBase64;
    @JsonProperty("cover")
    private String CoverUrl;
}
