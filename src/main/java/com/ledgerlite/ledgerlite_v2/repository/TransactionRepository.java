package com.ledgerlite.ledgerlite_v2.repository;

import com.ledgerlite.ledgerlite_v2.model.Transaction;
import  org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository <Transaction,Long>{
}
