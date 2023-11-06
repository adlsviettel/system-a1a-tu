package com.allianceoneapparel.account;

import com.allianceoneapparel.account.model.AccountRequest;
import com.allianceoneapparel.account.model.AccountResponse;
import com.allianceoneapparel.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<AccountResponse> authenticate(
            @RequestBody AccountRequest request
    )  {
        return ResponseEntity.ok(accountService.authenticate(request));
    }
}
