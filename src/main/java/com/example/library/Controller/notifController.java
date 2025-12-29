package com.example.library.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.DTO.Request.RequestDataLoan;
import com.example.library.DTO.Response.coreResponse;
import com.example.library.Service.notifService;
import com.example.library.Service.key.generateKey;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class notifController {

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String formatDate = sdf.format(new Date());

    private final notifService notifService;

    @PostMapping("/send")
    public ResponseEntity<coreResponse> sendNotif(
            @RequestBody RequestDataLoan loan,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestHeader("X-timestamp") String timestamp) {
        String secret = "kunkunPedia";
        log.info("Received request to send notification with data: {}", loan);
        log.info("Authorization header: {}", authorizationHeader);


        try {
            // Extract the signature from "Bearer <signature>"
            String token = authorizationHeader.replace("Bearer ", "").trim();
            String expectedToken = generateKey.generateHmacToken(timestamp, secret);
            if (!expectedToken.equals(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return notifService.sendNotifikasi(loan);
    }
}
