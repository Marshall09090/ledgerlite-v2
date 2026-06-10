package com.ledgerlite.ledgerlite_v2;
import com.ledgerlite.ledgerlite_v2.model.Transaction;
import com.ledgerlite.ledgerlite_v2.repository.TransactionRepository;
import com.ledgerlite.ledgerlite_v2.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionService service;

    @Test
    void getAll_returnsTransactionsFromRepository() {
        // Arrange: build two fake transactions
        Transaction t1 = new Transaction();
        t1.setDescription("Salary");
        Transaction t2 = new Transaction();
        t2.setDescription("Groceries");

        // Tell the FAKE repository how to behave:
        // "when someone calls findAll(), return these two"
        when(repository.findAll()).thenReturn(List.of(t1, t2));

        // Act: call the real service method
        List<Transaction> result = service.getAll();

        // Assert: check the service returned what the repo gave it
        assertEquals(2, result.size());
        assertEquals("Salary", result.get(0).getDescription());
    }

}