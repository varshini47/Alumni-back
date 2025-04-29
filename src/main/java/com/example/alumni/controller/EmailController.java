package com.example.alumni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.alumni.model.User;
import com.example.alumni.service.EmailService;
import com.example.alumni.service.UserPointsService;
import com.example.alumni.dto.InviteRequest;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import com.example.alumni.repository.*;

import java.util.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://localhost:3000")
@SessionAttributes("resetSession")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserPointsService userPointsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    @PostMapping("/forgot-password")
public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request, HttpSession session) {
    String email = request.get("email");
    Optional<User> userOptional = userRepository.findByEmail(email);

  if (!userOptional.isPresent()) { // ✅ CORRECT
    System.out.println("/n/nreturn");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
  }

User user = userOptional.get();
        String resetSessionId = UUID.randomUUID().toString();
        session.setAttribute("resetSession:" + user.getId(), resetSessionId);
    String sessionKey = "resetSession:" + user.getId();

     System.out.println(session.getAttribute(sessionKey));

        System.out.println("/n/n"+user.getId());

    // Send reset link
    String resetLink = "http://localhost:3000/reset-password?userId=" + user.getId();
    emailService.sendResetEmail(user.getEmail(), resetLink);

    return ResponseEntity.ok("Password reset link sent successfully!");
}


@PostMapping("/reset-password")
public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request, HttpSession session) {
    Long userId = Long.parseLong(request.get("userId"));
    String newPassword = request.get("newPassword");
     System.out.println(newPassword);
    // Check if session is valid
    String sessionKey = "resetSession:" + userId;
    System.out.println(session.getAttribute(sessionKey));

    if (session.getAttribute(sessionKey) == null) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired reset link!");
    }

    // Update password in database
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    // Remove session after reset
    session.removeAttribute(sessionKey);

    return ResponseEntity.ok("Password updated successfully!");
}


    @PostMapping("/invite")
    public String sendInvite(@RequestBody InviteRequest inviteRequest) {
        System.out.println("entered email controller-1");
        try {
            System.out.println("entered email controller-2");
            emailService.sendInvite(inviteRequest.getFromEmail(), inviteRequest.getToEmails(),inviteRequest.getName(), inviteRequest.getRegistrationLink());
            System.out.println(inviteRequest.getName());
            int pointsAwarded = inviteRequest.getToEmails().size() * 10;
            userPointsService.addPoints(inviteRequest.getUserId(), pointsAwarded);
            //userPointsService.addPoints(inviteRequest.getUserId(), 20);
            System.out.println(userPointsService.getPoints(inviteRequest.getUserId()));
            return "Invite sent successfully!";
        } catch (MessagingException e) {
            return "Failed to send invite: " + e.getMessage();
        }
    }
}


