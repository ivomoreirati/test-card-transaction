package br.com.authorize.services.impl;

import br.com.authorize.services.AccountService;
import br.com.authorize.services.AuthorizeService;
import br.com.authorize.services.TransactionService;
import br.com.authorize.utils.OperationResponseUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class AuthorizeServiceTest {

    AuthorizeService authorizeService;

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionService transactionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.authorizeService = new AuthorizeServiceImpl(accountService, transactionService);
    }

    @Test
    public void testReceiveSuccess() throws IOException {
        final var in = OperationResponseUtils.getInputFileMockOperations();
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            result.append(authorizeService.receive(line));
        }
        assertTrue(!result.toString().contains("Please, input again some valid object!"));
    }

    @Test
    public void testReceiveWithoutSuccess() throws IOException {
        final var in = OperationResponseUtils.getInputFileMockOperationsError();
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            result.append(authorizeService.receive(line));
        }
        assertTrue(result.toString().contains("Please, input again some valid object!"));
    }
}