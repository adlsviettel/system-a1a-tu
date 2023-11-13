package com.allianceoneapparel.authentication;

import com.allianceoneapparel.account.Account;
import com.allianceoneapparel.account.AccountRepository;
import com.allianceoneapparel.config.JwtService;
import com.allianceoneapparel.core.common.ResponseAPI;
import com.allianceoneapparel.token.Token;
import com.allianceoneapparel.token.TokenRepository;
import com.allianceoneapparel.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

//    public ResponseAPI<String> register(RegisterRequest registerRequest) {
//    }

    public ResponseAPI<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        var account = accountRepository.findAccountByUsername(request.username()).orElseThrow();
        var jwtToken = jwtService.generateToken(account);
        var refreshToken = jwtService.generateRefreshToken(account);
        revokeAllAccountTokens(account);
        saveAccountToken(account, jwtToken);
        var accountResponse = AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
        return new ResponseAPI<>(200, null, accountResponse);
    }

    public ResponseAPI<AuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var account = this.accountRepository.findAccountByUsername(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, account)) {
                var accessToken = jwtService.generateToken(account);
                revokeAllAccountTokens(account);
                saveAccountToken(account, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                return new ResponseAPI<>(200, null, authResponse);
            }
        }
        return null;
    }

    private void saveAccountToken(Account user, String jwtToken) {
        var token = Token.builder()
                .account(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllAccountTokens(Account account) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(account.getId());
        if (validUserTokens == null) {
            return;

        }

        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
