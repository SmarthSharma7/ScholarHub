
package com.Smarth.ScholarHub.Controllers;

import com.Smarth.ScholarHub.Models.Subject;
import com.Smarth.ScholarHub.Services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        Subject tempSubject;
        try {
            tempSubject = subjectService.addSubject(userId, subject);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist");
        }
        return ResponseEntity.ok(tempSubject);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<List<Subject>> getSubjects(@PathVariable UUID userId) {
        return ResponseEntity.ok(subjectService.getSubjectsByUserId(userId));
    }

    @PutMapping("/update/{subjectId}/{isPresent}")
    public ResponseEntity<?> updateAttendance(@PathVariable(value = "subjectId") UUID subjectId,
                                              @PathVariable(value = "isPresent") boolean isPresent) {
        Subject subject;
        try {
            subject = subjectService.updateAttendance(subjectId, isPresent);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subject not found");
        }
        return ResponseEntity.ok(subject);
    }

}