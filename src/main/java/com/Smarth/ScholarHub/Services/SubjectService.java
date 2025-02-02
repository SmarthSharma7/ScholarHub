
package com.Smarth.ScholarHub.Services;

import com.Smarth.ScholarHub.Models.Subject;
import com.Smarth.ScholarHub.Models.User;
import com.Smarth.ScholarHub.Repositories.SubjectRepository;
import com.Smarth.ScholarHub.Repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager; // To refresh entity after DB update

    public Subject addSubject(UUID userId, Subject subject) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
        subject.setUser(user);
        return subjectRepository.save(subject);
    }

    public List<Subject> getSubjectsByUserId(UUID userId) {
        return subjectRepository.findByUserId(userId);
    }

    @Transactional
    public Subject updateAttendance(UUID subjectId, boolean isPresent) {
        Subject subject = subjectRepository.findById(subjectId).
                                            orElseThrow(() -> new RuntimeException("Subject not found"));
        subject.setTotalClasses(subject.getTotalClasses() + 1); // Always increment total classes
        if (isPresent) {
            subject.setAttendedClasses(subject.getAttendedClasses() + 1); // Increment attended if present
        }
        subjectRepository.save(subject);
        entityManager.refresh(subject); // Refresh to get updated attendance_percentage from DB
        return subject;
    }

}