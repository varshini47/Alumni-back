package com.example.alumni.controller;
import com.example.alumni.model.ChatGroup;
import com.example.alumni.model.User;
import com.example.alumni.repository.GroupRepository;
import com.example.alumni.repository.UserRepository;
import com.example.alumni.service.JobOpportunityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/searchchat")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SearchChatController {

     @Autowired
    private  UserRepository userRepository;
   
    @Autowired
    private GroupRepository groupRepository;

    public SearchChatController(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    // Search Users
    @GetMapping("/users")
    public List<User> searchUsers(@RequestParam String query) {
        return userRepository.findByNameContainingIgnoreCase(query);
    }

    // Search Groups
    @GetMapping("/groups")
    public List<ChatGroup> searchGroups(@RequestParam String query) {
        return groupRepository.findByNameContainingIgnoreCase(query);
    }

    @GetMapping("/groups/{groupId}/members")
public ResponseEntity<List<User>> getGroupMembers(@PathVariable Long groupId) {
    ChatGroup group = groupRepository.findById(groupId)
                                 .orElseThrow(() -> new RuntimeException("Group not found"));
    
    // Convert PersistentSet to List
    List<User> members = new ArrayList<>(group.getMembers());

    return ResponseEntity.ok(members);
}

}
