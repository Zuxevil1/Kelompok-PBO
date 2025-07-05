package com.example.backend.controller;

import com.example.backend.model.Product;
import com.example.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Product product) {
        Optional<Product> existing = productRepository.findAll().stream()
                .filter(p -> p.getCode().equalsIgnoreCase(product.getCode()))
                .findFirst();
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("Kode produk sudah ada!");
        }

        return ResponseEntity.ok(productRepository.save(product));
    }

        @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productRepository.findById(id).map(product -> {
            product.setCode(updatedProduct.getCode());
            product.setName(updatedProduct.getName());
            product.setCategory(updatedProduct.getCategory());
            product.setPrice(updatedProduct.getPrice());
            product.setStock(updatedProduct.getStock());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Produk dengan ID " + id + " tidak ditemukan."));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
