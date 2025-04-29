package com.example.alumni.service;
import com.example.alumni.model.WorkExperience;
import com.example.alumni.model.User;
import com.example.alumni.repository.WorkExperienceRepository;
import com.example.alumni.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkExperienceService {

    @Autowired
    private WorkExperienceRepository workExperienceRepository;

    @Autowired
    private UserRepository userRepository; // Inject UserRepository to fetch user by ID

    // public List<WorkExperience> getAllWorkExperiences() {
    //     return workExperienceRepository.findAll();
    // }

    public WorkExperience addWorkExperience(WorkExperience workExperience) {
        return workExperienceRepository.save(workExperience);
    }

    public List<WorkExperience> getWorkExperiencesByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        return workExperienceRepository.findByUser(user); // Fetch work experiences based on User object
    }
    public List<WorkExperience> getAllWorkExperiences() {
        return workExperienceRepository.findAllWithUser();
    }
    public void deleteWorkExperience(Long id) {
        workExperienceRepository.deleteById(id);
    }

    public WorkExperience getWorkExperienceById(Long id) {
        return workExperienceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Work Experience not found with ID: " + id));
    }
    

    public WorkExperience updateWorkExperience(Long id, WorkExperience updatedExperience) {
        return workExperienceRepository.findById(id).map(existingExperience -> {
            existingExperience.setStartDate(updatedExperience.getStartDate());
            existingExperience.setEndDate(updatedExperience.getEndDate());
            existingExperience.setCompany(updatedExperience.getCompany());
            existingExperience.setRole(updatedExperience.getRole());
            existingExperience.setLocation(updatedExperience.getLocation());
            existingExperience.setDescription(updatedExperience.getDescription());
            return workExperienceRepository.save(existingExperience);
        }).orElseThrow(() -> new RuntimeException("Work Experience not found with ID: " + id));
    }
    
}
