package com.mycompany.mavenproject3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerForm extends JFrame {
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, nohpField, emailField, passField;
    private JComboBox<String> genderBox;
    private JButton saveButton, editButton, deleteButton;
    private List<Customer> customers;

    private Customer selectedCustomer = null;

    public CustomerForm(Mavenproject3 mainApp) {
        setTitle("WK. Cuan | Daftar Customer");
        setSize(650, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Kelola Customer", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = addLabeledField("Nama:", formPanel, gbc, 0);
        nohpField = addLabeledField("Nomor HP:", formPanel, gbc, 1);
        emailField = addLabeledField("Email:", formPanel, gbc, 2);
        passField = addLabeledField("Password:", formPanel, gbc, 3);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Gender:"), gbc);
        genderBox = new JComboBox<>(new String[]{"Pria", "Wanita"});
        gbc.gridx = 1;
        formPanel.add(genderBox, gbc);

        saveButton = new JButton("Simpan");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Hapus");

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(saveButton, gbc);
        gbc.gridy = 6;
        formPanel.add(editButton, gbc);
        gbc.gridy = 7;
        formPanel.add(deleteButton, gbc);

        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "Nomor HP", "Email", "Password", "Gender"}, 0);
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);

        loadCustomerData();

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(formPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        setupActions();
    }

    private JTextField addLabeledField(String label, JPanel panel, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        JTextField field = new JTextField(10);
        gbc.gridx = 1;
        panel.add(field, gbc);
        return field;
    }

    private void setupActions() {
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String nohp = nohpField.getText().trim();
            String email = emailField.getText().trim();
            String password = passField.getText().trim();
            boolean isMale = "Pria".equals(genderBox.getSelectedItem());

            if (name.isEmpty() || nohp.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                if (selectedCustomer != null) {
                    selectedCustomer.setName(name);
                    selectedCustomer.setNohp(nohp);
                    selectedCustomer.setEmail(email);
                    selectedCustomer.setPassword(password);
                    selectedCustomer.setGender(isMale);
                    ApiClient.updateCustomer(selectedCustomer);
                    JOptionPane.showMessageDialog(this, "Customer berhasil diperbarui.");
                } else {
                    Customer newCustomer = new Customer(null, name, nohp, email, password, isMale);
                    ApiClient.addCustomer(newCustomer);
                    JOptionPane.showMessageDialog(this, "Customer baru berhasil ditambahkan.");
                }

                loadCustomerData();
                clearForm();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) {
                Long id = Long.parseLong(customerTable.getValueAt(selectedRow, 0).toString());
                selectedCustomer = customers.stream()
                        .filter(c -> c.getId().equals(id))
                        .findFirst()
                        .orElse(null);

                if (selectedCustomer != null) {
                    nameField.setText(selectedCustomer.getName());
                    nohpField.setText(selectedCustomer.getNohp());
                    emailField.setText(selectedCustomer.getEmail());
                    passField.setText(selectedCustomer.getPassword());
                    genderBox.setSelectedItem(selectedCustomer.getGenderString().equals("Male") ? "Pria" : "Wanita");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) {
                Long id = Long.parseLong(customerTable.getValueAt(selectedRow, 0).toString());
                try {
                    ApiClient.deleteCustomer(id);
                    JOptionPane.showMessageDialog(this, "Customer berhasil dihapus.");
                    loadCustomerData();
                    clearForm();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Gagal menghapus: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void loadCustomerData() {
        try {
            customers = ApiClient.getAllCustomers();
            tableModel.setRowCount(0);
            for (Customer c : customers) {
                addTableRow(c);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTableRow(Customer customer) {
        tableModel.addRow(new Object[]{
                customer.getId(),
                customer.getName(),
                customer.getNohp(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getGenderString()
        });
    }

    private void clearForm() {
        nameField.setText("");
        nohpField.setText("");
        emailField.setText("");
        passField.setText("");
        genderBox.setSelectedIndex(0);
        customerTable.clearSelection();
        selectedCustomer = null;
    }
}