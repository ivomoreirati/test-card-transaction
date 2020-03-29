package br.com.authorize.services.valitations.account;

import br.com.authorize.entities.Account;
import br.com.authorize.services.AccountService;
import org.springframework.scheduling.annotation.Async;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface CheckAccountValidation {

	@Async
	CompletableFuture<Boolean> valid(AccountService service, final Account account, final Set<String> violations);

}
