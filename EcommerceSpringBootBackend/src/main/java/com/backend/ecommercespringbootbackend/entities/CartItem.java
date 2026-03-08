package com.backend.ecommercespringbootbackend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
public class CartItem implements Serializable {
    @Id
    @Column(name = "cart_item_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vacation_id")
    private Vacation vacation;

    @ManyToMany
    @JoinTable(
            name = "excursion_cartitem",
            joinColumns = @JoinColumn(name = "cart_item_id"),
            inverseJoinColumns = @JoinColumn(name = "excursion_id")
    )
    private Set<Excursion> excursions;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(name = "create_date")
    @CreationTimestamp
    private Date create_date;

    @UpdateTimestamp
    @Column(name = "last_update")
    private Date last_update;
}
