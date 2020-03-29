package br.com.authorize.services.impl;

import br.com.authorize.dto.AccountResponseDTO;
import br.com.authorize.entities.Account;
import br.com.authorize.entities.Transaction;
import br.com.authorize.repositories.TransactionRepository;
import br.com.authorize.services.AccountService;
import br.com.authorize.services.TransactionService;
import br.com.authorize.services.valitations.transaction.CheckTransactionValidation;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class TransactionServiceImpl implements TransactionService {

    TransactionRepository transactionRepository;

    AccountService accountService;

    private final List<CheckTransactionValidation> validations;

    public TransactionServiceImpl(List<CheckTransactionValidation> validations,
                                  TransactionRepository transactionRepository,
                                  AccountService accountService) {
        this.validations = validations;
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Override
    public AccountResponseDTO process(final Transaction transaction) {
        final Set<String> violations = Sets.newHashSet();
        final var futures = validations.stream()
                .map(it -> it.valid(this, transaction, violations))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();

        var accountResult = new Account();
        final var account = accountService.findAccount();

        if(!isNull(account)){
            accountResult = account;
        }

        if(violations.isEmpty()) {
            transaction.setDateCreated(LocalDateTime.now());
            transactionRepository.save(transaction);
            accountService.updateAccount(transaction, accountResult);
        }

        return AccountResponseDTO.builder().account(accountResult).violations(violations).build();

    }

    @Override
    public List<Transaction> getTransactionsByInterval(long intervalMinutes) {
        final var transactions = transactionRepository.findAll();
        if(!transactions.isEmpty()) {
            return transactions.stream()
                    .filter(transaction -> transaction.getDateCreated()
                            .compareTo(LocalDateTime.now()
                                    .minusMinutes(intervalMinutes)) >= 0)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
