package com.example.alumni.repository;
import com.example.alumni.model.User;
import com.example.alumni.model.WorkExperience;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {
    List<WorkExperience> findByUser(User user);
    List<WorkExperience> findByCompanyContainingIgnoreCase(String company);
    void deleteByUser_Id(Long userId);
    @Query("SELECT w FROM WorkExperience w JOIN FETCH w.user")
    List<WorkExperience> findAllWithUser();

}