package com.example.alumni.controller;

import com.example.alumni.dto.UserLeaderboardDTO;
import com.example.alumni.model.User;
import com.example.alumni.model.UserPoints;
import com.example.alumni.service.UserPointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for frontend
public class UserPointsController {

    @Autowired
    private UserPointsService userPointsService;

    // ✅ Get user points by ID
    @GetMapping("/{id}/points")
    public ResponseEntity<Map<String, Integer>> getUserPoints(@PathVariable Long id) {
        int points = userPointsService.getPoints(id);
        Map<String, Integer> response = new HashMap<>();
        response.put("points", points);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<UserLeaderboardDTO>> getLeaderboard() {
        List<UserLeaderboardDTO> leaderboard = userPointsService.getLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }
    // @GetMapping("/leaderboard")
    // public ResponseEntity<List<User>> getLeaderboard() {
    //     List<User> leaderboard = userPointsService.getLeaderboard();
    //     return ResponseEntity.ok(leaderboard);
    // }
    // // ✅ Add points to a user
    // @PostMapping("/{id}/points")
    // public ResponseEntity<String> addUserPoints(@PathVariable Long id, @RequestParam int pointsToAdd) {
    //     userPointsService.addPoints(id, pointsToAdd);
    //     return ResponseEntity.ok("Added " + pointsToAdd + " points to user ID: " + id);
    // }
}
