package br.com.authorize.util;

import br.com.authorize.dto.AccountResponseDTO;
import br.com.authorize.entities.Account;
import br.com.authorize.entities.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ParseJson
{
    
    static private final ObjectMapper mapper = new ObjectMapper();
    
    static public Account parseAccount(String json){
        
        try{
            return mapper.readValue(json, Account.class);
            
         } catch(IOException e){
            
             System.err.println("Error converter object json. " + e.getMessage());

             return null;
        }
    }

    static public String convertObjectToString(AccountResponseDTO account){

        try{
            return mapper.writeValueAsString(account);

        }catch(IOException e){

            System.err.println("Error converter object to string. " + e.getMessage());

            return null;
        }
    }

    static public Transaction parseTransaction(String json){

        try{
            return mapper.readValue(json, Transaction.class);

        }catch(IOException e){

            System.err.println("Error converter object json. " + e.getMessage());

            return null;
        }
    }
}
