package com.backend.ecommercespringbootbackend.upgrades;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerLoginRepository extends JpaRepository<CustomerLogin, Long> {
    CustomerLogin findByEmail(String email);
}
