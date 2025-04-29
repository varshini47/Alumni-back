package com.example.alumni.controller;

import com.example.alumni.model.ContactMessage;
import com.example.alumni.repository.ContactMessageRepository;
import com.google.common.base.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "http://localhost:3000") // Adjust as needed
public class ContactMessageController {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    // @PostMapping("/submit")
    // public ResponseEntity<String> submitContactForm(@RequestBody ContactMessage contactMessage) {
    //     System.out.println("\n\nEntered contacr");
    //     System.out.println(contactMessage);
    //     contactMessageRepository.save(contactMessage);
    //     return ResponseEntity.ok("Message received successfully!");
    // }

    @PostMapping("/submit")
    public ResponseEntity<String> submitContactForm(@Valid @RequestBody ContactMessage contactMessage) {
        System.out.println("\n\nEntered contact");
        System.out.println(contactMessage);
        
        // Validate required fields
        if (contactMessage.getName() == null || contactMessage.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Name is required");
        }
        if (contactMessage.getEmail() == null || contactMessage.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        if (contactMessage.getMessage() == null || contactMessage.getMessage().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Message is required");
        }

        // Explicitly set resolved to false in case it's missing
        contactMessage.setResolved(false);

        // `createdAt` is already handled by @PrePersist

        contactMessageRepository.save(contactMessage);
        return ResponseEntity.ok("Message received successfully!");
    }

    @GetMapping("/all")
    public ResponseEntity<List<ContactMessage>> getAllContactMessages() {
        List<ContactMessage> messages = contactMessageRepository.findAll();
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<String> markAsResolved(@PathVariable Long id) {
         ContactMessage messageOptional = contactMessageRepository.findById(id)
                                           .orElse(null);
        
        if (messageOptional !=null) {
            ContactMessage message = messageOptional;
            message.setResolved(true);  // ✅ Updating the resolved field
            contactMessageRepository.save(message);  // ✅ Saving changes to DB
            return ResponseEntity.ok("Message marked as resolved");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 📌 Delete a message
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id) {
        if (contactMessageRepository.existsById(id)) {
            contactMessageRepository.deleteById(id);
            return ResponseEntity.ok("Message deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
