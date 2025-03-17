package com.Smarth.ScholarHub.Controllers;

import com.Smarth.ScholarHub.DTOs.MessageResponse;
import com.Smarth.ScholarHub.DTOs.SubjectRecord;
import com.Smarth.ScholarHub.DTOs.SubjectResponse;
import com.Smarth.ScholarHub.Services.SubjectNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subjectNew")
public class SubjectNewController {

    private final SubjectNewService subjectNewService;

    @Autowired
    public SubjectNewController(final SubjectNewService subjectNewService) {
        this.subjectNewService = subjectNewService;
    }

    @PostMapping("/add/{userId}/{subjectName}")
    public ResponseEntity<?> addSubject(@PathVariable("userId") UUID userId,
                                        @PathVariable("subjectName") String subjectName) {
        try {
            UUID id = subjectNewService.addSubject(userId, subjectName);
            return ResponseEntity.ok(new MessageResponse(id.toString()));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }

    @DeleteMapping("/delete/{subjectId}")
    public ResponseEntity<?> deleteSubject(@PathVariable UUID subjectId) {
        try {
            subjectNewService.deleteSubject(subjectId);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse("Subject deleted successfully"));
    }

    @GetMapping("/getAll/{userId}")
    public ResponseEntity<List<SubjectResponse>> getAllSubjects(@PathVariable("userId") UUID userId) {
        List<SubjectResponse> response = subjectNewService.getAllSubjects(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateSubject(@RequestBody List<SubjectRecord> subjectRecordList) {
        try {
            subjectNewService.updateSubject(subjectRecordList);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse("Subject updated successfully"));
    }

}