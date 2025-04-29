package com.example.alumni.repository;

import com.example.alumni.model.Connection;
import com.example.alumni.model.Connection.ConnectionStatus;
import com.example.alumni.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    List<Connection> findBySenderOrReceiver(User sender, User receiver);
    List<Connection> findBySenderAndStatus(User sender, ConnectionStatus status);
    List<Connection> findByReceiverAndStatus(User receiver, ConnectionStatus status);
    Optional<Connection> findBySenderAndReceiver(User sender, User receiver);
    boolean existsBySenderAndReceiver(User sender, User receiver);
    List<Connection> findBySenderAndStatusOrReceiverAndStatus(
        User sender, ConnectionStatus senderStatus,
        User receiver, ConnectionStatus receiverStatus
    );
}
