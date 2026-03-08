package com.backend.ecommercespringbootbackend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="customers")
@Getter
@Setter
public class Customer {
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

    @Column(name="password_hash",  nullable=false)
    private String password;

    @Column(name="address",  nullable=false)
    private String address;

    @Column(name="postal_code",   nullable=false)
    private String postal_code;

    @Column(name="phone", nullable=false)
    private String phone;

    @Column(name="create_date")
    @CreationTimestamp
    private Date create_date;

    @Column(name="last_update")
    @UpdateTimestamp
    private Date last_update;

    @ManyToOne
    @JoinColumn(name="division_id")
    private Division division;

    //Needed a @Transient variable to store country sent by the frontEnd
    @Transient
    private String country;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Cart> carts;

    public Customer(String firstName, String lastName,String email, String password, String address, String postalCode, String phone, Division divisionId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.postal_code = postalCode;
        this.phone = phone;
        this.division = divisionId;
    }

    public Customer() {
    }

    public void add(Cart cart) {
        if (cart != null) {
            if (carts == null) {
                carts = new HashSet<>();
            }
            carts.add(cart);
            cart.setCustomer(this);
        }
    }

    public String getPasswordHash() {
        return password;
    }
}
