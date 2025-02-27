package com.Smarth.ScholarHub.Controllers;

import com.Smarth.ScholarHub.DTOs.SubjectResponse;
import com.Smarth.ScholarHub.Models.Subject;
import com.Smarth.ScholarHub.Services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addSubject(@PathVariable UUID userId,
                                        @RequestBody Subject subject) {
        SubjectResponse subjectResponse;
        try {
            subjectResponse = subjectService.addSubject(userId, subject);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
        return ResponseEntity.ok(subjectResponse);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getSubjects(@PathVariable UUID userId) {
        List<SubjectResponse> listOfSubjects;
        try {
            listOfSubjects = subjectService.getSubjectsByUserId(userId);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
        return ResponseEntity.ok(listOfSubjects);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAttendance(@RequestBody Subject subject) {
        SubjectResponse subjectResponse;
        try {
            subjectResponse = subjectService.updateSubject(subject);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
        return ResponseEntity.ok(subjectResponse);
    }

    @DeleteMapping("/delete/{subjectId}")
    public ResponseEntity<?> deleteSubject(@PathVariable UUID subjectId) {
        try {
            subjectService.deleteSubject(subjectId);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
        return ResponseEntity.ok("{\"message\": \"Subject deleted successfully\"}");
    }

}