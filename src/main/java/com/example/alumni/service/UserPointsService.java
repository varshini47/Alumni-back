package com.example.alumni.service;

import com.example.alumni.model.UserPoints;
import com.example.alumni.dto.UserLeaderboardDTO;
import com.example.alumni.model.User;
import com.example.alumni.repository.UserPointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import com.example.alumni.repository.UserRepository;

@Service
public class UserPointsService {

    @Autowired
    private UserPointsRepository userPointsRepository;

    @Autowired
    private UserRepository userRepository;

    public void addPoints(Long userId, int pointsToAdd) {
        Optional<UserPoints> optionalUserPoints = userPointsRepository.findById(userId);

        UserPoints userPoints;
        if (optionalUserPoints.isPresent()) {
            userPoints = optionalUserPoints.get();
            userPoints.addPoints(pointsToAdd); // Add new points
        } else {
            userPoints = new UserPoints(userId, pointsToAdd); // Create new entry
        }

        userPointsRepository.save(userPoints); // Save to DB
        System.out.println("Added " + pointsToAdd + " points to user: " + userId);
    }

    public int getPoints(Long userId) {
        return userPointsRepository.findById(userId)
                .map(UserPoints::getPoints)
                .orElse(0);
    }

    // public List<User> getLeaderboard() {
    //     List<UserPoints> sortedPoints = userPointsRepository.findAllByOrderByPointsDesc();

    //     return sortedPoints.stream()
    //             .map(up -> userRepository.findById(up.getUserId()).orElse(null)) // Get user details
    //             .filter(user -> user != null) // Filter out null users
    //             .collect(Collectors.toList());
    // }
    public List<UserLeaderboardDTO> getLeaderboard() {
        List<UserPoints> sortedPoints = userPointsRepository.findAllByOrderByPointsDesc();

        return sortedPoints.stream()
                .map(up -> {
                    User user = userRepository.findById(up.getUserId()).orElse(null);
                    return (user != null) ? new UserLeaderboardDTO(user, up.getPoints()) : null;
                })
                .filter(dto -> dto != null) // Remove null values
                .collect(Collectors.toList());
    }
}
