package com.example.alumni.controller;

import com.example.alumni.model.Message;
import com.example.alumni.model.User;
import com.example.alumni.repository.MessageRepository;
import com.google.common.base.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for frontend
public class ChatController {

    @Autowired
    private MessageRepository chatMessageRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload Message message) {
        message.setTimestamp(LocalDateTime.now());

        chatMessageRepository.save(message);

        messagingTemplate.convertAndSendToUser(message.getReceiverId().toString(), "/messages", message);
        messagingTemplate.convertAndSendToUser(message.getSenderId().toString(), "/messages", message);

        System.out.println("\n\nhiiiii");
        // messagingTemplate.convertAndSendToUser(message.getSenderId().toString(),
        // "/queue/messages", message);
    }

    @MessageMapping("/messageUpdate")
    public void updateMessage(Message updatedMessage) {
        Message existingMessage = chatMessageRepository.findById(updatedMessage.getId())
                .orElseThrow(() -> new RuntimeException("Message not found"));
    
        existingMessage.setContent(updatedMessage.getContent());
        existingMessage.setTimestamp(LocalDateTime.now());
    
        Message savedMessage = chatMessageRepository.save(existingMessage);
    
        // Notify sender & receiver
        messagingTemplate.convertAndSendToUser(
            savedMessage.getReceiverId().toString(), "/messageUpdate", savedMessage
        );
    
        messagingTemplate.convertAndSendToUser(
            savedMessage.getSenderId().toString(), "/messageUpdate", savedMessage
        );
    
        System.out.println("Message updated: " + savedMessage);
    }
    
    // ✅ Delete Message
    // @MessageMapping("/messageDelete")
    // public void deleteMessage(Long messageId) {

    //     System.out.println("\n\nmessageId is    :"+ messageId);
    //     Message messageToDelete = chatMessageRepository.findById(messageId)
    //             .orElseThrow(() -> new RuntimeException("Message not found"));
    
    //     chatMessageRepository.deleteById(messageId);
    
    //     // Notify sender & receiver about deletion
    //     messagingTemplate.convertAndSendToUser(
    //         messageToDelete.getReceiverId().toString(), "/messageDelete", messageId
    //     );
    
    //     messagingTemplate.convertAndSendToUser(
    //         messageToDelete.getSenderId().toString(), "/messageDelete", messageId
    //     );
    
    //     System.out.println("Message deleted: " + messageId);
    // }
    @MessageMapping("/messageDelete")
public void deleteMessage(@Payload Map<String, Object> payload) {
    Long messageId = Long.valueOf(payload.get("id").toString());

    System.out.println("\n\nmessageId is    :" + messageId);

    Message messageToDelete = chatMessageRepository.findById(messageId)
            .orElseThrow(() -> new RuntimeException("Message not found"));

    chatMessageRepository.deleteById(messageId);

    // Notify sender & receiver about deletion
    messagingTemplate.convertAndSendToUser(
        messageToDelete.getReceiverId().toString(), "/messageDelete", messageId
    );

    messagingTemplate.convertAndSendToUser(
        messageToDelete.getSenderId().toString(), "/messageDelete", messageId
    );

    System.out.println("Message deleted: " + messageId);
}


    @GetMapping("/recent/{userId}")
    public List<User> getRecentContacts(@PathVariable Long userId) {
        System.out.println("Entered" + userId);
        return chatMessageRepository.findRecentContacts(userId);
    }

    @GetMapping("/history/{userId}")
    public List<Message> getChatHistory(@PathVariable Long userId) {
        return chatMessageRepository.findBySenderIdOrReceiverId(userId, userId);
    }

    @GetMapping("/history/{userId}/{receiverId}")
    public List<Message> getChatHistoryBetweenUsers(@PathVariable Long userId, @PathVariable Long receiverId) {
        return chatMessageRepository.findBySenderAndReceiver(userId, receiverId);
    }

    @PutMapping("/edit/{messageId}")
    public ResponseEntity<Message> editMessage(@PathVariable Long messageId, @RequestBody String newContent) {
        System.out.println("\n\n"+newContent);
        return chatMessageRepository.findById(messageId)
                .map(message -> {
                    message.setContent(newContent);

                    System.out.println(message);
                    chatMessageRepository.save(message);

                    // Notify frontend about the update
                    messagingTemplate.convertAndSendToUser(message.getReceiverId().toString(), "/messages", message);

                    return ResponseEntity.ok(message);
                }).orElse(ResponseEntity.notFound().build());
    }

    // **✅ Delete Message**
    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<Void> deleteeMessage(@PathVariable Long messageId) {
        if (chatMessageRepository.existsById(messageId)) {
            chatMessageRepository.deleteById(messageId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
