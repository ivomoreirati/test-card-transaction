package br.com.authorize.services;

import br.com.authorize.dto.AccountResponseDTO;
import br.com.authorize.entities.Transaction;

import java.util.List;

public interface TransactionService {

    AccountResponseDTO process(final Transaction transaction);

    List<Transaction> getTransactionsByInterval(long intervalMinutes);

}
