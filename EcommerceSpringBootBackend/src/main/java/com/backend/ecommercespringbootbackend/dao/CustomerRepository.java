package com.backend.ecommercespringbootbackend.dao;

import com.backend.ecommercespringbootbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByFirstNameIgnoreCase(String firstName);
    Optional<Customer> findByEmail(String email);
    boolean existsByEmailIgnoreCase(String email);
}
