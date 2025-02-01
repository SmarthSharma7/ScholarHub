
package com.Smarth.ScholarHub.Services;

import com.Smarth.ScholarHub.Models.User;
import com.Smarth.ScholarHub.DTO.RegisterRequest;
import com.Smarth.ScholarHub.Repositories.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDetailsRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void userRegistration(RegisterRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

}