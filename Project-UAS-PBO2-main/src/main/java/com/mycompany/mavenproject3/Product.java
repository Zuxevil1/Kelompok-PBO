package com.mycompany.mavenproject3;

/**
 * Model Produk
 * Merepresentasikan entitas produk untuk aplikasi penjualan.
 */
public class Product {
    private Long id;
    private String code;
    private String name;
    private String category;
    private double price;
    private double originalPrice;
    private int stock;

    // Diperlukan untuk deserialisasi JSON
    public Product() {}

    public Product(Long id, String code, String name, String category, double price, int stock) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.category = category;
        this.price = price;
        this.originalPrice = price;
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
    public void setPrice(double price) { this.price = price; }
    public void setOriginalPrice(double originalPrice) { this.originalPrice = originalPrice; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}
