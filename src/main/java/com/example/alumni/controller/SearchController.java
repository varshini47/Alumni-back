package com.example.alumni.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.alumni.service.SearchService;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:3000")// Allow frontend access
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    public ResponseEntity<?> search(@RequestParam String type, @RequestParam String query) {
        List<?> results = searchService.search(type, query);
        System.out.println(results);
        return ResponseEntity.ok(results);
    }
    

}
