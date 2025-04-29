package com.example.alumni.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ChatGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
        name = "group_members",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    public ChatGroup() {}

    public ChatGroup(String name, User createdBy) {
        this.name = name;
        this.members.add(createdBy); // Add creator as the first member
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<User> getMembers() {  // ✅ Now we have getMembers() function!
        return members;
    }

    public void addMember(User user) {
        this.members.add(user);
    }
}
