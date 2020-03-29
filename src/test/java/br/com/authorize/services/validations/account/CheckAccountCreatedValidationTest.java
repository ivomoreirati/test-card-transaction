package br.com.authorize.services.validations.account;

import br.com.authorize.constants.Violation;
import br.com.authorize.entities.Account;
import br.com.authorize.repositories.AccountRepository;
import br.com.authorize.services.AccountService;
import br.com.authorize.services.impl.AccountServiceImpl;
import br.com.authorize.services.valitations.account.CheckAccountCreatedValidation;
import br.com.authorize.services.valitations.account.CheckAccountValidation;
import br.com.authorize.utils.OperationResponseUtils;
import com.google.common.collect.Sets;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

public class CheckAccountCreatedValidationTest {

    @InjectMocks
    private CheckAccountCreatedValidation validation;

    private AccountService accountService;

    @Mock
    AccountRepository accountRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        List<CheckAccountValidation> validations = new ArrayList<>();
        validations = Stream.of(new CheckAccountCreatedValidation()).collect(Collectors.toList());
        this.accountService = new AccountServiceImpl(validations, accountRepository);
    }

    @Test
    public void testCheckAccountCreatedValidationWithViolation() throws IOException, JSONException {
        final var account = OperationResponseUtils.getAccount();
        List<Account> accounts = new ArrayList<>();
        accounts.add(Account.builder().id(BigInteger.ONE).build());

        doReturn(accounts).when(accountRepository).findAll();

        Set<String> violations = Sets.newHashSet();
        validation.valid(accountService, account, violations);
        String violation = Violation.Account.ACCOUNT_ALREADY_INITIALIZED;
        assertTrue(violations.contains(violation));
    }

    @Test
    public void testCheckAccountCreatedValidationWithoutViolation() throws IOException, JSONException {
        final var account = OperationResponseUtils.getAccount();
        List<Account> accounts = new ArrayList<>();

        doReturn(accounts).when(accountRepository).findAll();

        Set<String> violations = Sets.newHashSet();
        validation.valid(accountService, account, violations);
        assertEquals(0, violations.size());
    }
}