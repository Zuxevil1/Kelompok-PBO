package com.mycompany.mavenproject3;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ApiClient {
    private static final String BASE_URL_PRODUCT = "http://localhost:8080/api/products";
    private static final String BASE_URL_CUSTOMER = "http://localhost:8080/api/customers";
    
    private static final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    // =======================
    // 🔹 PRODUCT API SECTION
    // =======================

    public static List<Product> getAllProducts() throws IOException {
        URL url = new URL(BASE_URL_PRODUCT);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (InputStream is = con.getInputStream()) {
            return mapper.readValue(is, new TypeReference<List<Product>>() {});
        }
    }

    public static void addProduct(Product product) throws IOException {
        product.setId(null);

        URL url = new URL(BASE_URL_PRODUCT);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            mapper.writeValue(os, product);
        }

        int responseCode = con.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
            String errorMsg = readError(con);
            throw new IOException("Gagal menambahkan produk. Kode respons: " + responseCode + "\n" + errorMsg);
        }
    }

    public static void updateProduct(Product product) throws IOException {
        if (product.getId() == null || product.getId() == 0) {
            throw new IOException("ID produk kosong atau 0!");
        }

        String url = BASE_URL_PRODUCT + "/" + product.getId();
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            mapper.writeValue(os, product);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new IOException("Gagal update produk. Kode response: " + responseCode + "\n" + readError(conn));
        }
    }

    public static void deleteProduct(Long productId) throws IOException {
        String url = BASE_URL_PRODUCT + "/" + productId;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new IOException("Gagal menghapus produk. Kode response: " + responseCode + "\n" + readError(conn));
        }
    }

    // =======================
    // 🔹 CUSTOMER API SECTION
    // =======================

    public static List<Customer> getAllCustomers() throws IOException {
        URL url = new URL(BASE_URL_CUSTOMER);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (InputStream is = con.getInputStream()) {
            return mapper.readValue(is, new TypeReference<List<Customer>>() {});
        }
    }

    public static void addCustomer(Customer customer) throws IOException {
        URL url = new URL(BASE_URL_CUSTOMER);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            mapper.writeValue(os, customer);
        }

        int responseCode = con.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
            String errorMsg = readError(con);
            throw new IOException("Gagal menambahkan customer. Kode respons: " + responseCode + "\n" + errorMsg);
        }
    }

    public static void updateCustomer(Customer customer) throws IOException {
        if (customer.getId() == null || customer.getId() == 0L) {
            throw new IOException("ID customer kosong atau 0!");
        }

        String url = BASE_URL_CUSTOMER + "/" + customer.getId();
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            mapper.writeValue(os, customer);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new IOException("Gagal update customer. Kode response: " + responseCode + "\n" + readError(conn));
        }
    }

    public static void deleteCustomer(Long customerId) throws IOException {
        String url = BASE_URL_CUSTOMER + "/" + customerId;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new IOException("Gagal menghapus customer. Kode response: " + responseCode + "\n" + readError(conn));
        }
    }

    // ===================
    // 🔹 HISTORY SECTION
    // ===================

    private static final String BASE_URL_HISTORY = "http://localhost:8080/api/history";

    public static List<History> getAllHistory() throws IOException {
        URL url = new URL(BASE_URL_HISTORY);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (InputStream is = con.getInputStream()) {
            return mapper.readValue(is, new TypeReference<List<History>>() {});
        }
    }

    public static void addHistory(History history) throws IOException {
        // Hapus ID sebelum dikirim ke server
        history.setIdPesanan(null);
    
        URL url = new URL(BASE_URL_HISTORY);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
    
        String jsonData = mapper.writeValueAsString(history);
        System.out.println("Sending JSON to /api/history:\n" + jsonData); // DEBUG
    
        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonData.getBytes());
        }
    
        int responseCode = con.getResponseCode();
        String error = readError(con); // selalu baca error untuk logging
    
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
            System.err.println("Error response:\n" + error); // DEBUG
            throw new IOException("Gagal menambahkan history. Kode response: " + responseCode + "\n" + error);
        }
    }
    
    


    // =======================
    // 🔸 Helper Function
    // =======================

    private static String readError(HttpURLConnection con) {
        InputStream errorStream = con.getErrorStream();
        if (errorStream == null) {
            return "Tidak ada error stream dari server (mungkin respons kosong).";
        }
    
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream))) {
            return reader.lines().reduce("", (acc, line) -> acc + line + "\n");
        } catch (IOException e) {
            return "Gagal membaca pesan error.";
        }
    }
    
}
