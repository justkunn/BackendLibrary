package com.example.library.DTO.Response;

import lombok.Data;

@Data
public class ResponseDataLoan {
    private Integer idLoan;
    private Integer idBook;
    private Integer idMember;
    private String loanDate;
    private String dueDate;
    private String returnDate;
    private String status;
}