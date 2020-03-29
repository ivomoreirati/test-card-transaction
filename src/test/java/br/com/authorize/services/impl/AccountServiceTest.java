package br.com.authorize.services.impl;

import br.com.authorize.entities.Account;
import br.com.authorize.repositories.AccountRepository;
import br.com.authorize.services.AccountService;
import br.com.authorize.services.valitations.account.CheckAccountCreatedValidation;
import br.com.authorize.services.valitations.account.CheckAccountValidation;
import br.com.authorize.utils.OperationResponseUtils;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

public class AccountServiceTest {

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
    public void testProcessWithViolation() throws IOException, JSONException {
        final var account = OperationResponseUtils.getAccount();
        List<Account> accounts = new ArrayList<>();
        accounts.add(Account.builder().id(BigInteger.ONE).build());

        doReturn(accounts).when(accountRepository).findAll();

        final var result = accountService.process(account);
        assertFalse(result.getViolations().isEmpty());
    }

    @Test
    public void testProcessWithoutViolation() throws IOException, JSONException {
        final var account = OperationResponseUtils.getAccount();
        doReturn(new ArrayList<>()).when(accountRepository).findAll();

        final var result = accountService.process(account);
        assertTrue(result.getViolations().isEmpty());
    }
}