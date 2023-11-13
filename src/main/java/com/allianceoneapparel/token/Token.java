package com.allianceoneapparel.token;

import com.allianceoneapparel.account.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Token", schema = "app")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TokenId")
    private Integer id;

    @Column(name = "Token", unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "TokenType")
    private TokenType tokenType = TokenType.BEARER;

    @Column(name = "Revoked")
    private boolean revoked;

    @Column(name = "Expired")
    private boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AccountId")
    private Account account;
}
