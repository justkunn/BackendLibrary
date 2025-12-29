package com.example.library.DTO.Response;

import lombok.Data;

@Data
public class coreResponse {
    private String message;
    private String status;
    private ResponseDataLoan dataLoan;
    private String responseCode;
    private String timestamp;
}

