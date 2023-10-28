package vn.allianceone.account;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    protected AccountResponse onLogin(LoginParam params) {
        try {
            Account account = accountRepository.findAccountByCode(params.getUsername()).orElseThrow(() -> new Exception(""));
            boolean isAuth = BCrypt.checkpw(params.getPassword(), new String(account.getPassword()));
            if (isAuth) return new AccountResponse(account.getId(), account.getCode(), account.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
