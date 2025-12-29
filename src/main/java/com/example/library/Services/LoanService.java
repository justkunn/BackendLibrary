package com.example.library.Services;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.library.DTO.Book.Response.DataBook.ResponseDataBook;
import com.example.library.DTO.Loan.Request.RequestDataLoan;
import com.example.library.DTO.Loan.Response.CoreResponse;
import com.example.library.DTO.Loan.Response.ResponseDataLoan;
import com.example.library.DTO.Member.Response.ResponseDataMember;
import com.example.library.Entity.BookEntity;
import com.example.library.Entity.LoanEntity;
import com.example.library.Entity.MemberEntity;
import com.example.library.Repository.BookRepository;
import com.example.library.Repository.LoanRepository;
import com.example.library.Repository.MemberRepository;
import com.example.library.Services.key.generateKey;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    private static final String URL = "http://localhost:2112/api/v1/notification/send";

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String formatDate = sdf.format(new Date());

    private final RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory());
    private final String SECRET = "kunkunPedia";

    String signature = generateKey.generateHmacToken(formatDate, SECRET);
    
    public ResponseEntity<CoreResponse> addLoan(RequestDataLoan loanRequest) {
        log.info("Generated signature: {}", signature);
        log.info("preparing to add loan with data : {}", loanRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + signature);
        headers.set("X-timestamp", formatDate); 
        

        try {
            log.info("preparing to map RequestDataLoan to LoanEntity");
            LoanEntity loanEntity = new LoanEntity();
            LoanEntity loan = mapToEntity(loanEntity, loanRequest);
            log.info("mapping RequestDataLoan to LoanEntity : {}", loanEntity);

            BookEntity bookValidate = bookRepository.findById(loanRequest.getIdBook()).orElse(null);
            if (bookValidate == null) {
                String msg = "Book not found with ID :" + loanRequest.getIdBook();
                CoreResponse errResponse = mapToResponse(null, HttpStatus.BAD_REQUEST, msg);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
            }

            MemberEntity memberValidate = memberRepository.findByIdMember(loanRequest.getIdMember()).orElse(null);
            if (memberValidate == null) {
                String msg = "Member not registred for id : " + loanRequest.getIdMember();
                CoreResponse errResponse = mapToResponse(null, HttpStatus.BAD_REQUEST, msg);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
            }

            BookEntity bookEntity = loan.getBook();
            if (bookEntity.getStock() == null || bookEntity.getStock() <= 0) {
                String Message = "Book stock is empty";
                CoreResponse eResponse = mapToResponse(null, HttpStatus.BAD_REQUEST, Message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(eResponse);
            }
            // set minus 1 to stock
            bookEntity.setStock(bookEntity.getStock() - 1);
            bookRepository.save(bookEntity);

            LoanEntity savedEntity = loanRepository.save(loanEntity);
            log.info("set to database : {}", savedEntity);

            String msg = "Add has successfully with ID : " + savedEntity.getIdLoan();
            CoreResponse responseDataLoan = mapToResponse(savedEntity, HttpStatus.OK, msg);
            log.info("mapping LoanEntity to CoreResponse : {}", responseDataLoan);
            ResponseDataLoan data = (ResponseDataLoan) responseDataLoan.getData();

            try {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(data);

                log.info("ini json : {}", json);
                log.info("ini url : {}", URL);
                log.info("ini headers : {}", headers);
                HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
                log.info("ini entity : {}", requestEntity);
                restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);
            } catch (Exception e) {
                log.error("Failed to send notification: {}", e.getMessage());
                String msgError = "Loan added but failed to send notification";
                CoreResponse errorResponse = mapToResponse(savedEntity, HttpStatus.INTERNAL_SERVER_ERROR, msgError);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
            return ResponseEntity.ok(responseDataLoan);

        } catch (IllegalArgumentException err) {
            log.error("An error occurred while adding the loan: {}", err.getMessage());

            BookEntity errBook = bookRepository.findByIdBook(loanRequest.getIdBook()).orElse(null);
            if (errBook == null) {
                String message = "Book not found " + loanRequest.getIdBook();

                CoreResponse errorResponse = mapToResponse(null, HttpStatus.BAD_REQUEST, message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            MemberEntity errMember = memberRepository.findByIdMember(loanRequest.getIdMember()).orElse(null);
            if (errMember == null) {
                String message = "Member not Registered " + loanRequest.getIdMember();

                CoreResponse errResponse = mapToResponse(null, HttpStatus.BAD_REQUEST, message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
            }

            String msg = "Loan failed to add";
            CoreResponse errorResponse = mapToResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, msg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    public ResponseEntity<CoreResponse> editLoan(Integer idLoan, RequestDataLoan loan) {
        log.info("preparing to edit loan with ID : {}", idLoan);

        try {
            LoanEntity loanEntity = loanRepository.findByIdLoan(idLoan).orElse(null);

            BookEntity bookValidate = bookRepository.findById(loan.getIdBook()).orElse(null);
            if (bookValidate == null) {
                String msg = "Book not found with ID :" + loan.getIdBook();
                CoreResponse errResponse = mapToResponse(null, HttpStatus.BAD_REQUEST, msg);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
            }

            MemberEntity memberValidate = memberRepository.findByIdMember(loan.getIdMember()).orElse(null);
            if (memberValidate == null) {
                String msg = "Member not registred for id : " + loan.getIdMember();
                CoreResponse errResponse = mapToResponse(null, HttpStatus.BAD_REQUEST, msg);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
            }

            if (bookValidate.getStock() == null || bookValidate.getStock() <= 0 ) {
                String msg = "The requested quantity exceeds the available book stock.";

                CoreResponse nValidate = mapToResponse(null, HttpStatus.BAD_REQUEST, msg);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(nValidate);
            }

            mapToEntity(loanEntity, loan);
            loanRepository.save(loanEntity);

            String msg = "loan has edited for ID : " + idLoan;

            CoreResponse response = mapToResponse(loanEntity, HttpStatus.OK, msg);
            log.info("loan has updated");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException err) {
            log.error("An error occurred while adding the loan: {}", err.getMessage());

            BookEntity errBook = bookRepository.findByIdBook(loan.getIdBook()).orElse(null);
            if (errBook == null) {
                String message = "Book not found " + loan.getIdBook();

                CoreResponse errorResponse = mapToResponse(null, HttpStatus.BAD_REQUEST, message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            MemberEntity errMember = memberRepository.findByIdMember(loan.getIdMember()).orElse(null);
            if (errMember == null) {
                String message = "Member not Registered " + loan.getIdMember();

                CoreResponse errResponse = mapToResponse(null, HttpStatus.BAD_REQUEST, message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
            }

            String msg = "Loan failed to update";
            CoreResponse errorResponse = mapToResponse(null, HttpStatus.BAD_REQUEST, msg);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    public ResponseEntity<CoreResponse> getLoan(Integer idLoan) {
        log.info("preparing to get loan with ID : {}", idLoan);

        // get from database
        Optional<LoanEntity> getloan = loanRepository.findByIdLoan(idLoan);
        log.info("data loan with ID : {}", idLoan);
        if (getloan.isPresent()) {
            LoanEntity loanEntity = getloan.get();
            String msg = "Loan has showed with ID : " + idLoan;
            CoreResponse succResponse = mapToResponse(loanEntity, HttpStatus.OK, msg);

            return ResponseEntity.ok(succResponse);
        } else {
            log.warn("Loan not found with ID : {} ", idLoan);
            String msg = "Loan not Found";
            CoreResponse errResponse = mapToResponse(null, HttpStatus.BAD_REQUEST, msg);
            return ResponseEntity.ok(errResponse);
        }
    }

    public ResponseEntity<CoreResponse> deleteLoan(Integer idLoan) {
        log.info("Preparing to delete loan with ID : {}", idLoan);

        //get from database
        Optional<LoanEntity> deleteLoan = loanRepository.findByIdLoan(idLoan);
        log.info("get data loan with ID : {}", idLoan);

        if (deleteLoan.isPresent()){
            LoanEntity loanEntity = deleteLoan.get();
            loanRepository.delete(loanEntity);
            String msg = "Loan deleted for ID : " + idLoan;

            CoreResponse response = mapToResponse(loanEntity, HttpStatus.OK, msg);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            log.info("loan with ID {} not found", idLoan);

            String msg = "Loan not found for ID : " + idLoan;
            CoreResponse errResponse = mapToResponse(null, HttpStatus.BAD_REQUEST, msg);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
        }
    }

    public LoanEntity mapToEntity(LoanEntity loanEntity, RequestDataLoan loan) {
        log.info("preparing inset to database with data : {}", loan);
        BookEntity bookEntity = bookRepository.findByIdBook(loan.getIdBook()).orElse(null);
        MemberEntity memberEntity = memberRepository.findByIdMember(loan.getIdMember()).orElse(null);

        loanEntity.setBook(bookEntity);
        loanEntity.setMember(memberEntity);
        loanEntity.setLoanDate(loan.getLoanDate());
        loanEntity.setDueDate(loan.getDueDate());
        loanEntity.setReturnDate(loan.getReturnDate());

        log.info("set to database : {}", loanEntity);
        return loanEntity;
    }

    public CoreResponse mapToResponse(LoanEntity loanEntity, HttpStatus status, String msg) {
        log.info("preparing to map LoanEntity to CoreResponse");

        CoreResponse response = new CoreResponse();

        if (status == HttpStatus.OK) {
            response.setStatus("Success");
            response.setMessage(msg != null ? msg : "Loan added successfully");
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            response.setStatus("Internal Server Error");
            response.setMessage(msg != null ? msg : "Loan failed to execute");
        } else if (status == HttpStatus.BAD_REQUEST) {
            response.setStatus("Bad Request");
            response.setMessage(msg != null ? msg : "Loan failed to execute");
        } else {
            response.setStatus("Unknown Error");
            response.setMessage(msg != null ? msg : "Loan failed to execute");
        }
        response.setResponseCode(status.value());

        if (loanEntity != null) {
            ResponseDataLoan data = new ResponseDataLoan();
            data.setIdLoan(loanEntity.getIdLoan());
            data.setLoanDate(loanEntity.getLoanDate());
            data.setDueDate(loanEntity.getDueDate());
            data.setReturnDate(loanEntity.getReturnDate());
            data.setTimesTamp(formatDate);

            // set the book data
            BookEntity bookEntity = loanEntity.getBook();
            if (bookEntity != null) {
                ResponseDataBook bookData = new ResponseDataBook();
                bookData.setIdBook(bookEntity.getIdBook());
                bookData.setBookName(bookEntity.getBookName());
                bookData.setTotalPage(bookEntity.getPage());
                bookData.setPublisher(bookEntity.getPublisher());
                bookData.setAuthor(bookEntity.getAuthor());
                bookData.setReleaseYear(bookEntity.getRelease());
                if (bookEntity.getStock() != null) {
                    bookData.setStock(bookEntity.getStock());
                } else {
                    bookData.setStock(null);
                }
                data.setBook(bookData);
            } else {
                data.setBook(null);
            }

            // set the member data
            MemberEntity memberEntity = loanEntity.getMember();
            if (memberEntity != null) {
                ResponseDataMember memberData = new ResponseDataMember();
                memberData.setIdMember(memberEntity.getIdMember());
                memberData.setMemberName(memberEntity.getMemberName());
                memberData.setAge(memberEntity.getAge());
                memberData.setPhoneNumber(memberEntity.getPhoneNumber());
                memberData.setEmail(memberEntity.getEmail());
                memberData.setLevel(memberEntity.getLevel());
                data.setMember(memberData);
            } else {
                data.setMember(null);
            }

            // set the data to response

            response.setData(data);
        } else {
            response.setData(null);
        }

        response.setTimestamp(formatDate);

        return response;
    }
}
