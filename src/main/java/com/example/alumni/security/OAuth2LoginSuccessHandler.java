package com.example.alumni.security;

import com.example.alumni.model.User;
import com.example.alumni.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    // Constructor to inject the UserRepository
    public OAuth2LoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Retrieve the OAuth2 user details
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauthUser.getAttributes();
        System.out.println(attributes);
        String email = (String) attributes.get("email"); // Get email from OAuth2 attributes
        String given_name = (String) attributes.get("given_name"); 
        String last_name = (String) attributes.get("family_name"); 

        // Check if the user exists in the database using the email
        User existingUser = userRepository.findByEmail(email).orElse(null);

        HttpSession session = request.getSession();  // Get the HTTP session
        if (existingUser != null) {
            // If user exists, store the user in the session
            session.setAttribute("user", existingUser);
            response.sendRedirect("http://localhost:3000/login");  // Redirect to login page
        } else {
            // If user doesn't exist, create a new user and store in session
            User newUser = new User();
            System.out.println(
                email
            );
            System.out.println(
                given_name
            );
            System.out.println(
                last_name
            );
            newUser.setEmail(email); // Set the email
            newUser.setName(given_name); 
            newUser.setLastName(last_name);  // Set the name
           // userRepository.save(newUser);  // Save the new user to the database

            session.setAttribute("user", newUser);  // Store the new user in the session
            response.sendRedirect("http://localhost:3000/complete-profile");  // Redirect to complete profile page
        }
    }
}