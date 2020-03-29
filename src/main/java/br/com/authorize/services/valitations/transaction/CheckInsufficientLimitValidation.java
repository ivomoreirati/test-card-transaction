package br.com.authorize.services.valitations.transaction;

import br.com.authorize.constants.Violation;
import br.com.authorize.entities.Transaction;
import br.com.authorize.services.AccountService;
import br.com.authorize.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.isNull;

@Service
public class CheckInsufficientLimitValidation implements CheckTransactionValidation {
    @Autowired
    AccountService accountService;

    @Override
    public CompletableFuture<Boolean> valid(TransactionService transactionService, Transaction transaction, Set<String> violations) {
        final var account = accountService.findAccount();
        if(!isNull(account)){
            if(account.getAvailableLimit().compareTo(transaction.getAmount()) == -1){
                violations.add(Violation.Transaction.INSUFFICIENT_LIMIT);
                CompletableFuture.completedFuture(false);
            }
        }

        return CompletableFuture.completedFuture(true);
    }
}