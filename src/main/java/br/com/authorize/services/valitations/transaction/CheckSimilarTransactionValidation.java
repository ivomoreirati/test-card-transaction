package br.com.authorize.services.valitations.transaction;

import br.com.authorize.constants.Violation;
import br.com.authorize.entities.Transaction;
import br.com.authorize.services.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Service
public class CheckSimilarTransactionValidation implements CheckTransactionValidation {

    @Value("${control-interval-transaction.minute}")
    private long minuteControlTransaction;

    @Override
    public CompletableFuture<Boolean> valid(TransactionService transactionService, Transaction transaction, Set<String> violations) {
        final var transactionList = transactionService.getTransactionsByInterval(minuteControlTransaction);

        if(!isEmpty(transactionList)) {
            if(transactionList.contains(transaction)) {
                violations.add(Violation.Transaction.DOUBLED_TRANSACTION);
                CompletableFuture.completedFuture(false);
            }
        }

        return CompletableFuture.completedFuture(true);
    }
}