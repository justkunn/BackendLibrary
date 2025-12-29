package com.example.library.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.library.DTO.Request.RequestDataLoan;
import com.example.library.DTO.Response.ResponseDataLoan;
import com.example.library.DTO.Response.coreResponse;
import com.example.library.Entity.notificationEntity;
import com.example.library.Repository.NotificationRepository;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class notifService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String formatDate = sdf.format(new Date());

    public ResponseEntity<coreResponse> sendNotifikasi(RequestDataLoan notif) {
        log.info("Preparing to send notification with data: {}", notif);

        try {
            // set to database
            notificationEntity notificationEntity = new notificationEntity();
            notificationEntity savedNotif = mapToEntity(notificationEntity, notif);

            notificationRepository.save(savedNotif);

            // sending to email

            sendMail(notif);

            // set respponse
            coreResponse response = mapToResponse(notificationEntity, HttpStatus.OK, "Notificaton has been sent",
                    notif);
            log.info("notofication sent successfully with data : {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error occurred while sending notification: {}", e.getMessage());
            coreResponse response = mapToResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send notification",
                    notif);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public notificationEntity mapToEntity(notificationEntity notification, RequestDataLoan loan) {
        log.info("Preparing to map request data to entity");

        notification.setToEmail(loan.getMember().getEmail());
        notification.setSubject("Bukti Peminjaman Buku " + loan.getBook().getBookName() + " Perpustakaan Digital");
        notification.setBody("Peminjaman buku untuk " + loan.getMember().getEmail()
                + ": Buku " + loan.getBook().getBookName() + ", ID Loan:" + loan.getIdLoan()//
        );
        notification.setStatus("On loan");
        notification.setTimestamp(formatDate);
        return notification;
    }

    public coreResponse mapToResponse(notificationEntity notification, HttpStatus status, String message,
            RequestDataLoan loan) {
        log.info("Preparing set the response data");

        coreResponse response = new coreResponse();

        if (status == HttpStatus.OK) {
            response.setMessage(message);
            response.setStatus("success");
            response.setResponseCode("200");
        } else if (status == HttpStatus.BAD_REQUEST) {
            response.setDataLoan(null);
            response.setMessage(message);
            response.setStatus("failed");
            response.setResponseCode("400");
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            response.setDataLoan(null);
            response.setMessage(message);
            response.setStatus("error");
            response.setResponseCode("500");
        } else {
            response.setDataLoan(null);
            response.setMessage("Unexpected error occurred");
            response.setStatus("error");
            response.setResponseCode("500");
        }

        if (notification != null) {
            ResponseDataLoan dataLoan = new ResponseDataLoan();
            dataLoan.setIdLoan(loan.getIdLoan());
            dataLoan.setIdBook(loan.getBook().getIdBook());
            dataLoan.setIdMember(loan.getMember().getIdMember());
            dataLoan.setLoanDate(loan.getLoanDate());
            dataLoan.setDueDate(loan.getDueDate());
            dataLoan.setReturnDate(loan.getReturnDate());
            dataLoan.setStatus(notification.getStatus());
            response.setDataLoan(dataLoan);
        } else {
            response.setDataLoan(null);
        }

        response.setTimestamp(formatDate);

        return response;

    }

    public void sendMail(RequestDataLoan loan) {
        // send email logic here

        log.info("Preparing to send email notification");

        //
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            
            helper.setTo(loan.getMember().getEmail());
            helper.setSubject("Bukti Peminjaman Buku " + loan.getBook().getBookName() + " Perpustakaan Digital");
            helper.setText("Halo " + loan.getMember().getMemberName() + ",\n\n" +
                    "Terima kasih telah melakukan peminjaman buku melalui Perpustakaan Digital kami.\n\n" +
                    "📚 Judul Buku          : " + loan.getBook().getBookName() + "\n" +
                    "📅 Tanggal Pinjam      : " + loan.getLoanDate() + "\n" +
                    "📅 Batas Pengembalian  : " + loan.getDueDate() + "\n\n" +
                    "Mohon pastikan buku dikembalikan sebelum tanggal jatuh tempo untuk menghindari denda keterlambatan.\n\n"
                    +
                    "Jika Anda memiliki pertanyaan, silakan hubungi admin perpustakaan.\n\n" +
                    "Salam hangat,\nTim Perpustakaan Digital");

            mailSender.send(message);
            log.info("Email notification sent successfully to {}", loan.getMember().getMemberName());
        } catch (Exception e) {
            log.error("Failed to send email notification: {}", e.getMessage());
        }
    }
}