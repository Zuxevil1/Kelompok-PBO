package com.mycompany.mavenproject3;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class SellForm extends JFrame {
    private JComboBox<String> productBox;
    private JComboBox<String> unitBox;
    private JComboBox<String> customerBox;
    private JTextField stockField;
    private JTextField priceField;
    private JTextField qtyField;
    private JButton processButton;

    private List<Product> products;
    private List<Unit> units;
    private List<Customer> customers;
    private Mavenproject3 mainApp;

    public SellForm(Mavenproject3 mainApp) {
        this.mainApp = mainApp;
        this.products = mainApp.getProductList();
        this.customers = mainApp.getCustomerList();
        this.units = List.of(new Unit("Botol", 1), new Unit("Box", 12));

        setTitle("Form Penjualan");
        setSize(300, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        updateInfo(); // Inisialisasi info produk saat pertama kali dibuka
    }

    private void initUI() {
        JPanel sellPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ComboBox Customer
        gbc.gridx = 0; gbc.gridy = 0;
        sellPanel.add(new JLabel("Customer:"), gbc);
        customerBox = new JComboBox<>();
        customers.forEach(c -> customerBox.addItem(c.getName()));
        gbc.gridx = 1;
        sellPanel.add(customerBox, gbc);

        // ComboBox Produk
        gbc.gridx = 0; gbc.gridy = 1;
        sellPanel.add(new JLabel("Barang:"), gbc);
        productBox = new JComboBox<>();
        products.forEach(p -> productBox.addItem(p.getName()));
        gbc.gridx = 1;
        sellPanel.add(productBox, gbc);

        // Field Stok
        gbc.gridx = 0; gbc.gridy = 2;
        sellPanel.add(new JLabel("Stok Tersedia:"), gbc);
        stockField = new JTextField(10);
        stockField.setEditable(false);
        gbc.gridx = 1;
        sellPanel.add(stockField, gbc);

        // Field Harga
        gbc.gridx = 0; gbc.gridy = 3;
        sellPanel.add(new JLabel("Harga Jual:"), gbc);
        priceField = new JTextField(10);
        priceField.setEditable(false);
        gbc.gridx = 1;
        sellPanel.add(priceField, gbc);

        // ComboBox Satuan
        gbc.gridx = 0; gbc.gridy = 4;
        sellPanel.add(new JLabel("Satuan:"), gbc);
        unitBox = new JComboBox<>();
        units.forEach(u -> unitBox.addItem(u.getSatuan()));
        gbc.gridx = 1;
        sellPanel.add(unitBox, gbc);

        // Qty
        gbc.gridx = 0; gbc.gridy = 5;
        sellPanel.add(new JLabel("Qty:"), gbc);
        qtyField = new JTextField(10);
        gbc.gridx = 1;
        sellPanel.add(qtyField, gbc);

        // Tombol Proses
        processButton = new JButton("Proses");
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        sellPanel.add(processButton, gbc);

        add(sellPanel);

        // Event listener
        productBox.addActionListener(e -> updateInfo());
        unitBox.addActionListener(e -> updateInfo());
        processButton.addActionListener(e -> processTransaction());
    }

    private void updateInfo() {
        Product selectedProduct = getSelectedProduct();
        Unit selectedUnit = getSelectedUnit();

        if (selectedProduct != null && selectedUnit != null) {
            int stockInUnit = selectedProduct.getStock() / selectedUnit.getJumlah();
            double pricePerUnit = selectedProduct.getPrice() * selectedUnit.getJumlah();

            stockField.setText(String.valueOf(stockInUnit));
            priceField.setText(formatRupiah(pricePerUnit));
        }
    }

    private void processTransaction() {
        try {
            Product selectedProduct = getSelectedProduct();
            Unit selectedUnit = getSelectedUnit();
            String customerName = (String) customerBox.getSelectedItem();

            if (selectedProduct == null || selectedUnit == null || customerName == null) {
                JOptionPane.showMessageDialog(this, "Data tidak lengkap!");
                return;
            }

            int qty = Integer.parseInt(qtyField.getText());
            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Qty harus lebih dari 0");
                return;
            }

            int totalBotol = qty * selectedUnit.getJumlah();
            double totalHarga = totalBotol * selectedProduct.getPrice();

            if (selectedProduct.getStock() >= totalBotol) {
                // Kurangi stok produk
                selectedProduct.setStock(selectedProduct.getStock() - totalBotol);
                ApiClient.updateProduct(selectedProduct); // Simpan ke server

                // Simpan ke history
                History history = new History(
                        null,
                        customerName,
                        selectedProduct.getName(),
                        totalBotol,
                        totalHarga,
                        LocalDateTime.now().withNano(0)
                );
                ApiClient.addHistory(history);

                mainApp.refreshBanner();

                JOptionPane.showMessageDialog(this, "Transaksi berhasil\nTotal: " + formatRupiah(totalHarga));
                qtyField.setText("");
                updateInfo();
            } else {
                JOptionPane.showMessageDialog(this, "Stok tidak mencukupi!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Qty harus berupa angka!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage());
        }
    }

    private Product getSelectedProduct() {
        String name = (String) productBox.getSelectedItem();
        return products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private Unit getSelectedUnit() {
        String name = (String) unitBox.getSelectedItem();
        return units.stream()
                .filter(u -> u.getSatuan().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private String formatRupiah(double harga) {
        return java.text.NumberFormat
                .getCurrencyInstance(java.util.Locale.forLanguageTag("id-ID"))
                .format(harga)
                .replace(",00", "");
    }
}
