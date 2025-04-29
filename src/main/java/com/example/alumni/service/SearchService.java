package com.example.alumni.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.alumni.model.User;
import com.example.alumni.model.WorkExperience;
import com.example.alumni.repository.UserRepository;
import com.example.alumni.repository.WorkExperienceRepository;

@Service
public class SearchService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkExperienceRepository workExperienceRepository;

    public List<User> search(String type, String query) {
        if ("name".equalsIgnoreCase(type)) {
            return userRepository.findByNameContainingIgnoreCase(query);
        } else if ("company_name".equalsIgnoreCase(type)) {
            List<WorkExperience> experiences = workExperienceRepository.findByCompanyContainingIgnoreCase(query);
            return experiences.stream()
                .map(WorkExperience::getUser) // Extract user from WorkExperience
                .distinct()
                .collect(Collectors.toList());
        } else if ("batch".equalsIgnoreCase(type)) {
            return userRepository.findByBatchContainingIgnoreCase(query);
        }
        return Collections.emptyList();
    }
    

}
