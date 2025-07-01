package com.mycompany.mavenproject3;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/api/products";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Product> getAllProducts() throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (InputStream is = con.getInputStream()) {
            return mapper.readValue(is, new TypeReference<List<Product>>() {});
        }
    }

    public static void addProduct(Product product) throws IOException {
        // Pastikan ID-nya null sebelum dikirim
        product.setId(null);

        URL url = new URL(BASE_URL);
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
        if (product.getId() == 0) {
            throw new IOException("ID produk kosong atau 0!");
        }

        String url = BASE_URL + "/" + product.getId();
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
        String url = BASE_URL + "/" + productId;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new IOException("Gagal menghapus produk. Kode response: " + responseCode + "\n" + readError(conn));
        }
    }

    private static String readError(HttpURLConnection con) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getErrorStream()))) {
            return reader.lines().reduce("", (acc, line) -> acc + line + "\n");
        } catch (IOException e) {
            return "Gagal membaca pesan error.";
        }
    }
}
