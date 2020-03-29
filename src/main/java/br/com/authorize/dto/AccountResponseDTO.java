package br.com.authorize.dto;

import br.com.authorize.entities.Account;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {
    private Account account;
    private Set<String> violations;
}
