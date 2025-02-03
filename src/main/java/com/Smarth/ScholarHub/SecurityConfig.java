
package com.Smarth.ScholarHub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // Important: Mark this class as a configuration class
public class SecurityConfig { // Or any other configuration class

    @Bean  // This annotation makes the method return a bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}