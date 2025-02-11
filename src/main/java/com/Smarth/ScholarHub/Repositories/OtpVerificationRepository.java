
package com.Smarth.ScholarHub.Repositories;

import com.Smarth.ScholarHub.Models.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, UUID> {

    Optional<OtpVerification> findByEmail(String email);

}