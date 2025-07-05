package com.mycompany.mavenproject3;

/**
 * Kelas Unit merepresentasikan satuan pembelian produk,
 * seperti "Botol", "Box", dll, dan dapat digunakan untuk
 * menghitung total harga berdasarkan satuan tersebut.
 */
public class Unit {
    private String satuan;
    private int jumlah; // jumlah botol per satuan (contoh: Box = 12)

    // Konstruktor kosong - diperlukan jika ingin gunakan JSON deserialisasi
    public Unit() {
    }

    public Unit(String satuan, int jumlah) {
        this.satuan = satuan;
        this.jumlah = jumlah;
    }

    public String getSatuan() { return satuan; }
    public int getJumlah() { return jumlah; }

    public void setSatuan(String satuan) { this.satuan = satuan; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    /**
     * Menghitung harga total berdasarkan jumlah dan diskon satuan.
     * @param product Produk yang akan dihitung harganya
     * @return harga total
     */
    public double getHargaDariProduk(Product product) {
        double basePrice = product.getPrice();
        double discount = satuan.equalsIgnoreCase("Box") ? 0.9 : 1.0; // 10% diskon untuk Box
        return basePrice * jumlah * discount;
    }

    @Override
    public String toString() {
        return satuan + " (" + jumlah + " botol)";
    }
}
