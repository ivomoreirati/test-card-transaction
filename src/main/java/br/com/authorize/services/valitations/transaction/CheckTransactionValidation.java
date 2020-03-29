package br.com.authorize.services.valitations.transaction;

import br.com.authorize.entities.Transaction;
import br.com.authorize.services.TransactionService;
import org.springframework.scheduling.annotation.Async;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface CheckTransactionValidation {

	@Async
	CompletableFuture<Boolean> valid(final TransactionService service, final Transaction transaction, final Set<String> violations);
}
