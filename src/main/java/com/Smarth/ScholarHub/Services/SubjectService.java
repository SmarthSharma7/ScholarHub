
package com.Smarth.ScholarHub.Services;

import com.Smarth.ScholarHub.DTOs.SubjectResponse;
import com.Smarth.ScholarHub.Models.Subject;
import com.Smarth.ScholarHub.Models.User;
import com.Smarth.ScholarHub.Repositories.SubjectRepository;
import com.Smarth.ScholarHub.Repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public SubjectResponse addSubject(UUID userId, Subject subject) {
        User user = userRepository.findById(userId).orElse(new User());
        subject.setUser(user);
        subject.setName(subject.getName().trim());
        subjectRepository.save(subject);
        SubjectResponse subjectResponse = new SubjectResponse();
        subjectResponse.setId(subject.getId());
        subjectResponse.setName(subject.getName());
        subjectResponse.setTotalClasses(subject.getTotalClasses());
        subjectResponse.setAttendedClasses(subject.getAttendedClasses());
        if (subjectResponse.getTotalClasses() != 0) {
            subjectResponse.setAttendedPercentage(((double) subject.getAttendedClasses() / subject.getTotalClasses()) * 100);
        } else {
            subjectResponse.setAttendedPercentage(100);
        }
        subjectResponse.setCreatedAt(subject.getCreatedAt());
        return subjectResponse;
    }

    public List<SubjectResponse> getSubjectsByUserId(UUID userId) {
        List<Subject> list = subjectRepository.findByUserId(userId);
        if (list.isEmpty()) {
            List<SubjectResponse> list1 = new ArrayList<>();
            SubjectResponse subjectResponse = new SubjectResponse();
            subjectResponse.setName("THe LIst IS EMpty");
            list1.add(subjectResponse);
            return list1;
        }
        SubjectResponse subjectResponse = new SubjectResponse();
        List<SubjectResponse> listOfSubjects = new ArrayList<>();
        for (Subject subject : list) {
            subjectResponse.setId(subject.getId());
            subjectResponse.setName(subject.getName());
            subjectResponse.setTotalClasses(subject.getTotalClasses());
            subjectResponse.setAttendedClasses(subject.getAttendedClasses());
            subjectResponse.setAttendedPercentage(((double) subject.getAttendedClasses() / subject.getTotalClasses()) * 100);
            subjectResponse.setCreatedAt(subject.getCreatedAt());
            listOfSubjects.add(subjectResponse);
        }
        return listOfSubjects;
    }

    @Transactional
    public SubjectResponse updateSubject(Subject subject) {
        subject.setName(subject.getName().trim());
        subjectRepository.save(subject);
        SubjectResponse subjectResponse = new SubjectResponse();
        subjectResponse.setId(subject.getId());
        subjectResponse.setName(subject.getName());
        subjectResponse.setTotalClasses(subject.getTotalClasses());
        subjectResponse.setAttendedClasses(subject.getAttendedClasses());
        if (subjectResponse.getTotalClasses() != 0) {
            subjectResponse.setAttendedPercentage(((double) subject.getAttendedClasses() / subject.getTotalClasses()) * 100);
        } else {
            subjectResponse.setAttendedPercentage(100);
        }
        return subjectResponse;
    }

    public void deleteSubject(UUID subjectId) {
        Subject subject = subjectRepository.findById(subjectId).
                orElseThrow(() -> new RuntimeException("Subject not found"));
        subjectRepository.delete(subject);
    }

}