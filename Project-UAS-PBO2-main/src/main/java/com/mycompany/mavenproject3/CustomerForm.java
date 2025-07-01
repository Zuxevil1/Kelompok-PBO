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

    public CustomerForm(Mavenproject3 mainApp) {
        this.customers = mainApp.getCustomerList();

        setTitle("WK. Cuan | Daftar Customer");
        setSize(600, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Kelola Customer", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = addLabeledField("Nama:", formPanel, gbc, 0);
        nohpField = addLabeledField("Nomor HP:", formPanel, gbc, 1);
        emailField = addLabeledField("Email:", formPanel, gbc, 2);
        passField = addLabeledField("Password:", formPanel, gbc, 3);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Gender:"), gbc);
        genderBox = new JComboBox<>(new String[]{"Pria", "Wanita"});
        gbc.gridx = 1;
        formPanel.add(genderBox, gbc);

        saveButton = new JButton("Simpan");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Hapus");

        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(saveButton, gbc);
        gbc.gridy = 6;
        formPanel.add(editButton, gbc);
        gbc.gridy = 7;
        formPanel.add(deleteButton, gbc);

        // Tabel
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "Nomor HP", "Email", "Password", "Gender"}, 0);
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);

        loadCustomerData(customers);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(formPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        setupActions();
    }

    private JTextField addLabeledField(String label, JPanel panel, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y;
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
            String gender = (String) genderBox.getSelectedItem();

            if (name.isEmpty() || nohp.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int selectedRow = customerTable.getSelectedRow();
            boolean isMale = "Pria".equals(gender);

            if (selectedRow != -1) {
                Customer customer = customers.get(selectedRow);
                customer.setName(name);
                customer.setNohp(nohp);
                customer.setEmail(email);
                customer.setPassword(password);
                customer.setGender(isMale);

                updateTableRow(selectedRow, customer);
                JOptionPane.showMessageDialog(this, "Data berhasil diperbarui.");
            } else {
                int newId = customers.size() + 1; // Ganti dengan ID dari server jika pakai API
                Customer newCustomer = new Customer(newId, name, nohp, email, password, isMale);
                customers.add(newCustomer);
                addTableRow(newCustomer);
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan.");
            }

            clearForm();
        });

        editButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) {
                nameField.setText(customerTable.getValueAt(selectedRow, 1).toString());
                nohpField.setText(customerTable.getValueAt(selectedRow, 2).toString());
                emailField.setText(customerTable.getValueAt(selectedRow, 3).toString());
                passField.setText(customerTable.getValueAt(selectedRow, 4).toString());
                genderBox.setSelectedItem(customerTable.getValueAt(selectedRow, 5).toString());
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) {
                customers.remove(selectedRow);
                tableModel.removeRow(selectedRow);
                clearForm();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void loadCustomerData(List<Customer> customerList) {
        for (Customer customer : customerList) {
            addTableRow(customer);
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

    private void updateTableRow(int row, Customer customer) {
        tableModel.setValueAt(customer.getName(), row, 1);
        tableModel.setValueAt(customer.getNohp(), row, 2);
        tableModel.setValueAt(customer.getEmail(), row, 3);
        tableModel.setValueAt(customer.getPassword(), row, 4);
        tableModel.setValueAt(customer.getGenderString(), row, 5);
    }

    private void clearForm() {
        nameField.setText("");
        nohpField.setText("");
        emailField.setText("");
        passField.setText("");
        genderBox.setSelectedIndex(0);
        customerTable.clearSelection();
    }
}
