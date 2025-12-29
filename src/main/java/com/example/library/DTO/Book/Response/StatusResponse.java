package com.example.library.DTO.Book.Response;

import com.example.library.DTO.Book.Response.DataBook.ResponseDataBook;

import lombok.*;

@Data
public class StatusResponse {
    private String status;
    private String message;
    private ResponseDataBook data;
    private String ResponseCode;
    private String timestamp;
}
