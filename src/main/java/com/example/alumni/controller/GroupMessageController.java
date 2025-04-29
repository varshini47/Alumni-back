package com.example.alumni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import com.example.alumni.model.GroupMessage;
import com.example.alumni.model.Message;
import com.example.alumni.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")

public class GroupMessageController {

    private final GroupMessageRepository messageRepository;

    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public GroupMessageController(GroupMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

   @MessageMapping("/sendGroupMessage")
    public void sendGroupMessage(@Payload GroupMessage message) {
    message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);
        System.out.println("\n\nBola tha");
        System.out.println("✅ Sending message to: " + message.getGroupId());
    System.out.println("📨 Message Content: " + message.getContent());
    // System.out.println("Backend: Sending message to /user/" + message.getGroupId() + "/queue/messages");
    // messagingTemplate.convertAndSendToUser(message.getGroupId().toString(), "/messages", message);

        messagingTemplate.convertAndSendToUser( message.getGroupId().toString(), "/messages", message);
        // messagingTemplate.convertAndSendToGroup(message.getGroupId().toString(), "/messages", message);


        System.out.println("\n\nhiiiii");
        //messagingTemplate.convertAndSendToUser(message.getSenderId().toString(), "/queue/messages", message);
    }


    @MessageMapping("/groupMessageUpdate")
    public void updateGroupMessage(@Payload GroupMessage updatedMessage) {
        GroupMessage existingMessage = messageRepository.findById(updatedMessage.getId())
                .orElseThrow(() -> new RuntimeException("Group message not found"));

        existingMessage.setContent(updatedMessage.getContent());
        existingMessage.setTimestamp(LocalDateTime.now());

        GroupMessage savedMessage = messageRepository.save(existingMessage);

        // ✅ Notify all group members about the updated message
        messagingTemplate.convertAndSendToUser(
            savedMessage.getGroupId().toString(), "/groupMessageUpdate", savedMessage
        );

        System.out.println("✅ Group message updated: " + savedMessage);
    }

    // ✅ WebSocket: Handle Group Message Deletion
    @MessageMapping("/groupMessageDelete")
    public void deleteGroupMessage(@Payload Map<String, Object> payload) {
        Long messageId = Long.valueOf(payload.get("id").toString());

        System.out.println("\n\n🗑️ Deleting group message with ID: " + messageId);

        GroupMessage messageToDelete = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Group message not found"));

        messageRepository.deleteById(messageId);

        // ✅ Notify all group members about the deleted message
        messagingTemplate.convertAndSendToUser(
            messageToDelete.getGroupId().toString(), "/groupMessageDelete", messageId
        );

        System.out.println("✅ Group message deleted: " + messageId);
    }

    @GetMapping("/groups/{groupId}/messages")
    public List<GroupMessage> getGroupMessages(@PathVariable Long groupId) {
        return messageRepository.findByGroupIdOrderByTimestamp(groupId);
    }

    // Send a message (private or group)
    @PostMapping("/send")
    public GroupMessage sendMessage(@RequestBody GroupMessage message) {
        return messageRepository.save(message);
    }

     @PutMapping("/group/edit/{messageId}")
    public ResponseEntity<GroupMessage> editMessage(@PathVariable Long messageId, @RequestBody String newContent) {
        System.out.println("\n\n"+newContent);
        return messageRepository.findById(messageId)
                .map(message -> {
                    message.setContent(newContent);

                    System.out.println(message);
                    messageRepository.save(message);

                    // Notify frontend about the update
                    messagingTemplate.convertAndSendToUser(message.getGroupId().toString(), "/messages", message);

                    return ResponseEntity.ok(message);
                }).orElse(ResponseEntity.notFound().build());
    }

    // **✅ Delete Message**
    @DeleteMapping("/group/delete/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
