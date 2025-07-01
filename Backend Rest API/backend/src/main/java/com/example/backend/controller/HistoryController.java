package com.example.backend.controller;

import com.example.backend.model.History;
import com.example.backend.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private HistoryRepository repo;

    @GetMapping
    public List<History> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<History> getById(@PathVariable Integer id) {
        Optional<History> opt = repo.findById(id);
        return opt.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public History create(@RequestBody History h) {
        return repo.save(h);
    }

    @PutMapping("/{id}")
    public ResponseEntity<History> update(@PathVariable Integer id, @RequestBody History h) {
        return repo.findById(id).map(ex -> {
            h.setIdPesanan(id);
            repo.save(h);
            return ResponseEntity.ok(h);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return repo.findById(id).map(ex -> {
            repo.deleteById(id);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}