package com.example.library.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "loan")
@Data
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_loan")
    private Integer idLoan;

    @ManyToOne
    @JoinColumn(name = "id_book", referencedColumnName = "id_book")
    private BookEntity book;

    @ManyToOne
    @JoinColumn(name = "id_member", referencedColumnName = "id_member")
    private MemberEntity member;

    @Column(name = "loan_date")
    private String loanDate;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "return_date")
    private String returnDate;

}
