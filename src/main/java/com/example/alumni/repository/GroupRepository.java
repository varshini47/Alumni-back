package com.example.alumni.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.alumni.model.ChatGroup;

public interface GroupRepository extends JpaRepository<ChatGroup, Long> {
    List<ChatGroup> findByNameContainingIgnoreCase(String name);
}
