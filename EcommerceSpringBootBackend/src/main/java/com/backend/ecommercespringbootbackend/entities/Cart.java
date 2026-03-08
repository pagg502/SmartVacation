package com.backend.ecommercespringbootbackend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "carts")
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_id")
    private Long id;

    @Column(name="order_tracking_number")
    private String orderTrackingNumber;

    @Column(name = "package_price", nullable = false)
    private BigDecimal package_price;

    @Column(name = "party_size",  nullable = false)
    private int party_size;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusType status;

    @Column(name = "create_date")
    @CreationTimestamp
    private Date create_date;

    @Column(name = "last_update")
    @UpdateTimestamp
    private Date last_update;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private Set<CartItem> cartItems;

    public void add(CartItem item){
        if(item != null){
            if (cartItems == null){
                cartItems = new HashSet<>();
            }
            cartItems.add(item);
            item.setCart(this);
        }
    }
}
