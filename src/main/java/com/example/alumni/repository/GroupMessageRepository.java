package com.example.alumni.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.example.alumni.model.*;

import java.util.List;

@Repository
public interface GroupMessageRepository extends JpaRepository<GroupMessage, Long> {
    
    // Get messages between two users (Private chat)
  
    
    // Get messages in a group
    Optional<GroupMessage> findTopByGroupIdOrderByTimestampDesc(Long groupId);
    List<GroupMessage> findByGroupIdOrderByTimestamp(Long groupId);
    @Query("SELECT g FROM ChatGroup g " +
    "JOIN g.members u " +  // Join group members directly
    "JOIN GroupMessage m ON g.id = m.groupId " +
    "WHERE u.id = :userId " +
    "GROUP BY g.id " +
    "ORDER BY MAX(m.timestamp) DESC")
List<ChatGroup> findRecentGroups(@Param("userId") Long userId);


}
