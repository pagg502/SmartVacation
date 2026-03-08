package com.backend.ecommercespringbootbackend.dao;

import com.backend.ecommercespringbootbackend.entities.Division;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DivisionRepository extends JpaRepository<Division,Long> {
}
