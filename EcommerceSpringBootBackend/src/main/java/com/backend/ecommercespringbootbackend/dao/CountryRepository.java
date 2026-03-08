package com.backend.ecommercespringbootbackend.dao;

import com.backend.ecommercespringbootbackend.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country,Long> {
}
