package vn.allianceone.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByCode(String username);
}
