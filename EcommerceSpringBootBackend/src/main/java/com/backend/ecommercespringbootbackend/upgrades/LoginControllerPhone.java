package com.backend.ecommercespringbootbackend.upgrades;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginControllerPhone {
    private final LoginAuthenticationPhone loginAuthenticationPhone;
    private final LoginCheckSessionDTORepository loginCheckSessionDTORepository;

    public LoginControllerPhone(LoginAuthenticationPhone loginAuthenticationPhone, LoginCheckSessionDTORepository loginCheckSessionDTORepository) {
        this.loginAuthenticationPhone = loginAuthenticationPhone;
        this.loginCheckSessionDTORepository = loginCheckSessionDTORepository;
    }

    @PostMapping("/login/phone")
    public ResponseEntity<?> login(@RequestBody CustomerLogin request, HttpSession session) {
        System.out.println("*******************");
        System.out.println("*******************");
        System.out.println("New Login Requested by Phone");
        System.out.println("Email: " + request.getEmail());

        return loginAuthenticationPhone.authenticate(request.getEmail(), request.getPassword(), request.getFcmToken());
    }

    //Set FCM Token (Unique identifier for android phone when authenticating to google)
    @PostMapping("/login/fcmtoken")
    public ResponseEntity<?> fcmtoken(@RequestBody Map<String, String> request) {
        //Find user by email
        LoginCheckSessionDTO customerDTO = loginCheckSessionDTORepository.findByEmail(request.get("email"));

        //Update FCMToken
        customerDTO.setFcmToken(request.get("FCMToken"));

        //Save FCMToken
        loginCheckSessionDTORepository.save(customerDTO);

        return ResponseEntity.ok("FCM Token received!");
    }

    //Logout and delete stored token for authenticated user on the Cellphone
    @DeleteMapping("/logout/phone")
    public ResponseEntity<?> logout(@RequestBody LoginCheckSessionDTO loginCheckSessionDTO) {

        //Print Token that will be deleted
        System.out.println("Logging out - Customer token sent by Phone to delete: " + loginCheckSessionDTO.getPhoneToken());

        //Find token and return user
        LoginCheckSessionDTO customerDTO = loginCheckSessionDTORepository.findByPhoneToken(loginCheckSessionDTO.getPhoneToken());

        //Update token and fcmToken to null
        customerDTO.setPhoneToken(null);
        customerDTO.setFcmToken(null);

        //Save token
        loginCheckSessionDTORepository.save(customerDTO);

        //Return Successful operation message
        return ResponseEntity.ok(Map.of("Response", "Logout Successful"));
    }

    //Get session status
    @GetMapping("/status/phone")
    public ResponseEntity<?> status(HttpSession session) {
        //Object email = session.getAttribute("userEmail", "firstName");
        String email = (String) session.getAttribute("userEmail");
        String firstName = (String) session.getAttribute("firstName");

        if (email == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }
        if (firstName == null) {
            return ResponseEntity.status(401).body("Not user found for email: " + email);
        }
        return ResponseEntity.ok(Map.of("email", email,  "firstName", firstName));
    }
}



