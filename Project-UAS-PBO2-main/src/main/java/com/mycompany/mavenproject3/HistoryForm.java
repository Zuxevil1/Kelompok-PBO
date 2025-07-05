package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HistoryForm extends JFrame {
    private JTable historyTable;
    private DefaultTableModel tableModel;
    private List<History> history;
    private JComboBox<String> filterBox;

    public HistoryForm(Mavenproject3 mainApp) {
        this.history = mainApp.getHistoryList();

        setTitle("WK. Cuan | Riwayat Transaksi");
        setSize(700, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{
            "ID Pesanan", "Nama Customer", "Produk", "Jumlah", "Total Bayar", "Waktu Pemesanan"
        }, 0);
        historyTable = new JTable(tableModel);
        loadHistoryData(history);

        // Panel filter
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterBox = new JComboBox<>(new String[]{"Semua", "Hari Ini", "Minggu Ini", "Bulan Ini"});
        topPanel.add(new JLabel("Filter:"));
        topPanel.add(filterBox);
        add(topPanel, BorderLayout.NORTH);

        // Tabel di tengah
        JScrollPane scrollPane = new JScrollPane(historyTable);
        add(scrollPane, BorderLayout.CENTER);

        // Event filter
        filterBox.addActionListener(e -> applyFilter());
    }

    private void loadHistoryData(List<History> historyList) {
        tableModel.setRowCount(0);
        for (History h : historyList) {
            tableModel.addRow(new Object[]{
                h.getIdPesanan(),
                h.getNamaCustomer(),
                h.getNamaProduk(),
                h.getJumlah() + " Botol",
                formatRupiah(h.getJumlahPembayaran()),
                h.getWaktu()
            });
        }
    }

    private String formatRupiah(double harga) {
        java.text.NumberFormat formatter = java.text.NumberFormat.getCurrencyInstance(
                java.util.Locale.forLanguageTag("id-ID")
        );
        return formatter.format(harga).replace(",00", "");
    }

    private void applyFilter() {
        String selected = (String) filterBox.getSelectedItem();
        List<History> filtered = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (History h : history) {
            LocalDate tanggal = h.getWaktu().toLocalDate();
            switch (selected) {
                case "Hari Ini":
                    if (tanggal.equals(now)) filtered.add(h);
                    break;
                case "Minggu Ini":
                    if (!tanggal.isBefore(now.minusDays(6))) filtered.add(h);
                    break;
                case "Bulan Ini":
                    if (tanggal.getMonth() == now.getMonth() && tanggal.getYear() == now.getYear())
                        filtered.add(h);
                    break;
                default:
                    filtered = history;
                    break;
            }
        }

        loadHistoryData(filtered);
    }
}
