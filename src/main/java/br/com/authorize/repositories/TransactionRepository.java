package br.com.authorize.repositories;

import br.com.authorize.entities.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, BigInteger>{

    @Override
    List<Transaction> findAll();

}
