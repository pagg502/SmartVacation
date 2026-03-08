package com.backend.ecommercespringbootbackend.upgrades;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class LoginAuthenticationPhone {
    private final CustomerLoginRepository customerLoginRepository;
    private final LoginCheckSessionDTORepository loginCheckSessionDTORepository;

    public LoginAuthenticationPhone(CustomerLoginRepository customerLoginRepository,  LoginCheckSessionDTORepository loginCheckSessionDTORepository) {
        this.customerLoginRepository = customerLoginRepository;
        this.loginCheckSessionDTORepository = loginCheckSessionDTORepository;
    }

    public ResponseEntity<?> authenticate(String email, String password, String fcmToken) {
        if(email == null){
            System.out.println("Login Failed! No email was sent from Mobile Phone.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Failed! No email was sent from Mobile Phone.");
        }

        CustomerLogin customerLogin = customerLoginRepository.findByEmail(email);

        if (customerLogin == null) {
            System.out.println("No email found!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email. No email found!");
        }

        if (!passwordMatches(password, customerLogin.getPasswordHash())) {
            System.out.println("Invalid password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }
        LoginCheckSessionDTO customerDTO = loginCheckSessionDTORepository.findByEmail(email);
        System.out.println("Login Successful");
        System.out.println("FirstName: " + customerDTO.getFirstName());
        customerDTO.setFcmToken(fcmToken);
        loginCheckSessionDTORepository.save(customerDTO);
        if(customerDTO.getPhoneToken() == null){
            String phoneToken = UUID.randomUUID().toString() + UUID.randomUUID().toString();
            customerDTO.setPhoneToken(phoneToken);
            loginCheckSessionDTORepository.save(customerDTO);
        }
        return ResponseEntity.ok(customerDTO);
    }

    private boolean passwordMatches(String rawPassword, String storedHash) {
        return BCrypt.checkpw(rawPassword, storedHash);
    }
}
