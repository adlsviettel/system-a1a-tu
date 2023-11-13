package com.allianceoneapparel.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity(name = "Account")
@Table(name = "Account", schema = "app")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account implements UserDetails {
    @Id
    @Column(name = "AccountId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "AccountCode", nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(name = "Password", nullable = false)
    private byte[] password;

    @Column(name = "AccountName", nullable = false)
    private String fullName;

    @Column(name = "IsActive")
    private boolean isActive;

    @Column(name = "AccountType")
    @Enumerated(EnumType.ORDINAL)
    private AccountPositionType position;

    @Column(name = "Department")
    private String department;

    @Column(name = "Email")
    private String email;

    @Column(name = "Gender")
    @Enumerated(EnumType.ORDINAL)
    private AccountGenderType gender;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return new String(password);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

}

enum AccountGenderType {
    REMOVE_ZERO_TO_START_AT_ONE,
    MALE,
    FEMALE,
}


enum AccountPositionType {
    REMOVE_ZERO_TO_START_AT_ONE,
    STAFF,
    MANAGER
}