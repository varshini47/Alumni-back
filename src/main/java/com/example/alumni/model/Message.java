package com.example.alumni.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime timestamp;
}
