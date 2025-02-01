
package com.Smarth.ScholarHub.Controllers;

import com.Smarth.ScholarHub.DTO.LoginRequest;
import com.Smarth.ScholarHub.Models.User;
import com.Smarth.ScholarHub.DTO.RegisterRequest;
import com.Smarth.ScholarHub.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        userService.userRegistration(registerRequest);
        return ResponseEntity.ok("User registered successfully.");
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
        User user = userService.findByEmail(loginRequest.getEmail());

        // Check if password matches
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            // You can return a response or start a session here (e.g., using HttpSession or JWT)
            return ResponseEntity.ok("Login successful.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
        }
    }

}