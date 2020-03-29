package br.com.authorize.services.validations.transaction;

import br.com.authorize.constants.Violation;
import br.com.authorize.entities.Transaction;
import br.com.authorize.repositories.AccountRepository;
import br.com.authorize.repositories.TransactionRepository;
import br.com.authorize.services.AccountService;
import br.com.authorize.services.TransactionService;
import br.com.authorize.services.impl.TransactionServiceImpl;
import br.com.authorize.services.valitations.transaction.CheckSimilarTransactionValidation;
import br.com.authorize.services.valitations.transaction.CheckTransactionValidation;
import br.com.authorize.utils.OperationResponseUtils;
import com.google.common.collect.Sets;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

public class CheckSimilarTransactionValidationTest {

    @InjectMocks
    private CheckSimilarTransactionValidation validation;

    private TransactionService transactionService;

    @Mock
    private AccountService accountService;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AccountRepository accountRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        List<CheckTransactionValidation> validations = new ArrayList<>();
        validations = Stream.of(new CheckSimilarTransactionValidation()).collect(Collectors.toList());
        this.transactionService = new TransactionServiceImpl(validations, transactionRepository, accountService);
        ReflectionTestUtils.setField(validation, "minuteControlTransaction", 2);
    }

    @Test
    public void testCheckSimilarTransactionValidationWithViolation() throws IOException, JSONException {
        final var transaction = OperationResponseUtils.getTransaction();
        List<Transaction> transactionList = new ArrayList<>();
        final var transaction1 = new Transaction();
        transaction1.setAmount(new BigInteger("20"));
        transaction1.setMerchant("1");
        transaction1.setDateCreated(LocalDateTime.now().minusMinutes(1));
        transactionList.add(transaction1);
        doReturn(transactionList).when(transactionRepository).findAll();

        Set<String> violations = Sets.newHashSet();
        validation.valid(transactionService, transaction, violations);
        String violation = Violation.Transaction.DOUBLED_TRANSACTION;
        assertTrue(violations.contains(violation));
    }

    @Test
    public void testCheckSimilarTransactionValidationWithoutViolation() throws IOException, JSONException {
        final var transaction = OperationResponseUtils.getTransaction();
        List<Transaction> transactionList = new ArrayList<>();
        final var transaction1 = new Transaction();
        transaction1.setAmount(new BigInteger("10"));
        transaction1.setMerchant("1");
        transaction1.setDateCreated(LocalDateTime.now().minusMinutes(1));
        transactionList.add(transaction1);
        doReturn(transactionList).when(transactionRepository).findAll();

        Set<String> violations = Sets.newHashSet();
        validation.valid(transactionService, transaction, violations);
        assertEquals(0, violations.size());
    }
}