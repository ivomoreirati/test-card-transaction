package br.com.authorize.services.impl;

import br.com.authorize.services.AccountService;
import br.com.authorize.services.AuthorizeService;
import br.com.authorize.services.TransactionService;
import br.com.authorize.util.ParseJson;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    AccountService accountService;

    TransactionService transactionService;

    public AuthorizeServiceImpl(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @Override
    public String receive(final String line) {
        StringBuilder accountResponseResult = new StringBuilder();
        try {
            final JSONObject json = new JSONObject(line);
            final Iterator<?> keys = json.keys();
            keys.forEachRemaining(key -> {
                if (key.equals("account")) {
                    final var account = ParseJson.parseAccount(json.get("account").toString());
                    final var accountResponse = accountService.process(account);
                    accountResponseResult.append(ParseJson.convertObjectToString(accountResponse));
                }
                if (key.equals("transaction")) {
                    final var transaction = ParseJson.parseTransaction(json.get("transaction").toString());
                    final var accountResponse = transactionService.process(transaction);
                    accountResponseResult.append(ParseJson.convertObjectToString(accountResponse));
                }
            });
        } catch (Exception ex) {
            accountResponseResult.append("Please, input again some valid object! " + ex.getMessage());
        }
        return accountResponseResult.toString();
    }
}

