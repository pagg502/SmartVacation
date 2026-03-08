package com.backend.ecommercespringbootbackend.upgrades;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoginCheckSessionDTORepository extends JpaRepository<LoginCheckSessionDTO, Long> {
    LoginCheckSessionDTO findByEmail(String email);

    List<LoginCheckSessionDTO> findByFirstName(String firstName);

    int countByFirstName(String firstName);

    List<LoginCheckSessionDTO> findByPhoneIsStartingWith(String phoneAreaCode);

    int countByPhoneIsStartingWith(String phoneAreaCode);

    LoginCheckSessionDTO findByPhoneToken(String phoneToken);
}
