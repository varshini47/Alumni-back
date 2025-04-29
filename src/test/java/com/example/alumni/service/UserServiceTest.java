package com.example.alumni.service;
import com.example.alumni.model.User;
import com.example.alumni.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPointsService userPointsService;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private WorkExperienceRepository workExperienceRepository;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private JobOpportunityRepository jobOpportunityRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
        System.out.println("\n=== Setting up test ===");
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setName("John");
        System.out.println("=== Test setup complete ===\n");
    }

    @Test
    void registerUser_successful() {
        System.out.println("\n=== Starting registerUser_successful test ===");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPwd");
        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = userService.registerUser(user);
        System.out.println("Test completed with result: " + result);
        System.out.println("=== End of registerUser_successful test ===\n");
        
        assertEquals("User registered successfully!", result);
        verify(userRepository).findByEmail(user.getEmail());
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
        verify(userPointsService).addPoints(user.getId(), 10);
    }

    @Test
    void registerUser_emailExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        String result = userService.registerUser(user);

        assertEquals("Email already registered!", result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void loginUser_successful() {
        user.setPassword("encodedPwd");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPwd")).thenReturn(true);

        Optional<User> result = userService.loginUser(user.getEmail(), "password");

        assertTrue(result.isPresent());
    }

    @Test
    void loginUser_invalidPassword() {
        user.setPassword("encodedPwd");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPwd", "encodedPwd")).thenReturn(false);

        Optional<User> result = userService.loginUser(user.getEmail(), "wrongPwd");

        assertFalse(result.isPresent());
    }

    @Test
    void getAllUsers_shouldPrintAndReturnUsers() {
        User user = new User();
        user.setId(1L);
        
        when(userRepository.findAll()).thenReturn(List.of(user));
        
        List<User> result = userService.getAllUsers();
        
        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void updateUser_success() {
        User updatedUser = new User();
        updatedUser.setName("Updated Name");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUserProfile(1L, updatedUser);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
    }

    @Test
    void updateUser_notFound() {
        User updatedUser = new User();
        
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.updateUserProfile(1L, updatedUser);
        });
    }

    @Test
    void deleteUser_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        boolean result = userService.deleteUserById(1L);

        assertTrue(result);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = userService.deleteUserById(1L);

        assertFalse(result);
        verify(userRepository, never()).delete(any(User.class));
    }
}
