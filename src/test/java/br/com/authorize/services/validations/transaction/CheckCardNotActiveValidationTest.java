package br.com.authorize.services.validations.transaction;

import br.com.authorize.constants.Violation;
import br.com.authorize.entities.Account;
import br.com.authorize.repositories.AccountRepository;
import br.com.authorize.repositories.TransactionRepository;
import br.com.authorize.services.AccountService;
import br.com.authorize.services.TransactionService;
import br.com.authorize.services.impl.TransactionServiceImpl;
import br.com.authorize.services.valitations.transaction.CheckAccountNotCreatedValidation;
import br.com.authorize.services.valitations.transaction.CheckCardNotActiveValidation;
import br.com.authorize.services.valitations.transaction.CheckTransactionValidation;
import br.com.authorize.utils.OperationResponseUtils;
import com.google.common.collect.Sets;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

public class CheckCardNotActiveValidationTest {

    @InjectMocks
    private CheckCardNotActiveValidation validation;

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
        validations = Stream.of(new CheckAccountNotCreatedValidation()).collect(Collectors.toList());
        this.transactionService = new TransactionServiceImpl(validations, transactionRepository, accountService);
    }

    @Test
    public void testCheckCardNotActiveValidationWithViolation() throws IOException, JSONException {
        final var transaction = OperationResponseUtils.getTransaction();
        doReturn(Account.builder().activeCard(false).build()).when(accountService).findAccount();

        Set<String> violations = Sets.newHashSet();
        validation.valid(transactionService, transaction, violations);
        String violation = Violation.Transaction.CARD_NOT_ACTIVE;
        assertTrue(violations.contains(violation));
    }

    @Test
    public void testCheckCardNotActiveValidationWithoutViolation() throws IOException, JSONException {
        final var transaction = OperationResponseUtils.getTransaction();
        doReturn(Account.builder().activeCard(true).build()).when(accountService).findAccount();

        Set<String> violations = Sets.newHashSet();
        validation.valid(transactionService, transaction, violations);
        assertEquals(0, violations.size());
    }
}