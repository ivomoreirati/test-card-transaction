package br.com.authorize.services.impl;

import br.com.authorize.dto.AccountResponseDTO;
import br.com.authorize.entities.Account;
import br.com.authorize.entities.Transaction;
import br.com.authorize.repositories.AccountRepository;
import br.com.authorize.services.AccountService;
import br.com.authorize.services.valitations.account.CheckAccountValidation;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;

    private final List<CheckAccountValidation> validations;

    public AccountServiceImpl(List<CheckAccountValidation> validations, AccountRepository accountRepository) {
        this.validations = validations;
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountResponseDTO process(final Account account) {
        final Set<String> violations = Sets.newHashSet();
        final var futures = validations.stream()
                .map(it -> it.valid(this, account, violations))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();

        var accountResult = new Account();

        if(!violations.isEmpty()){
            accountResult = findAccount();
        } else {
            accountResult = accountRepository.save(account);
        }

        return AccountResponseDTO.builder().account(accountResult).violations(violations).build();

    }

    @Override
    public void updateAccount(Transaction transaction, Account accountResult) {
        accountResult.setAvailableLimit(accountResult.getAvailableLimit().subtract(transaction.getAmount()));
        accountRepository.save(accountResult);
    }


    @Override
    public Account findAccount() {
        return accountRepository.findAll().stream().findFirst().orElse(null);
    }
}
