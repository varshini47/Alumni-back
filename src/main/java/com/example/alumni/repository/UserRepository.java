package com.example.alumni.repository;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.alumni.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findByNameContainingIgnoreCase(String name);
    List<User> findByBatch(String batch);
    List<User> findByBatchContainingIgnoreCase(String batch);
    void deleteById(Long id);
    // List<User> findByNameContainingIgnoreCase(String name);
}

