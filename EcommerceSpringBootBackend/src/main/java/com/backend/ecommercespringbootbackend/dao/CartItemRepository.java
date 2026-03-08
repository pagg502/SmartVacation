package com.backend.ecommercespringbootbackend.dao;

import com.backend.ecommercespringbootbackend.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
