package com.ledgerlite.ledgerlite_v2.controller;

import com.ledgerlite.ledgerlite_v2.model.Transaction;
import com.ledgerlite.ledgerlite_v2.repository.TransactionRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    private final TransactionRepository repository;
    public TransactionController(TransactionRepository repository){
        this.repository=repository;
    }
    @GetMapping
    public List<Transaction> getAll(){
        return repository.findAll();
    }
    @PostMapping
    public Transaction create(@RequestBody Transaction t ){
        return repository.save(t);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        repository.deleteById(id);

    }
}
