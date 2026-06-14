package com.ledgerlite.ledgerlite_v2;

import com.ledgerlite.ledgerlite_v2.model.Transaction;
import com.ledgerlite.ledgerlite_v2.repository.TransactionRepository;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import java.util.List;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TransactionRepositoryTest{
    @Autowired
    private TransactionRepository repository;
    @Test
    void shouldSaveAndFindTransaction(){
        Transaction t = new Transaction();
        t.setDescription("Salary");
        t.setAmount(500);
        t.setDate(LocalDate.now());
        t.setType("INCOME");

        repository.save(t);
        List<Transaction> all = repository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getDescription()).isEqualTo("Salary");

    }
}
