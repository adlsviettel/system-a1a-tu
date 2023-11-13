package com.allianceoneapparel.account;

import com.allianceoneapparel.core.common.Constants;
import com.allianceoneapparel.core.exception.AppException;
import com.allianceoneapparel.core.extension.MessageSourceExtensions;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository repository;
    private final MessageSourceExtensions localize;

    public void changePassword(ChangePasswordRequest request, Principal connectedAccount) {
        var account = (Account) ((UsernamePasswordAuthenticationToken) connectedAccount).getPrincipal();

        if (!passwordEncoder.matches(request.currentPassword(), account.getPassword())) {
            throw new AppException(400, localize.getMsg(Constants.USER_OLD_PASSWORD_ERROR));
        }

        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new AppException(400, localize.getMsg(Constants.USER_NEW_PASSWORD_ERROR));
        }

        account.setPassword(passwordEncoder.encode(request.newPassword()).getBytes());
        repository.save(account);
    }
}
