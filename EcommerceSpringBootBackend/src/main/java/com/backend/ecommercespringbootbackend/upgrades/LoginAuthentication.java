package com.backend.ecommercespringbootbackend.upgrades;

import com.backend.ecommercespringbootbackend.firebase.FCMService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAuthentication {
    private final CustomerLoginRepository customerLoginRepository;
    private final LoginCheckSessionDTORepository loginCheckSessionDTORepository;

    public LoginAuthentication(CustomerLoginRepository customerLoginRepository,  LoginCheckSessionDTORepository loginCheckSessionDTORepository) {
        this.customerLoginRepository = customerLoginRepository;
        this.loginCheckSessionDTORepository = loginCheckSessionDTORepository;
    }

    public ResponseEntity<?> authenticate(String email, String password) throws FirebaseMessagingException {
        if(email == null){
            System.out.println("Login Failed! No email was sent from frontend.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Failed! No email was sent from frontend.");
        }

        CustomerLogin customerLogin = customerLoginRepository.findByEmail(email);

        if (customerLogin == null) {
            System.out.println("Email Not Found!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email. Email Not Found!");
        }
        if (!passwordMatches(password, customerLogin.getPasswordHash())) {
            System.out.println("Invalid password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }
        LoginCheckSessionDTO customerDTO = loginCheckSessionDTORepository.findByEmail(email);
        System.out.println("Login Successful");
        System.out.println("FirstName: " + customerDTO.getFirstName());

        if(customerDTO.getFcmToken() != null){
            FCMService fcmService = new FCMService();
            String response = fcmService.sendNotification(customerDTO.getFcmToken(),"New Sign-in Attempt", "Ecommerce Website");
            if(response.isEmpty()){
                System.out.println("Firebase could not be reached!");
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Firebase could not be reached!");
            }else{
                System.out.println("2FA notification sent to phone. Firebase response: " + response);
                Map<String, String> map = new HashMap<>();
                map.put("status", "PENDING_2FA");
                System.out.println("Login status: " +map+ ", timing out in 15s");
                return ResponseEntity.ok(map);
                //return ResponseEntity.ok(customerDTO);
            }
        }
        //2FA not set up, no FCM Token found. Return status 2FA not set
        Map<String, Object> map = new HashMap<>();
        map.put("status", "2FA_NOT_SET");
        //Allow sign in/return customer
        map.put("customer", customerDTO);

        System.out.println("2FA_NOT_SET. Allowing signing and returning customer.");
        return ResponseEntity.ok(map);
    }

    private boolean passwordMatches(String rawPassword, String storedHash) {
        return BCrypt.checkpw(rawPassword, storedHash);
    }
}

