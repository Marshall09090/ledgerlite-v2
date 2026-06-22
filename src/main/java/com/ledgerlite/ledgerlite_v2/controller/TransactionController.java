package com.ledgerlite.ledgerlite_v2.controller;

import com.ledgerlite.ledgerlite_v2.model.Transaction;
import com.ledgerlite.ledgerlite_v2.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;import org.springframework.http.HttpStatus;


import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Transaction> getAll() {
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<Transaction> create(@Valid @RequestBody Transaction transaction) {
        Transaction saved = service.create(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.ok().build();
        }

}