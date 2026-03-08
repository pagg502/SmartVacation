package com.backend.ecommercespringbootbackend.upgrades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class CustomerLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="customer_id")
    private Long id;

    @Column(name="customer_first_name", nullable=false)
    private String firstName;

    @Column(name="email",  nullable=false)
    private String email;

    @Column(name="password_hash",  nullable=false)
    private String password;

    @Column(name="phoneToken")
    private String phoneToken;

    @Column(name="fcmToken")
    private String fcmToken;

    public CustomerLogin(String fistrName, String username, String password, String phoneToken, String fcmToken) {
        this.firstName = fistrName;
        this.email = username;
        this.password = password;
        this.phoneToken = phoneToken;
        this.fcmToken = fcmToken;
    }

    public CustomerLogin() {
    }

    public String getPasswordHash() {
        return password;
    }
}
