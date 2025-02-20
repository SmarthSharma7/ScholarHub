
package com.Smarth.ScholarHub.Controllers;

import com.Smarth.ScholarHub.DTOs.LoginRequest;
import com.Smarth.ScholarHub.DTOs.UpdateProfileRequest;
import com.Smarth.ScholarHub.DTOs.VerifyOtpRequest;
import com.Smarth.ScholarHub.Models.User;
import com.Smarth.ScholarHub.DTOs.RegisterRequest;
import com.Smarth.ScholarHub.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> userRegistration(@Valid @RequestBody RegisterRequest registerRequest,
                                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errors.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.badRequest().body(errors.toString());
        }

        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use.");
        }

        return ResponseEntity.ok(userService.userRegistration(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequest loginRequest,
                                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errors.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.badRequest().body(errors.toString());
        }

        // Find user by email
        User user;
        try {
            user = userService.findByEmail(loginRequest.getEmail());
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }

        // Check if password matches
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            // You can return a response or start a session here (e.g., using HttpSession or JWT)
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid password\"}");
        }
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> userDeletion(@PathVariable String email) {
        try {
            userService.userDeletion(email);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.badRequest().body("Account not found");
        }
        return ResponseEntity.ok("{\"message\": \"Account deleted successfully\"}");

    }

    @PostMapping("/send-otp/{email}")
    public ResponseEntity<?> sendOtp(@PathVariable String email) {
        try {
            userService.sendOtp(email);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
        return ResponseEntity.ok("{\"message\": \"OTP sent successfully\"}");
    }

    @PostMapping("/verify-otp/{email}")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest, @PathVariable String email) {
        try {
            userService.verifyOtp(email, verifyOtpRequest.getOtp());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
        return ResponseEntity.ok("{\"message\": \"OTP verified successfully\"}");
    }

    @PutMapping("/reset-pass/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable String email, @RequestBody VerifyOtpRequest verifyOtpRequest) {
        try {
            userService.resetPassword(email, verifyOtpRequest.getOtp());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
        return ResponseEntity.ok("{\"message\": \"Password changed successfully\"}");
    }

    @PutMapping("/update-profile/{pass}")
    public ResponseEntity<?> updateProfile(@PathVariable String pass, @RequestBody UpdateProfileRequest updateProfileRequest) {
        try {
            userService.updateProfile(pass, updateProfileRequest);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
        return ResponseEntity.ok("{\"message\": \"Profile updated successfully\"}");
    }

}