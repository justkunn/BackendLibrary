package com.example.library.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.DTO.Loan.Request.RequestDataLoan;
import com.example.library.DTO.Loan.Response.CoreResponse;
import com.example.library.Services.LoanService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/loan")
@AllArgsConstructor

public class LoanController {
    private final LoanService loanService;

    @PostMapping("/add")
    public ResponseEntity<CoreResponse> addLoan(@RequestBody @Valid RequestDataLoan loan) {
        log.info("Adding Loan : {}", loan);

        ResponseEntity<CoreResponse> addLoan = loanService.addLoan(loan);

        return addLoan;
    }

    @PutMapping("/edit/{idLoan}")
    public ResponseEntity<CoreResponse> editLoan(@PathVariable @Valid Integer idLoan, @RequestBody RequestDataLoan loan) {
        log.info("preparing Loan With ID : {}", idLoan);

        ResponseEntity<CoreResponse> editLoan = loanService.editLoan(idLoan, loan);

        return editLoan;
    }

    @GetMapping("/get/{idLoan}")
    public ResponseEntity<CoreResponse> getLoan(@PathVariable Integer idLoan) {
        log.info("preparing data loan with ID : {}", idLoan);

        ResponseEntity<CoreResponse> getLoan = loanService.getLoan(idLoan);

        return getLoan;
    }

    @DeleteMapping("/delete/{idLoan}")
    public ResponseEntity<CoreResponse> deleteLoan(@PathVariable Integer idLoan) {
        log.info("preparing to delete loan with ID : {}", idLoan);

        ResponseEntity<CoreResponse> deleteLoan = loanService.deleteLoan(idLoan);

        return deleteLoan;
    }
}
