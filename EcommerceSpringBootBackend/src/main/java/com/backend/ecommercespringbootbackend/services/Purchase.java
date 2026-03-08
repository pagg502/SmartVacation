package com.backend.ecommercespringbootbackend.services;

import com.backend.ecommercespringbootbackend.entities.Cart;
import com.backend.ecommercespringbootbackend.entities.CartItem;
import com.backend.ecommercespringbootbackend.entities.Customer;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class Purchase {
    private Customer customer;
    private Cart cart;
    private Set<CartItem> cartItems;
}
