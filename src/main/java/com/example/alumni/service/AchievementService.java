
package com.example.alumni.service;
import com.example.alumni.model.Achievement;
import com.example.alumni.repository.AchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achAchievementRepository;

    @Autowired
    private UserPointsService userPointsService;

    public List<Achievement> getAllAchievements() {
        return achAchievementRepository.findAll();
    }

    public Achievement addAchievement(Achievement achAchievement) {

        System.out.println(userPointsService.getPoints(achAchievement.getUserId()));
        return achAchievementRepository.save(achAchievement);
    }

    public List<Achievement> getAchievementsByUserId(Long userId) {
        // Placeholder method if you need filtering by userId
        return achAchievementRepository.findByUserId(userId);
    }

    //  public Optional<Achievement> getAchievementById(Long id) {
    //     return achAchievementRepository.findById(id);
    // }

    public Achievement getAchievementById(Long id) {
        return achAchievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Achievement not found with ID: " + id));
    }

    public Achievement updateAchievement(Long id, Achievement updatedAchievement) {
        Achievement existingAchievement = achAchievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Achievement not found with ID: " + id));
    
        // Update fields
        existingAchievement.setTitle(updatedAchievement.getTitle());
        existingAchievement.setDateOfAchievement(updatedAchievement.getDateOfAchievement());
        existingAchievement.setCategory(updatedAchievement.getCategory());
        existingAchievement.setDescription(updatedAchievement.getDescription());
        existingAchievement.setSupportingDocuments(updatedAchievement.getSupportingDocuments());
        existingAchievement.setOrganization(updatedAchievement.getOrganization());
    
        return achAchievementRepository.save(existingAchievement);
    }
    

    // Method to delete a job opportunity by ID
    public void deleteAchievement(Long id) {
        achAchievementRepository.deleteById(id);
    }
}