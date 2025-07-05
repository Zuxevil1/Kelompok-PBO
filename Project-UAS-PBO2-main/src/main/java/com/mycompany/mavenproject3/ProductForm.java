package com.mycompany.mavenproject3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductForm extends JFrame {
    private JTable drinkTable;
    private DefaultTableModel tableModel;
    private JTextField codeField, nameField, priceField, stockField;
    private JComboBox<String> categoryField;
    private JButton saveButton, editButton, deleteButton;

    private List<Product> products;
    private boolean isEditMode = false;
    private Long selectedProductId = null;

    private final Mavenproject3 mainApp;

    public ProductForm(Mavenproject3 mainApp) {
        this.mainApp = mainApp;
        setupUI();
        loadProductData();
        setupEventListeners();
    }

    private void setupUI() {
        setTitle("WK. Cuan | Stok Barang");
        setSize(800, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Kelola Produk", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        codeField = addLabeledField(formPanel, "Kode Barang", gbc, 0);
        nameField = addLabeledField(formPanel, "Nama Barang", gbc, 1);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Kategori:"), gbc);
        categoryField = new JComboBox<>(new String[]{"Coffee", "Dairy", "Juice", "Soda", "Tea"});
        gbc.gridx = 1;
        formPanel.add(categoryField, gbc);

        priceField = addLabeledField(formPanel, "Harga Jual", gbc, 3);
        stockField = addLabeledField(formPanel, "Stok Tersedia", gbc, 4);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        saveButton = new JButton("Simpan");
        formPanel.add(saveButton, gbc);

        editButton = new JButton("Edit");
        gbc.gridy = 6;
        formPanel.add(editButton, gbc);

        deleteButton = new JButton("Hapus");
        gbc.gridy = 7;
        formPanel.add(deleteButton, gbc);

        tableModel = new DefaultTableModel(new String[]{"Kode", "Nama", "Kategori", "Harga Jual", "Stok"}, 0);
        drinkTable = new JTable(tableModel);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(formPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(new JScrollPane(drinkTable), BorderLayout.CENTER);
    }

    private JTextField addLabeledField(JPanel panel, String label, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label + ":"), gbc);
        JTextField field = new JTextField(10);
        gbc.gridx = 1;
        panel.add(field, gbc);
        return field;
    }

    private void setupEventListeners() {
        saveButton.addActionListener(this::handleSave);
        editButton.addActionListener(this::handleEdit);
        deleteButton.addActionListener(this::handleDelete);
    }

    private void handleSave(ActionEvent e) {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String category = (String) categoryField.getSelectedItem();
        String priceText = priceField.getText().replace("Rp", "").replace(".", "").trim();
        String stockText = stockField.getText().trim();

        if (code.isEmpty() || name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int stock = Integer.parseInt(stockText);

            Product p = new Product(
                isEditMode ? selectedProductId : null,
                code, name, category, price, stock
            );

            if (isEditMode) {
                ApiClient.updateProduct(p);
            } else {
                ApiClient.addProduct(p);  // Jika gagal, akan lempar exception
            }

            loadProductData(); // <-- Penting: agar tabel selalu ter-refresh dari server
            clearForm();
            isEditMode = false;
            selectedProductId = null;

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan produk: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Format harga/stok tidak valid.");
        }
    }


    private void handleEdit(ActionEvent e) {
        int selectedRow = drinkTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product p = products.get(selectedRow);
            selectedProductId = p.getId();
            codeField.setText(p.getCode());
            nameField.setText(p.getName());
            categoryField.setSelectedItem(p.getCategory());
            priceField.setText(String.valueOf((int) p.getPrice()));
            stockField.setText(String.valueOf(p.getStock()));
            isEditMode = true;
        } else {
            JOptionPane.showMessageDialog(this, "Pilih produk untuk diedit!");
        }
    }

    private void handleDelete(ActionEvent e) {
        int selectedRow = drinkTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product p = products.get(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus produk ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    ApiClient.deleteProduct(p.getId());
                    loadProductData();
                    clearForm();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus produk: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih produk untuk dihapus!");
        }
    }

    private void loadProductData() {
        try {
            products = ApiClient.getAllProducts();
            tableModel.setRowCount(0);
            for (Product p : products) {
                tableModel.addRow(new Object[]{p.getCode(), p.getName(), p.getCategory(), formatRupiah(p.getPrice()), p.getStock()});
            }
            mainApp.setProductList(products);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data produk: " + e.getMessage());
        }
    }

    private void clearForm() {
        codeField.setText("");
        nameField.setText("");
        categoryField.setSelectedIndex(0);
        priceField.setText("");
        stockField.setText("");
        isEditMode = false;
        selectedProductId = null;
        drinkTable.clearSelection();
    }

    private String formatRupiah(double harga) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(harga).replace(",00", "");
    }
}
