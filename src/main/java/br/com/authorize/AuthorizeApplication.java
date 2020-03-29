package br.com.authorize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AuthorizeApplication
{
	public static void main(String[] args) {
		SpringApplication.run(AuthorizeApplication.class, args);
	}
}
