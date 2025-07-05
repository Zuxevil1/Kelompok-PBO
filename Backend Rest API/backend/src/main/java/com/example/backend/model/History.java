package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPesanan;

    private String namaCustomer;
    private String namaProduk;
    private int jumlah;
    private double jumlahPembayaran;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime waktu;

    // Constructors
    public History() {}

    public History(Long idPesanan, String namaCustomer, String namaProduk, int jumlah, double jumlahPembayaran, LocalDateTime waktu) {
        this.idPesanan = idPesanan;
        this.namaCustomer = namaCustomer;
        this.namaProduk = namaProduk;
        this.jumlah = jumlah;
        this.jumlahPembayaran = jumlahPembayaran;
        this.waktu = waktu;
    }

    // Getters and Setters
    public Long getIdPesanan() { return idPesanan; }
    public void setIdPesanan(Long idPesanan) { this.idPesanan = idPesanan; }

    public String getNamaCustomer() { return namaCustomer; }
    public void setNamaCustomer(String namaCustomer) { this.namaCustomer = namaCustomer; }

    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    public double getJumlahPembayaran() { return jumlahPembayaran; }
    public void setJumlahPembayaran(double jumlahPembayaran) { this.jumlahPembayaran = jumlahPembayaran; }

    public LocalDateTime getWaktu() { return waktu; }
    public void setWaktu(LocalDateTime waktu) { this.waktu = waktu; }
}
