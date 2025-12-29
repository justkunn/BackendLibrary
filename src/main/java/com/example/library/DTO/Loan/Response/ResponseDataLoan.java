package com.example.library.DTO.Loan.Response;

import com.example.library.DTO.Book.Response.DataBook.ResponseDataBook;
import com.example.library.DTO.Member.Response.ResponseDataMember;

import lombok.*;

@Data
public class ResponseDataLoan {
    private Integer idLoan;
    private ResponseDataBook Book;
    private ResponseDataMember Member;
    private String loanDate;
    private String dueDate;
    private String returnDate;
    private String timesTamp;
}