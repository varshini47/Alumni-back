
package com.example.alumni.security;

import com.example.alumni.security.OAuth2LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;

    // Constructor to inject OAuth2LoginSuccessHandler
    public SecurityConfig(OAuth2LoginSuccessHandler oauth2LoginSuccessHandler) {
        this.oauth2LoginSuccessHandler = oauth2LoginSuccessHandler;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disabling CSRF (only recommended for development)
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oauth2LoginSuccessHandler) // Set the success handler
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/register", "/api/work-experience/{id}", "/api/login", "/api/chat",
                                "/api/chat/sendMessage","/api/contact/submit","/api/contact/all",
                                "/api/google-login", "/api/complete-profile", "/api/logout", "/api/users/{id}",
                                "/api/chat/groups","api/contact/{id}/resolve","api/contact/{id}",
                                "/api/users/{id}/updateProfile", "/api/chat/availableGroups/{id}",
                                "/api/chat/recent/{userId}",
                                "/api/work-experience/user/{id}", "/api/work-experience", "/api/work-experience/{id}",
                                "/api/chat/groups/{groupId}/join","api/events/{id}",
                                "/api/chat/groups/{userId}", "/api/chat/groups/create",
                                "/api/chat/history/{userId}/{receiverId}", "/api/chat/history/{userId}",
                                "/api/chat/groups/join/{groupId}/{userId}", "/api/chat/sendGroupMessage",
                                "/api/chat/groupHistory/{groupId}", "/api/chat", "/api/chat/sendGroupMessage",
                                "/api/chat/groups/{groupId}/messages","/api/gallery/{folderId}/images","/api/gallery/folders/{folderId}",
                                "/api/achievements", "/api/searchchat", "/api/achievements/all", "/api/chat/send",
                                "/api/achievements/user/{id}", "/chat", "/chat/**", "/api/chat/history/{userId}",
                                "/api/jobs", "/api/email", "/api/gallery/folders", "/api/gallery/folder/{folderName}",
                                "/api/gallery/upload", "/api/email/invite", "/api/search", "/api/events",
                                "/api/users/{id}/points", "/api/users/leaderboard", "/api/jobs/{id}",
                                "/api/chat/edit/{messageId}", "/api/chat/delete/{messageId}",
                                "/api/chat/group/edit/{messageId}", "/api/chat/group/delete/{messageId}",
                                "/api/achievements/{id}", "/api/users", "/api/chat/recent/{userId}","/api/chat/groups/exit",
                                "/api/chat/groups/recent/{userId}","api/chat/groupMessageUpdate","api/chat/groupMessageDelete",
                                "/api/chat/history/{userId}/{receiverId}", "api/email/forgot-password",
                                "api/email/reset-password", "/api/searchchat", "/api/searchchat/users","/api/chat/messageUpdate","/api/chat/messageDelete",
                                "/api/searchchat/groups", "/api/searchchat/groups/{groupId}/members",
                                "/api/connections/send/{senderId}/{receiverId}", "/api/connections/pending/{userId}",
                                "/api/connections/accept/{requestId}", "/api/connections/reject/{connectionId}",
                                "/api/connections/accepted/{userId}", "/api/connections/rejected/{userId}","/api/connections/user/{userId}")
                        .permitAll() // Public access to specific endpoints
                        .anyRequest().authenticated() // Secure all other requests
                ).csrf(csrf -> csrf.disable());

        return http.build();
    }
}
