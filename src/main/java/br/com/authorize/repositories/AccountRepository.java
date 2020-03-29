package br.com.authorize.repositories;

import br.com.authorize.entities.Account;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.List;

public interface AccountRepository extends CrudRepository<Account, BigInteger>{

    @Override
    List<Account> findAll();

}
