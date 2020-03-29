package br.com.authorize.services.valitations.account;

import br.com.authorize.constants.Violation;
import br.com.authorize.entities.Account;
import br.com.authorize.services.AccountService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.isNull;

@Service
public class CheckAccountCreatedValidation implements CheckAccountValidation {

    @Override
    public CompletableFuture<Boolean> valid(AccountService accountService, Account account, Set<String> violations) {
        if(!isNull(accountService.findAccount())){
            violations.add(Violation.Account.ACCOUNT_ALREADY_INITIALIZED);
            CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.completedFuture(true);
    }
}