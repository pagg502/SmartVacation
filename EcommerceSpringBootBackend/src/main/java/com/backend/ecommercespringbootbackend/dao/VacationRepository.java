package com.backend.ecommercespringbootbackend.dao;

import com.backend.ecommercespringbootbackend.entities.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
}
