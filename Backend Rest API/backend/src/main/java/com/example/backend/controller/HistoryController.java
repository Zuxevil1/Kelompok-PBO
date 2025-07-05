package com.example.backend.controller;

import com.example.backend.model.History;
import com.example.backend.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@CrossOrigin
public class HistoryController {

    @Autowired
    private HistoryRepository historyRepository;

    @GetMapping
    public List<History> getAll() {
        return historyRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return historyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody History history) {
        try {
            History saved = historyRepository.save(history);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace(); // CETAK ERROR DI TERMINAL
            return ResponseEntity.internalServerError().body("Gagal menyimpan history: " + e.getMessage());
        }
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!historyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        historyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
