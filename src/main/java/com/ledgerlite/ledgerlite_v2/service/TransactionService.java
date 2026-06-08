package com.ledgerlite.ledgerlite_v2.service;

import com.ledgerlite.ledgerlite_v2.model.Transaction;
import com.ledgerlite.ledgerlite_v2.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public List<Transaction> getAll() {
        return repository.findAll();
    }

    public Transaction create(Transaction transaction) {
        return repository.save(transaction);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}