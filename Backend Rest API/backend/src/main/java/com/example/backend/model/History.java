package com.example.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPesanan")
    private Integer idPesanan;

    @Column(nullable = false, length = 255)
    private String namacustomer;

    @Column(nullable = false, length = 255)
    private String namaproduk;

    @Column(nullable = false)
    private Integer jumlah;

    @Column(nullable = false)
    private Integer jumlahPembayaran;

    @Column(nullable = false)
    private LocalDateTime waktu;

    // Getters & Setters
    public Integer getIdPesanan() { return idPesanan; }
    public void setIdPesanan(Integer idPesanan) { this.idPesanan = idPesanan; }

    public String getNamacustomer() { return namacustomer; }
    public void setNamacustomer(String namacustomer) { this.namacustomer = namacustomer; }

    public String getNamaproduk() { return namaproduk; }
    public void setNamaproduk(String namaproduk) { this.namaproduk = namaproduk; }

    public Integer getJumlah() { return jumlah; }
    public void setJumlah(Integer jumlah) { this.jumlah = jumlah; }

    public Integer getJumlahPembayaran() { return jumlahPembayaran; }
    public void setJumlahPembayaran(Integer jumlahPembayaran) { this.jumlahPembayaran = jumlahPembayaran; }

    public LocalDateTime getWaktu() { return waktu; }
    public void setWaktu(LocalDateTime waktu) { this.waktu = waktu; }
}