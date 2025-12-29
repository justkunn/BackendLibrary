package com.example.library.DTO.Book.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class RequestDataBook {
    private Integer bookId;
    private String bookName;
    private String page;
    private String publisher;
    private String author;

    @Min(value = 1)
    @Max(value = 9999)
    private Integer release;
    private Integer stock;

    @JsonProperty("cover_base64")
    private String coverBase64;

    @JsonProperty("cover_url")
    private String coverUrl;
}
