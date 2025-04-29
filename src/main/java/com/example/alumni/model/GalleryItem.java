package com.example.alumni.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class GalleryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String folderName;  // Folder name
    private Long userId;  // ID of the user who uploaded the image

    @ElementCollection
    private List<String> imageUrls; // Store image URLs as a list
}
