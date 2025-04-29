package com.example.alumni.controller;

import com.example.alumni.model.Achievement;
import com.example.alumni.model.WorkExperience;
import com.example.alumni.service.WorkExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.alumni.service.UserPointsService;

import java.util.List;

@RestController
@RequestMapping("/api/work-experience")
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for frontend
public class WorkExperienceController {
    @Autowired
    private UserPointsService userPointsService;

    @Autowired
    private WorkExperienceService workExperienceService;

    @PostMapping
    public WorkExperience addWorkExperience(@RequestBody WorkExperience workExperience) {
        userPointsService.addPoints(workExperience.getUser().getId(), 10);
        return workExperienceService.addWorkExperience(workExperience);
    }

    @GetMapping("/user/{userId}")
    public List<WorkExperience> getWorkExperiencesByUserId(@PathVariable Long userId) {
        return workExperienceService.getWorkExperiencesByUserId(userId);
    }

    @GetMapping
    public List<WorkExperience> getAllWorkExperiences() {
        return workExperienceService.getAllWorkExperiences();
    }

    @GetMapping("/{id}")
public ResponseEntity<WorkExperience> getWorkExperienceById(@PathVariable Long id) {
    WorkExperience workExperience = workExperienceService.getWorkExperienceById(id);
    return ResponseEntity.ok(workExperience);
}


    @PutMapping("/{id}")
public ResponseEntity<WorkExperience> updateWorkExperience(
        @PathVariable Long id,
        @RequestBody WorkExperience updatedExperience) {
    WorkExperience workExperience = workExperienceService.updateWorkExperience(id, updatedExperience);
    return ResponseEntity.ok(workExperience);
}


    @DeleteMapping("/{id}")
public ResponseEntity<Void> deleteWorkExperience(@PathVariable Long id) {
    WorkExperience wa = workExperienceService.getWorkExperienceById(id);
    userPointsService.addPoints(wa.getUser().getId(), -10);
    workExperienceService.deleteWorkExperience(id);
    return ResponseEntity.noContent().build();
}

}
