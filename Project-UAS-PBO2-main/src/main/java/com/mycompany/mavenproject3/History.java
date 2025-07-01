package com.mycompany.mavenproject3;

import java.time.LocalDateTime;

/**
 * Model untuk menyimpan riwayat penjualan.
 */
public class History {
    private int idPesanan;
    private String namaCustomer;
    private String namaProduk;
    private int jumlah;
    private double jumlahPembayaran;
    private LocalDateTime waktu;

    // Konstruktor kosong (dibutuhkan untuk deserialisasi JSON jika menggunakan API)
    public History() {
    }

    public History(int idPesanan, String namaCustomer, String namaProduk, int jumlah, double jumlahPembayaran, LocalDateTime waktu) {
        this.idPesanan = idPesanan;
        this.namaCustomer = namaCustomer;
        this.namaProduk = namaProduk;
        this.jumlah = jumlah;
        this.jumlahPembayaran = jumlahPembayaran;
        this.waktu = waktu;
    }

    // Getter
    public int getIdPesanan() { return idPesanan; }
    public String getNamaCustomer() { return namaCustomer; }
    public String getNamaProduk() { return namaProduk; }
    public int getJumlah() { return jumlah; }
    public double getJumlahPembayaran() { return jumlahPembayaran; }
    public LocalDateTime getWaktu() { return waktu; }

    // Setter
    public void setIdPesanan(int idPesanan) { this.idPesanan = idPesanan; }
    public void setNamaCustomer(String namaCustomer) { this.namaCustomer = namaCustomer; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public void setJumlahPembayaran(double jumlahPembayaran) { this.jumlahPembayaran = jumlahPembayaran; }
    public void setWaktu(LocalDateTime waktu) { this.waktu = waktu; }

    // Opsional: untuk tampilan
    @Override
    public String toString() {
        return String.format("Pesanan #%d - %s - %s - %d pcs - Rp%.0f", idPesanan, namaCustomer, namaProduk, jumlah, jumlahPembayaran);
    }
}
