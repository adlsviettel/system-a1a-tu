package com.allianceoneapparel.account;

import com.allianceoneapparel.account.model.AccountRequest;
import com.allianceoneapparel.account.model.AccountResponse;
import com.allianceoneapparel.account.service.AccountService;
import com.allianceoneapparel.core.common.ResponseAPI;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v2/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseAPI<AccountResponse> authenticate(
            @RequestBody AccountRequest request
    ) {
        return accountService.authenticate(request);
    }

    @PostMapping("/refresh-token")
    public ResponseAPI<AccountResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        return accountService.refreshToken(request, response);
    }
}
