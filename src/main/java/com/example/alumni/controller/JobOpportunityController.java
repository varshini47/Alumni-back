package com.example.alumni.controller;

import com.example.alumni.model.JobOpportunity;
import com.example.alumni.service.JobOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.alumni.service.UserPointsService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:3000")
public class JobOpportunityController {
    @Autowired
    private UserPointsService userPointsService;

    @Autowired
    private JobOpportunityService jobJobOpportunityService;

    @GetMapping
    public List<JobOpportunity> getAllJobOpportunitys() {
        return jobJobOpportunityService.getAllJobOpportunitys();
    }

    @PostMapping
    public JobOpportunity addJobOpportunity(@RequestBody JobOpportunity jobJobOpportunity) {
        System.out.println("\n\nEntered Jobop\n\n");
        userPointsService.addPoints(jobJobOpportunity.getUserId(), 10);
        return jobJobOpportunityService.addJobOpportunity(jobJobOpportunity);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        JobOpportunity job = jobJobOpportunityService.getJobById(id);
        
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
    
        // Check if the application deadline has passed
        if (job.getApplicationDeadline().isBefore(LocalDate.now())) {
            userPointsService.addPoints(job.getUserId(), -10);
        }
    
        jobJobOpportunityService.deleteJobOpportunity(id);
        return ResponseEntity.ok().body("Job deleted successfully");
    }


}
