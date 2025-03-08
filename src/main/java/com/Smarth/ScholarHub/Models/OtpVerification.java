package com.Smarth.ScholarHub.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "otp_verification")
@Data
@NoArgsConstructor
public class OtpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private LocalDateTime sentTime;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    public OtpVerification(String email, String otp, LocalDateTime sentTime, LocalDateTime expirationTime) {
        this.email = email;
        this.otp = otp;
        this.sentTime = sentTime;
        this.expirationTime = expirationTime;
    }

}