
package com.Smarth.ScholarHub.Repositories;

import com.Smarth.ScholarHub.Models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {

    List<Subject> findByUserId(UUID userId);// Get all subjects for a user

}