package com.example.library.DTO.Loan.Response;

import lombok.*;

@Data
public class CoreResponse {
    private String status;
    private String message;
    private ResponseDataLoan data;
    private Integer responseCode;
    private String timestamp;
}
