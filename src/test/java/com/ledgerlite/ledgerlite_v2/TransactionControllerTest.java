import com.ledgerlite.ledgerlite_v2.LedgerliteV2Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.junit.jupiter.api.AfterEach;
import com.ledgerlite.ledgerlite_v2.repository.TransactionRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.ledgerlite.ledgerlite_v2.model.Transaction;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LedgerliteV2Application.class)
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TransactionRepository transactionRepository;

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
    }

    @Test
    void createTransaction_returns200_andEchoesDescription() throws Exception {
        String json = """
                {
                   "description": "Salary",
                   "amount": 5000,
                   "type": "INCOME",
                   "date": "2026-01-15"
                 }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Salary"));
    }

    @Test
    void getAllTransactions_returns200() throws Exception {
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTransaction_returns200_andRemovesIt() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setDescription("Rent");
        transaction.setAmount(1200);
        transaction.setType("EXPENSE");
        transaction.setDate(LocalDate.now());

        Transaction saved = transactionRepository.save(transaction);
        Long id = saved.getId();

        mockMvc.perform(delete("/api/transactions/" + id))
                .andExpect(status().isOk());
        assertFalse(transactionRepository.existsById(id));}

    @Test
    void invalidTransactionReturns400() throws Exception {
        String badJson = "{ \"description\": \"\", \"amount\": -50, \"type\": \"\" }";
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest());
    }
    @Test
    void deleteNonExistentTransaction_returns404() throws Exception {
        mockMvc.perform(delete("/api/transactions/999999"))
                .andExpect(status().isNotFound());
    }
}