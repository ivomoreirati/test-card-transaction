package br.com.authorize.utils;

import br.com.authorize.entities.Account;
import br.com.authorize.entities.Transaction;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class OperationResponseUtils {

    private static File getInputFileMockAccount() {
        return new File("src/test/resources/Account.txt");
    }

    private static File getInputFileMockTransaction() {
        return new File("src/test/resources/Transaction.txt");
    }

    private static String getResponseMockAccount() throws IOException {
        return FileUtils.readFileToString(getInputFileMockAccount(),
                String.valueOf(StandardCharsets.UTF_8));
    }

    public static BufferedReader getInputFileMockOperations() throws FileNotFoundException {
        final var file =  new File("src/test/resources/Operations.txt");
        FileReader fr = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fr);
        return bufferedReader;
    }

    public static BufferedReader getInputFileMockOperationsError() throws FileNotFoundException {
        final var file =  new File("src/test/resources/OperationsError.txt");
        FileReader fr = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fr);
        return bufferedReader;
    }
    private static String getResponseMockTransaction() throws IOException {
        return FileUtils.readFileToString(getInputFileMockTransaction(),
                String.valueOf(StandardCharsets.UTF_8));
    }

    public static Account parseAccount(String account) throws JSONException {
        final JSONObject json = new JSONObject(account);
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json.get("account").toString(), Account.class);
        } catch (IOException e) {
            return null;
        }
    }

    public static Account getAccount() throws IOException, JSONException {
        final var accountString = OperationResponseUtils.getResponseMockAccount();
        final var account = OperationResponseUtils.parseAccount(accountString);
        return account;
    }

    public static Transaction parseTransaction(String transaction) throws JSONException {
        final JSONObject json = new JSONObject(transaction);
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(json.get("transaction").toString(), Transaction.class);
        } catch (IOException e) {
            return null;
        }
    }

    public static Transaction getTransaction() throws IOException, JSONException {
        final var transactionString = OperationResponseUtils.getResponseMockTransaction();
        final var transaction = OperationResponseUtils.parseTransaction(transactionString);
        return transaction;
    }



}
