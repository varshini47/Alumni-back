package com.example.alumni.repository;

import com.example.alumni.model.UserPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserPointsRepository extends JpaRepository<UserPoints, Long> {
    List<UserPoints> findAllByOrderByPointsDesc();
}
