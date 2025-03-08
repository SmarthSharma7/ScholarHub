
package com.Smarth.ScholarHub.Controllers;

import com.Smarth.ScholarHub.DTOs.MessageResponse;
import com.Smarth.ScholarHub.DTOs.SearchResponse;
import com.Smarth.ScholarHub.DTOs.TeamUpProfileRequest;
import com.Smarth.ScholarHub.DTOs.TeamUpProfileResponse;
import com.Smarth.ScholarHub.Services.TeamUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teamUp")
public class TeamUpController {

    private final TeamUpService teamUpService;

    @Autowired
    public TeamUpController(final TeamUpService teamUpService) {
        this.teamUpService = teamUpService;
    }

    @PutMapping("/addTeamUpProfile")
    public ResponseEntity<?> addTeamUpProfile(@RequestBody TeamUpProfileRequest teamUpProfileRequest) {
        TeamUpProfileResponse teamUpProfileResponse;
        try {
            teamUpProfileResponse = teamUpService.addTeamUpProfile(teamUpProfileRequest);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(teamUpProfileResponse);
    }

    @GetMapping("/getTeamUpProfile/{userId}")
    public ResponseEntity<?> getTeamUpProfile(@PathVariable("userId") UUID userId) {
        TeamUpProfileResponse teamUpProfileResponse;
        try {
            teamUpProfileResponse = teamUpService.getTeamUpProfile(userId);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(teamUpProfileResponse);
    }
    @PutMapping("/updateTeamUpProfile")
    public ResponseEntity<?> updateTeamUpProfile(@RequestBody TeamUpProfileRequest teamUpProfileRequest) {
        TeamUpProfileResponse teamUpProfileResponse;
        try {
            teamUpProfileResponse = teamUpService.updateTeamUpProfile(teamUpProfileRequest);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(teamUpProfileResponse);
    }

    @DeleteMapping("/deleteTeamUpProfile/{userId}")
    public ResponseEntity<?> deleteTeamUpProfile(@PathVariable("userId") UUID userId) {
        try {
            teamUpService.deleteTeamUpProfile(userId);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse("Profile deleted successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchResponse>> searchUsers(@RequestParam String query) {
        List<SearchResponse> users = teamUpService.searchUsers(query);
        return ResponseEntity.ok(users);
    }

}