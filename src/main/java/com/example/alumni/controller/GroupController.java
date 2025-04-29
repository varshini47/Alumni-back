package com.example.alumni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.alumni.model.ChatGroup;
import com.example.alumni.service.GroupService;
import com.example.alumni.repository.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/groups")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupMessageRepository chatMessageRepository;

    @PostMapping
    public ChatGroup createGroup(@RequestBody GroupRequest request) {
        return groupService.createGroup(request.getName(), request.getCreatedBy());
    }

    @PostMapping("/{groupId}/join")
    public ChatGroup joinGroup(@PathVariable Long groupId, @RequestBody JoinGroupRequest request) {
        return groupService.joinGroup(groupId, request.getUserId());
    }

    @GetMapping
    public List<ChatGroup> getAllGroups() {
        return groupService.getAllGroupsSorted();
    }

    // @PostMapping("/{groupId}/exit")
    // public ResponseEntity<?> exitGroup(@RequestParam Long groupId, @RequestParam Long userId) {
    //     boolean removed = groupService.exitGroup(groupId, userId);

    //     if (removed) {
    //         return ResponseEntity.ok("User exited group successfully");
    //     } else {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to exit group");
    //     }
    // }

    @PostMapping("/exit")
    public ResponseEntity<?> exitGroup(@RequestBody GroupExitRequest request) {
        boolean removed = groupService.exitGroup(request.getGroupId(), request.getUserId());

        if (removed) {
            return ResponseEntity.ok("User exited group successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to exit group");
        }
    }

    @GetMapping("recent/{userId}")
    public List<ChatGroup> getRecentGroups(@PathVariable Long userId) {
        System.out.println("\n\nFetching recent groups for user: " + userId);
        System.out.println("Hi i am sheershika");
        List<ChatGroup> g = chatMessageRepository.findRecentGroups(userId);
        System.out.println("\n\n" + g);
        return chatMessageRepository.findRecentGroups(userId);
    }

}

// Request DTOs
class GroupRequest {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    private Long createdBy;
    // Getters and Setters
}

class JoinGroupRequest {
    private Long userId;
    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

 class GroupExitRequest {
    private Long userId;
    private Long groupId;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }
}
