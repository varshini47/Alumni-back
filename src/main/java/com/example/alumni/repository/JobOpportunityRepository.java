package com.example.alumni.repository;
import com.example.alumni.model.JobOpportunity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOpportunityRepository extends JpaRepository<JobOpportunity, Long> {
        List<JobOpportunity> findByUserId(Long userId);
        void deleteByUserId(Long userId);


}