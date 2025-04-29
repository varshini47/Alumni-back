package com.example.alumni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.alumni.model.ContactMessage;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
}
