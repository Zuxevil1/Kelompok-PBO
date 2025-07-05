package com.example.backend.model;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String category;
    private double price;

    @Transient // Tidak disimpan ke database
    private double originalPrice;

    private int stock;

    // 🔧 Constructor kosong wajib untuk JPA
    public Product() {}

    // 🔧 Constructor penuh — originalPrice diatur otomatis dari price
    public Product(Long id, String code, String name, String category, double price, int stock) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.category = category;
        this.price = price;
        this.originalPrice = price; // <-- inisialisasi wajib
        this.stock = stock;
    }

    // Getter
    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public double getOriginalPrice() { return originalPrice; }
    public int getStock() { return stock; }

    // Setter
    public void setId(Long id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) {
        this.price = price;
        this.originalPrice = price; // sinkronisasi saat setter dipanggil
    }
    public void setOriginalPrice(double originalPrice) { this.originalPrice = originalPrice; }
    public void setStock(int stock) { this.stock = stock; }
}
