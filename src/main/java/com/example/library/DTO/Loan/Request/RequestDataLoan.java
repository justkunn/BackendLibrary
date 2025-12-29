package com.example.library.DTO.Loan.Request;

import lombok.*;

@Data
public class RequestDataLoan {

    private Integer idBook;

    private Integer idMember;

    private String loanDate;

    private String dueDate;

    private String returnDate;
}