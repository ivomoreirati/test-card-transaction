package br.com.authorize.services.valitations.transaction;

import br.com.authorize.constants.Violation;
import br.com.authorize.entities.Transaction;
import br.com.authorize.services.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CheckHighFrequencyIntervalValidation implements CheckTransactionValidation {

    @Value("${control-interval-transaction.count}")
    private int countControlTransaction;

    @Value("${control-interval-transaction.minute}")
    private long minuteControlTransaction;

    @Override
    public CompletableFuture<Boolean> valid(TransactionService transactionService, Transaction transaction, Set<String> violations) {

        final var transactions = transactionService.getTransactionsByInterval(minuteControlTransaction);

        if(!isEmpty(transactions)) {
            if(transactions.size() == countControlTransaction ) {
                violations.add(Violation.Transaction.HIGH_FREQUENCY_SMALL_INTERVAL);
                CompletableFuture.completedFuture(false);
            }
        }

        return CompletableFuture.completedFuture(true);
    }
}