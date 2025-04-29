package com.example.alumni.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.alumni.model.ChatGroup;
import com.example.alumni.model.GroupMessage;
import com.example.alumni.model.User;
import com.example.alumni.repository.GroupMessageRepository;
import com.example.alumni.repository.GroupRepository;
import com.example.alumni.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupMessageRepository groupMessageRepository;

    public ChatGroup createGroup(String name, Long userId) {
        User creator = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        ChatGroup group = new ChatGroup(name, creator);
        return groupRepository.save(group);
    }

    public ChatGroup joinGroup(Long groupId, Long userId) {
        ChatGroup group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        group.getMembers().add(user);
        return groupRepository.save(group);
    }

    // public List<ChatGroup> getAllGroups() {
    //     return groupRepository.findAll();
    // }
    public List<ChatGroup> getAllGroupsSorted() {
        return groupRepository.findAll().stream()
                .sorted(Comparator.comparing(this::getLastMessageTimestamp, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    private LocalDateTime getLastMessageTimestamp(ChatGroup group) {
        return groupMessageRepository.findTopByGroupIdOrderByTimestampDesc(group.getId())
                .map(GroupMessage::getTimestamp)
                .orElse(null);
    }

    public boolean exitGroup(Long groupId, Long userId) {
        ChatGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
    
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        boolean removed = group.getMembers().remove(user);
    
        if (removed) {
            groupRepository.save(group);
        }
    
        return removed;
    }
    
}
