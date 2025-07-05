package com.mycompany.mavenproject3;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RegisForm extends JFrame {
    private JTextField customerField;
    private JTextField nohpField;
    private JTextField emailField;
    private JTextField passwordField;
    private JComboBox<String> genderBox;
    private JButton regisButton;

    private List<Customer> customers;
    private Mavenproject3 mainApp;

    public RegisForm(Mavenproject3 mainApp) {
        this.mainApp = mainApp;
        this.customers = mainApp.getCustomerList();

        setTitle("WK. Cuan | Registrasi");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel dan layout
        JPanel regisPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Field Nama
        gbc.gridx = 0; gbc.gridy = 0;
        regisPanel.add(new JLabel("Nama:"), gbc);
        customerField = new JTextField(15);
        gbc.gridx = 1;
        regisPanel.add(customerField, gbc);

        // Field Gender
        gbc.gridx = 0; gbc.gridy = 1;
        regisPanel.add(new JLabel("Gender:"), gbc);
        genderBox = new JComboBox<>(new String[]{"Pria", "Wanita"});
        gbc.gridx = 1;
        regisPanel.add(genderBox, gbc);

        // Field No HP
        gbc.gridx = 0; gbc.gridy = 2;
        regisPanel.add(new JLabel("Nomor HP:"), gbc);
        nohpField = new JTextField(15);
        gbc.gridx = 1;
        regisPanel.add(nohpField, gbc);

        // Field Email
        gbc.gridx = 0; gbc.gridy = 3;
        regisPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(15);
        gbc.gridx = 1;
        regisPanel.add(emailField, gbc);

        // Field Password
        gbc.gridx = 0; gbc.gridy = 4;
        regisPanel.add(new JLabel("Password:"), gbc);
        passwordField = new JTextField(15);
        gbc.gridx = 1;
        regisPanel.add(passwordField, gbc);

        // Tombol Registrasi
        regisButton = new JButton("Buat Akun");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        regisPanel.add(regisButton, gbc);

        add(regisPanel);

        // Aksi tombol
        regisButton.addActionListener(e -> handleRegistration());

        setVisible(true);
    }

    private void handleRegistration() {
        String nama = customerField.getText().trim();
        String nohp = nohpField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String gender = (String) genderBox.getSelectedItem();

        if (nama.isEmpty() || nohp.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean genderValue = "Pria".equalsIgnoreCase(gender);

        try {
            // ID diset null agar backend yang generate
            Customer newCustomer = new Customer(null, nama, nohp, email, password, genderValue);
            ApiClient.addCustomer(newCustomer); // simpan via API

            JOptionPane.showMessageDialog(this, "Registrasi berhasil!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Tutup form
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Registrasi gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
