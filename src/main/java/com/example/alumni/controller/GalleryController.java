package com.example.alumni.controller;

import com.example.alumni.model.GalleryItem;
import com.example.alumni.repository.GalleryRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/gallery")
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for frontend
public class GalleryController {

    @Autowired
    private GalleryRepository galleryRepository;

    // Get all folders
    @GetMapping("/folders")
    public List<GalleryItem> getFolders() {
        return galleryRepository.findAll();
    }

    // Get images inside a folder
    @GetMapping("/folder/{folderName}")
    public ResponseEntity<?> getImagesByFolder(@PathVariable String folderName) {
        Optional<GalleryItem> folder = galleryRepository.findByFolderName(folderName);

        if (!folder.isPresent()) {
            return ResponseEntity.status(404).body("Folder not found");
        }

        return ResponseEntity.ok(folder.get().getImageUrls());
    }

    // Upload an image (Create folder if it doesn’t exist)
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestBody Map<String, String> requestBody) {
        System.out.println("entered-1");
        String folderName = requestBody.get("folderName");
        String imageUrl = requestBody.get("imageUrl");
        Long userId = Long.parseLong(requestBody.get("userId"));
        System.out.println("entered-2");

        if (folderName == null || imageUrl == null || folderName.isEmpty() || imageUrl.isEmpty()) {
            return ResponseEntity.badRequest().body("Folder name and image URL are required.");
        }

        // Check if folder exists, otherwise create it
        GalleryItem folder = galleryRepository.findByFolderName(folderName)
            .orElseGet(() -> {
                GalleryItem newFolder = new GalleryItem();
                newFolder.setFolderName(folderName);
                newFolder.setUserId(userId);
                newFolder.setImageUrls(new ArrayList<>());
                return galleryRepository.save(newFolder);
            });
            System.out.println("entered-3");
        // Add image to folder
        folder.getImageUrls().add(imageUrl);
        System.out.println("entered-4");
        galleryRepository.save(folder);
        System.out.println("entered-5");
        return ResponseEntity.ok(folder);
    }

    @DeleteMapping("/folders/{folderId}")
public ResponseEntity<?> deleteFolder(@PathVariable Long folderId) {
    System.out.println("\n\n"+folderId);
    Optional<GalleryItem> folder = galleryRepository.findById(folderId);
    if (folder.isPresent()) {
        System.out.println("inside folder");
        galleryRepository.deleteById(folderId);
        return ResponseEntity.ok("Folder deleted successfully.");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Folder not found.");
    }
}

@DeleteMapping("/{folderId}/images")
public ResponseEntity<?> deleteImage(@PathVariable Long folderId, @RequestBody Map<String, String> requestBody) {
    String imageUrl = requestBody.get("imageUrl");

    // Find folder by ID
    Optional<GalleryItem> folderOptional = galleryRepository.findById(folderId);

    if (folderOptional.isPresent()) {
        GalleryItem folder = folderOptional.get();
        List<String> images = folder.getImageUrls();

        if (images.remove(imageUrl)) { // Remove if exists
            galleryRepository.save(folder); // Save updated folder
            return ResponseEntity.ok("Image deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found in folder.");
        }
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Folder not found.");
    }
}


}
