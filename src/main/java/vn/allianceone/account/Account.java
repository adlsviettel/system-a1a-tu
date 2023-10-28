package vn.allianceone.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Account")
@Table(name = "Account", schema = "app")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Account {
    @Id
    @Column(name = "AccountId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "AccountCode", nullable = false, unique = true)
    private String code;

    @Column(name = "AccountName", nullable = false)
    private String name;

    @JsonIgnore
    @Column(name = "Password", nullable = false)
    private byte[] password;
}
