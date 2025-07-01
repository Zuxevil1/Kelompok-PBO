package com.mycompany.mavenproject3;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mavenproject3 extends JFrame {
    private JButton addProductButton, viewCustomerButton, sellProductButton, reportButton, regisButton;
    private final List<Product> productList = new ArrayList<>();
    private final List<Customer> customerList = new ArrayList<>();
    private final List<History> historyList = new ArrayList<>();
    private JPanel menuPanel;

    public Mavenproject3() {
        setTitle("WK. STI Chill | Tampilan Penjual");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel Navigasi Kiri
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 4, 8, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Home");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        leftPanel.add(titleLabel, gbc);

        // Inisialisasi tombol navigasi
        addProductButton = createNavButton("Kelola Produk", leftPanel, gbc, 1);
        viewCustomerButton = createNavButton("Kelola Customer", leftPanel, gbc, 2);
        sellProductButton = createNavButton("Jual Produk", leftPanel, gbc, 3);
        reportButton = createNavButton("Laporan Penjualan", leftPanel, gbc, 4);
        regisButton = createNavButton("Registrasi Akun", leftPanel, gbc, 5);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(leftPanel, BorderLayout.NORTH);
        wrapperPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));

        add(wrapperPanel, BorderLayout.WEST);

        menuPanel = new JPanel(new GridBagLayout());
        add(menuPanel, BorderLayout.CENTER);

        // Event Listener
        addProductButton.addActionListener(e -> new ProductForm(this).setVisible(true));
        viewCustomerButton.addActionListener(e -> new CustomerForm(this).setVisible(true));
        sellProductButton.addActionListener(e -> new SellForm(this).setVisible(true));
        reportButton.addActionListener(e -> new HistoryForm(this).setVisible(true));
        regisButton.addActionListener(e -> new RegisForm(this).setVisible(true));

        // Muat produk awal
        refreshBanner();

        setVisible(true);
    }

    private JButton createNavButton(String label, JPanel panel, GridBagConstraints gbc, int gridY) {
        gbc.gridx = 0;
        gbc.gridy = gridY;
        JButton button = new JButton(label);
        panel.add(button, gbc);
        return button;
    }

    public void refreshBanner() {
        menuPanel.removeAll();

        List<Product> freshProductList;
        try {
            freshProductList = ApiClient.getAllProducts();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat produk dari server: " + e.getMessage());
            return;
        }

        productList.clear();
        productList.addAll(freshProductList);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 25, 8, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] categories = { "Coffee", "Dairy", "Juice", "Soda", "Tea" };

        for (int col = 0; col < categories.length; col++) {
            String category = categories[col];
            gbc.gridx = col;
            gbc.gridy = 0;

            JLabel categoryLabel = new JLabel(category, SwingConstants.CENTER);
            categoryLabel.setFont(new Font("Arial", Font.BOLD, 20));
            menuPanel.add(categoryLabel, gbc);

            int row = 1;
            for (Product p : productList) {
                if (category.equalsIgnoreCase(p.getCategory()) && p.getStock() > 0) {
                    gbc.gridy = row++;
                    menuPanel.add(new JLabel(p.getName()), gbc);
                }
            }
        }

        menuPanel.revalidate();
        menuPanel.repaint();
    }

    // Getter dan Helper
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList.clear();
        this.productList.addAll(productList);
        refreshBanner();
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public List<History> getHistoryList() {
        return historyList;
    }

    public void addHistory(History h) {
        historyList.add(h);
    }

    public String getPriceByProductName(String productName) {
        return productList.stream()
                .filter(p -> p.getName().equalsIgnoreCase(productName))
                .map(p -> String.valueOf(p.getPrice()))
                .findFirst()
                .orElse("Product not found");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Mavenproject3::new);
    }
}
