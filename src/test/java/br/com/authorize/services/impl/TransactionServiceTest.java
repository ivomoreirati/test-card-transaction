package br.com.authorize.services.impl;

import br.com.authorize.entities.Transaction;
import br.com.authorize.repositories.TransactionRepository;
import br.com.authorize.services.AccountService;
import br.com.authorize.services.TransactionService;
import br.com.authorize.services.valitations.transaction.CheckHighFrequencyIntervalValidation;
import br.com.authorize.services.valitations.transaction.CheckTransactionValidation;
import br.com.authorize.utils.OperationResponseUtils;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

public class TransactionServiceTest {

    @Mock
    private AccountService accountService;

    private TransactionService transactionService;

    @Mock
    TransactionRepository transactionRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        List<CheckTransactionValidation> validations = new ArrayList<>();
        final var validation = new CheckHighFrequencyIntervalValidation();
        ReflectionTestUtils.setField(validation, "countControlTransaction", 3);
        ReflectionTestUtils.setField(validation, "minuteControlTransaction", 2);
        validations = Stream.of(validation).collect(Collectors.toList());
        this.transactionService = new TransactionServiceImpl(validations, transactionRepository, accountService);
    }

    @Test
    public void testProcessWithViolation() throws IOException, JSONException {
        final var transaction = OperationResponseUtils.getTransaction();
        List<Transaction> transactionList = new ArrayList<>();
        final var transaction1 = new Transaction();
        transaction1.setDateCreated(LocalDateTime.now().minusMinutes(1));
        transactionList.add(transaction1);
        final var transaction2 = new Transaction();
        transaction2.setDateCreated(LocalDateTime.now().minusMinutes(1));
        transactionList.add(transaction2);
        final var transaction3 = new Transaction();
        transaction3.setDateCreated(LocalDateTime.now().minusMinutes(1));
        transactionList.add(transaction3);

        doReturn(transactionList).when(transactionRepository).findAll();

        final var result = transactionService.process(transaction);
        assertFalse(result.getViolations().isEmpty());
    }

    @Test
    public void testProcessWithoutViolation() throws IOException, JSONException {
        final var transaction = OperationResponseUtils.getTransaction();
        doReturn(new ArrayList<>()).when(transactionRepository).findAll();

        final var result = transactionService.process(transaction);
        assertTrue(result.getViolations().isEmpty());
    }
}