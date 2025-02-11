
package com.Smarth.ScholarHub.Services;

import com.Smarth.ScholarHub.Models.OtpVerification;
import com.Smarth.ScholarHub.Models.User;
import com.Smarth.ScholarHub.DTOs.RegisterRequest;
import com.Smarth.ScholarHub.Repositories.OtpVerificationRepository;
import com.Smarth.ScholarHub.Repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OtpVerificationRepository otpRepository;


    public User userRegistration(RegisterRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName().trim());
        user.setEmail(registerRequest.getEmail().trim());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword().trim()));
        userRepository.save(user);
        return user;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public void userDeletion(String email) {
        if (userRepository.existsByEmail(email)) {
            User user = userRepository.findByEmail(email).orElse(new User());
            userRepository.deleteById(user.getId());
        } else {
            throw new UsernameNotFoundException("Account with email: " + email + " not found");
        }
    }

    public void sendOtp(String email) {
        // Generate a new 6-digit OTP
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusMinutes(2); // OTP valid for 2 minutes

        // Check if an OTP entry already exists for the email
        Optional<OtpVerification> existingOtp = otpRepository.findByEmail(email);

        OtpVerification otpEntry;// INSERT new OTP
        if (existingOtp.isPresent()) {
            // Update existing OTP entry
            otpEntry = existingOtp.get();
            otpEntry.setOtp(otp);
            otpEntry.setSentTime(now);
            otpEntry.setExpirationTime(expirationTime);
        } else {
            // Create new OTP entry
            otpEntry = new OtpVerification(email, otp, now, expirationTime);
        }
        otpRepository.save(otpEntry); // UPDATE existing OTP

        // Send OTP via email
        sendEmail(email, otp);
    }


    private void sendEmail(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Your OTP for Password Reset");
            helper.setText("Your OTP is: <b>" + otp + "</b>. It is valid for 2 minutes.", true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    public void verifyOtp(String email, String otp) {
        // Fetch the OTP entry from the database
        OtpVerification otpEntry = otpRepository.findByEmail(email).orElse(new OtpVerification());

        // Check if OTP is correct
        if (!otpEntry.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        // Check if OTP is expired
        if (otpEntry.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        // OTP is valid, allow password reset
        otpRepository.delete(otpEntry); // OTP used, remove it
    }

    public void resetPassword(String email, String newPass) {
        if (newPass.length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters long");
        }
        User user = userRepository.findByEmail(email).orElse(new User());
        user.setPassword(passwordEncoder.encode(newPass.trim()));
        userRepository.save(user);
    }

}