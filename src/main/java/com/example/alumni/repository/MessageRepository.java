package com.example.alumni.repository;

import com.example.alumni.model.Message;
import com.example.alumni.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
//     @Query("SELECT DISTINCT u FROM User u WHERE u.id IN " +
//        "(SELECT DISTINCT CASE " +
//        "WHEN m.senderId = :userId THEN m.receiverId " +
//        "WHEN m.receiverId = :userId THEN m.senderId END " +
//        "FROM Message m WHERE m.senderId = :userId OR m.receiverId = :userId)")
// List<User> findRecentContacts(@Param("userId") Long userId);
@Query("SELECT DISTINCT u FROM User u WHERE u.id IN " +
       "(SELECT DISTINCT CASE " +
       "WHEN m.senderId = :userId THEN m.receiverId " +
       "WHEN m.receiverId = :userId THEN m.senderId END " +
       "FROM Message m WHERE m.senderId = :userId OR m.receiverId = :userId) " +
       "ORDER BY (SELECT MAX(m.timestamp) FROM Message m " +
       "          WHERE (m.senderId = u.id AND m.receiverId = :userId) " +
       "             OR (m.receiverId = u.id AND m.senderId = :userId)) DESC")
List<User> findRecentContacts(@Param("userId") Long userId);

@Query("SELECT m FROM Message m WHERE " +
"(m.senderId = :userId AND m.receiverId = :receiverId) OR " +
"(m.senderId = :receiverId AND m.receiverId = :userId) " +
"ORDER BY m.timestamp ASC")
List<Message> findBySenderAndReceiver(@Param("userId") Long userId, @Param("receiverId") Long receiverId);



}


