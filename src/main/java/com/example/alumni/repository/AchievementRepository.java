package com.example.alumni.repository;
import com.example.alumni.dto.AchievementDTO;
import com.example.alumni.model.Achievement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
        List<Achievement> findByUserId(Long userId);
        void deleteByUserId(Long userId);
//        @Query("SELECT new com.example.alumni.dto.AchievementDTO(a.id, a.title, a.category, a.dateOfAchievement, a.description, " +
//        "a.organization, a.supportingDocuments, u.name) " +
//        "FROM Achievement a JOIN User u ON a.userId = u.id")
// List<AchievementDTO> getAllAchievementsWithUser();
@Query("SELECT new com.example.alumni.dto.AchievementDTO(a.id, a.title, a.category, a.dateOfAchievement, a.description, " +
"a.organization, a.supportingDocuments, " +
"CASE WHEN a.achievedBy IS NULL THEN u.name ELSE a.achievedBy END) " +
"FROM Achievement a JOIN User u ON a.userId = u.id")
List<AchievementDTO> getAllAchievementsWithUser();



}