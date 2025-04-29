package com.example.alumni.service;

import com.example.alumni.model.Connection;
import com.example.alumni.model.Connection.ConnectionStatus;
import com.example.alumni.model.User;
import com.example.alumni.repository.ConnectionRepository;
import com.example.alumni.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;

    public ConnectionService(ConnectionRepository connectionRepository, UserRepository userRepository) {
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
    }

    public Connection sendRequest(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new RuntimeException("Sender with ID " + senderId + " not found"));
        User receiver = userRepository.findById(receiverId)
            .orElseThrow(() -> new RuntimeException("Receiver with ID " + receiverId + " not found"));

        Connection connection = new Connection();
        connection.setSender(sender);
        connection.setReceiver(receiver);
        connection.setStatus(ConnectionStatus.PENDING);

        return connectionRepository.save(connection);
    }
    
    // Alias for sendRequest to match controller method name
    public Connection sendConnectionRequest(Long senderId, Long receiverId) {
        return sendRequest(senderId, receiverId);
    }

    public List<Connection> getPendingRequests(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));
        return connectionRepository.findByReceiverAndStatus(user, ConnectionStatus.PENDING);
    }
    
    public List<Connection> getConnectedUsers(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));
        return connectionRepository.findBySenderAndStatus(user, ConnectionStatus.ACCEPTED);
    }
    
    // Alias for getPendingRequests to match controller method name
    public List<Connection> getPendingConnections(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));
        return connectionRepository.findBySenderAndStatus(user, ConnectionStatus.PENDING);
    }
    
    // Alias for getConnectedUsers to match controller method name
    public List<Connection> getAcceptedConnections(Long userId) {
        return getConnectedUsers(userId);
    }
    
    // Method to get all connections for a user
    public List<Connection> getAllConnections(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));
        return connectionRepository.findBySenderOrReceiver(user, user);
    }

    @Transactional
    public void acceptRequest(Long requestId) {
        System.out.println("\n\n........acceptedRequest1\n\n");
        Connection connection = connectionRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Connection request with ID " + requestId + " not found"));
            System.out.println("\n\n........acceptedRequest2\n\n");
        
        // Set the original connection to ACCEPTED
        connection.setStatus(ConnectionStatus.ACCEPTED);
        connectionRepository.save(connection);
        
        // Create a reverse connection (receiver to sender) with ACCEPTED status
        User sender = connection.getSender();
        User receiver = connection.getReceiver();
        
        // Check if reverse connection already exists
        Optional<Connection> existingReverseConnection = connectionRepository.findBySenderAndReceiver(receiver, sender);
        if (existingReverseConnection.isPresent()) {
            // Update existing reverse connection to ACCEPTED
            Connection reverseConnection = existingReverseConnection.get();
            reverseConnection.setStatus(ConnectionStatus.ACCEPTED);
            connectionRepository.save(reverseConnection);
        } else {
            // Create new reverse connection
            Connection reverseConnection = new Connection();
            reverseConnection.setSender(receiver);
            reverseConnection.setReceiver(sender);
            reverseConnection.setStatus(ConnectionStatus.ACCEPTED);
            connectionRepository.save(reverseConnection);
        }
    }
    
    // Alias for acceptRequest to match controller method name
    public Connection respondToConnectionRequest(Long connectionId, boolean accept) {
        if (accept) {
            acceptRequest(connectionId);
            return connectionRepository.findById(connectionId)
                .orElseThrow(() -> new RuntimeException("Connection not found after accepting"));
        } else {
            rejectRequest(connectionId);
            return null;
        }
    }

    public void rejectRequest(Long requestId) {
        if (!connectionRepository.existsById(requestId)) {
            throw new RuntimeException("Connection request with ID " + requestId + " not found");
        }
        connectionRepository.deleteById(requestId);
    }
    
    public Optional<Connection> getConnectionStatus(Long userId, Long otherUserId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));
        User otherUser = userRepository.findById(otherUserId)
            .orElseThrow(() -> new RuntimeException("Other user with ID " + otherUserId + " not found"));
            
        return connectionRepository.findBySenderAndReceiver(user, otherUser)
            .or(() -> connectionRepository.findBySenderAndReceiver(otherUser, user));
    }
}
