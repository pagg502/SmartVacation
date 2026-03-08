package com.backend.ecommercespringbootbackend.dao;

import com.backend.ecommercespringbootbackend.entities.Excursion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExcursionRepository extends JpaRepository<Excursion,Long> {
}
