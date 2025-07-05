package com.example.backend.controller;

import com.example.backend.model.Customer;
import com.example.backend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerRepository.save(customer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Customer updated) {
        return customerRepository.findById(id).map(customer -> {
            customer.setName(updated.getName());
            customer.setNohp(updated.getNohp());
            customer.setEmail(updated.getEmail());
            customer.setPassword(updated.getPassword());
            customer.setGender(updated.getGender());
            return ResponseEntity.ok(customerRepository.save(customer));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

