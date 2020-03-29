package br.com.authorize.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Account")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @JsonIgnore
    private BigInteger id;

    @Column(name = "activeCard")
    @JsonProperty("active-card")
    private boolean activeCard;

    @Column(name = "availableLimit")
    @JsonProperty("available-limit")
    private BigInteger availableLimit;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="accountId")
    @Fetch(FetchMode.SELECT)
    @JsonIgnore
    private List<Transaction> transactions = new ArrayList<>();

}
