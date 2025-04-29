package com.example.alumni.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventName;
    private String description;
    private String eventType;
    private String organizer;
    private String date;
    private String venue;
    private String contactPersonEmail;
    private String sponsorshipDetails;
}

