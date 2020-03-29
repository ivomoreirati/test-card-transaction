package br.com.authorize.services;

import br.com.authorize.dto.AccountResponseDTO;
import br.com.authorize.entities.Account;
import br.com.authorize.entities.Transaction;

public interface AccountService {

    AccountResponseDTO process(final Account account);

    void updateAccount(Transaction transaction, Account accountResult);

    Account findAccount();
}
