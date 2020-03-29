package br.com.authorize.config;

import br.com.authorize.server.AuthorizeServer;
import br.com.authorize.services.AuthorizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
@Slf4j
public class AuthorizeConfig {
    
    @Autowired
    AuthorizeService authorizeService;
    
    @Bean
    InitializingBean inicialize() {

        return () -> {

            Executors.newSingleThreadExecutor().execute(() -> {
                try{
                    AuthorizeServer.createServer(authorizeService);
                }catch(Exception e){
                    log.error("Error create MultiThread. " + e.getMessage());
                }
            });
        };
    }
}
