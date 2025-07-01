package com.mycompany.mavenproject3;

public class Customer {
    private int id;
    private String name;
    private String nohp;
    private String email;
    private String password;
    private boolean gender;

    // Konstruktor kosong untuk deserialisasi JSON
    public Customer() {
    }

    public Customer(int id, String name, String nohp, String email, String password, boolean gender) {
        this.id = id;
        this.name = name;
        this.nohp = nohp;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }

    // Getter
    public int getId() { return id; }
    public String getName() { return name; }
    public String getNohp() { return nohp; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean getGender() { return gender; }

    // Setter
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setNohp(String nohp) { this.nohp = nohp; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setGender(boolean gender) { this.gender = gender; }

    // Ekstra: representasi gender sebagai string
    public String getGenderString() {
        return gender ? "Male" : "Female";
    }

    // Opsional: untuk debugging atau tampil di combo box
    @Override
    public String toString() {
        return name + " (" + nohp + ")";
    }
}
