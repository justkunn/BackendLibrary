package com.example.library.DTO.Request;

import com.example.library.DTO.Response.loan.ResponseDataBook;
import com.example.library.DTO.Response.loan.ResponseDataMember;

import lombok.Data;

@Data
public class RequestDataLoan {
    private Integer idLoan;
    private ResponseDataBook Book;
    private ResponseDataMember Member;
    private String loanDate;
    private String dueDate;
    private String returnDate;
    private String timesTamp;
}
