package com.example.library.DTO.Book.Response;

import java.util.List;

import com.example.library.DTO.Book.Response.DataBook.ResponseDataBook;

import lombok.Data;

@Data
public class StatusResponseList {
    private String status;
    private String message;
    private List<ResponseDataBook> data;
    private String responseCode;
    private String timestamp;
}
