package com.backend.ecommercespringbootbackend.upgrades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class LoginCheckSessionDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="customer_id")
    private Long id;

    @Column(name="customer_first_name", nullable=false)
    private String firstName;

    @Column(name="customer_last_name",  nullable=false)
    private String lastName;

    @Column(name="email",  nullable=false)
    private String email;

    @Column(name="address",  nullable=false)
    private String address;

    @Column(name="postal_code",   nullable=false)
    private String postal_code;

    @Column(name="phone", nullable=false)
    private String phone;

    @Column(name="phoneToken")
    private String phoneToken;

    @Column(name="fcmToken")
    private String fcmToken;
}
