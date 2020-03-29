package br.com.authorize.server;

import br.com.authorize.services.AuthorizeService;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;


@Slf4j
public class AuthorizeServer {

    static public void createServer(AuthorizeService authorizeService) {

        while(true)
            processing(System.in, authorizeService);
    }

    static public void processing(InputStream inputStream, AuthorizeService authorizeService) {

        try{
            InputStreamReader instr = new InputStreamReader(inputStream);

            PrintWriter out = new PrintWriter(System.out, true);

            BufferedReader in = new BufferedReader(instr);

            String line;
            while ((line = in.readLine()) != null) {
                out.println(authorizeService.receive(line));
            }
            in.close();
            out.close();
        } catch(Exception ignored) {

        }
    }
}
