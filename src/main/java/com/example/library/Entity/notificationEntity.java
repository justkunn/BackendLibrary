package com.example.library.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification")
@Data
public class notificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    private Integer idNotification;

    @Column(name = "to_email")
    private String toEmail;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body")
    private String body;

    @Column(name = "status")
    private String status;

    @Column(name = "timestamp")
    private String timestamp;
}
