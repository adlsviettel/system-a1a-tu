package com.allianceoneapparel.account.service;

import com.allianceoneapparel.account.model.AccountRequest;
import com.allianceoneapparel.account.model.AccountResponse;
import com.allianceoneapparel.account.repository.AccountRepository;
import com.allianceoneapparel.core.common.ResponseAPI;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseAPI<AccountResponse> authenticate(AccountRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var account = accountRepository.findAccountByUsername(request.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(account);
        var refreshToken = jwtService.generateRefreshToken(account);
        account.setAccessToken(jwtToken);
        accountRepository.save(account);
        var accountResponse = AccountResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
        return new ResponseAPI<>(200, null, accountResponse);


    }

    public ResponseAPI<AccountResponse> refreshToken(
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
                account.setAccessToken(accessToken);
                accountRepository.save(account);
                var authResponse = AccountResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
               return new ResponseAPI<>(200, null, authResponse);
            }
        }
        return null;
    }
}
