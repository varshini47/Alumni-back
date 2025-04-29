package com.example.alumni.repository;

import com.example.alumni.model.GalleryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GalleryRepository extends JpaRepository<GalleryItem, Long> {
    Optional<GalleryItem> findByFolderName(String folderName);
}
