package com.example.backend.model;

import jakarta.persistence.*;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String nohp;
    private String email;
    private String password;
    private boolean gender;

    public Customer() {}

    public Customer(Long id, String name, String nohp, String email, String password, boolean gender) {
        this.id = id;
        this.name = name;
        this.nohp = nohp;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }

    // Getter
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getNohp() { return nohp; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean getGender() { return gender; }

    // Setter
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setNohp(String nohp) { this.nohp = nohp; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setGender(boolean gender) { this.gender = gender; }

    // Optional: tampilkan dalam bentuk string
    @Override
    public String toString() {
        return name + " (" + nohp + ")";
    }
}
