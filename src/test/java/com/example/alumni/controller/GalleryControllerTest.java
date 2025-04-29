package com.example.alumni.controller;

import com.example.alumni.model.GalleryItem;
import com.example.alumni.repository.GalleryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(GalleryController.class)
@AutoConfigureMockMvc(addFilters = false)
class GalleryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GalleryRepository galleryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private GalleryItem galleryItem;

    @BeforeEach
    void setUp() {
        System.out.println("\n=== Setting up test ===");
        galleryItem = new GalleryItem();
        galleryItem.setId(1L);
        galleryItem.setFolderName("Test Folder");
        galleryItem.setUserId(1L);
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("image1.jpg");
        imageUrls.add("image2.jpg");
        galleryItem.setImageUrls(imageUrls);
        System.out.println("=== Test setup complete ===\n");
    }

    @Test
    void getFolders_success() throws Exception {
        System.out.println("\n=== Starting getFolders_success test ===");
        List<GalleryItem> folders = Arrays.asList(galleryItem);
        when(galleryRepository.findAll()).thenReturn(folders);

        mockMvc.perform(get("/api/gallery/folders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].folderName").value("Test Folder"));

        verify(galleryRepository).findAll();
        System.out.println("=== End of getFolders_success test ===\n");
    }

    @Test
    void getImagesByFolder_success() throws Exception {
        System.out.println("\n=== Starting getImagesByFolder_success test ===");
        when(galleryRepository.findByFolderName("Test Folder")).thenReturn(Optional.of(galleryItem));

        mockMvc.perform(get("/api/gallery/folder/Test Folder"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("image1.jpg"))
                .andExpect(jsonPath("$[1]").value("image2.jpg"));

        verify(galleryRepository).findByFolderName("Test Folder");
        System.out.println("=== End of getImagesByFolder_success test ===\n");
    }

    @Test
    void getImagesByFolder_notFound() throws Exception {
        System.out.println("\n=== Starting getImagesByFolder_notFound test ===");
        when(galleryRepository.findByFolderName("NonExistentFolder")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/gallery/folder/NonExistentFolder"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Folder not found"));

        verify(galleryRepository).findByFolderName("NonExistentFolder");
        System.out.println("=== End of getImagesByFolder_notFound test ===\n");
    }

    @Test
    void uploadImage_success() throws Exception {
        System.out.println("\n=== Starting uploadImage_success test ===");
        when(galleryRepository.findByFolderName("Test Folder")).thenReturn(Optional.of(galleryItem));
        when(galleryRepository.save(any(GalleryItem.class))).thenReturn(galleryItem);

        mockMvc.perform(post("/api/gallery/upload")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"folderName\":\"Test Folder\",\"imageUrl\":\"newImage.jpg\",\"userId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(galleryRepository).findByFolderName("Test Folder");
        verify(galleryRepository).save(any(GalleryItem.class));
        System.out.println("=== End of uploadImage_success test ===\n");
    }

    @Test
    void uploadImage_missingFields() throws Exception {
        System.out.println("\n=== Starting uploadImage_missingFields test ===");
        mockMvc.perform(post("/api/gallery/upload")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"folderName\":\"\",\"imageUrl\":\"\",\"userId\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Folder name and image URL are required."));

        verify(galleryRepository, never()).save(any(GalleryItem.class));
        System.out.println("=== End of uploadImage_missingFields test ===\n");
    }

    @Test
    void deleteFolder_success() throws Exception {
        System.out.println("\n=== Starting deleteFolder_success test ===");
        when(galleryRepository.findById(1L)).thenReturn(Optional.of(galleryItem));
        doNothing().when(galleryRepository).deleteById(1L);

        mockMvc.perform(delete("/api/gallery/folders/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Folder deleted successfully."));

        verify(galleryRepository).deleteById(1L);
        System.out.println("=== End of deleteFolder_success test ===\n");
    }

    @Test
    void deleteFolder_notFound() throws Exception {
        System.out.println("\n=== Starting deleteFolder_notFound test ===");
        when(galleryRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/gallery/folders/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Folder not found."));

        verify(galleryRepository, never()).deleteById(anyLong());
        System.out.println("=== End of deleteFolder_notFound test ===\n");
    }

    @Test
    void deleteImage_success() throws Exception {
        System.out.println("\n=== Starting deleteImage_success test ===");
        when(galleryRepository.findById(1L)).thenReturn(Optional.of(galleryItem));
        when(galleryRepository.save(any(GalleryItem.class))).thenReturn(galleryItem);

        mockMvc.perform(delete("/api/gallery/1/images")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"imageUrl\":\"image1.jpg\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Image deleted successfully."));

        verify(galleryRepository).save(any(GalleryItem.class));
        System.out.println("=== End of deleteImage_success test ===\n");
    }

    @Test
    void deleteImage_notFound() throws Exception {
        System.out.println("\n=== Starting deleteImage_notFound test ===");
        when(galleryRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/gallery/1/images")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"imageUrl\":\"nonExistent.jpg\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Folder not found."));

        verify(galleryRepository, never()).save(any(GalleryItem.class));
        System.out.println("=== End of deleteImage_notFound test ===\n");
    }
} 