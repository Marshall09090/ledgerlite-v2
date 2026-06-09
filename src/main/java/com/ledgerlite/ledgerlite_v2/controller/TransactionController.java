package com.ledgerlite.ledgerlite_v2.controller;

import com.ledgerlite.ledgerlite_v2.model.Transaction;
import com.ledgerlite.ledgerlite_v2.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    public Transaction create(@Valid @RequestBody Transaction transaction) {
        return service.create(transaction);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}