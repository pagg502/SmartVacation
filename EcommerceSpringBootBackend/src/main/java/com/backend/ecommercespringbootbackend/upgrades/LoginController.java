package com.backend.ecommercespringbootbackend.upgrades;

import com.google.firebase.messaging.FirebaseMessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final LoginAuthentication loginAuthentication;
    private final CustomerLoginRepository customerLoginRepository;

    public LoginController(LoginAuthentication loginAuthentication, CustomerLoginRepository customerLoginRepository) {
        this.loginAuthentication = loginAuthentication;
        this.customerLoginRepository = customerLoginRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CustomerLogin request, HttpSession session) throws FirebaseMessagingException {
        System.out.println("*******************");
        System.out.println("*******************");
        System.out.println("New Login Requested by Browser");
        System.out.println("Email: " + request.getEmail());

        ResponseEntity<?> result = loginAuthentication.authenticate(request.getEmail(), request.getPassword());

        CustomerLogin customerLogin = customerLoginRepository.findByEmail(request.getEmail());

        //If login is successful, store user info in session
        if (result.getStatusCode().is2xxSuccessful() && result.getBody() != null) {
            Map<String, Object> body = (Map<String, Object>) result.getBody();
            if (body.get("customer") != null) {
                //Customer found in the response! Store session
                session.setAttribute("userEmail", request.getEmail());
                session.setAttribute("firstName", customerLogin.getFirstName());
            }
        }
        return result;
    }
    //Logout and close session stored in memory
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        Object sessionEmail = session.getAttribute("userEmail");
        if (sessionEmail != null) {
            String email = sessionEmail.toString();
            session.invalidate();
            System.out.println("Web session invalidated for " +sessionEmail+". Logout Successful");
            return ResponseEntity.ok(Map.of("Response", "Logout Successful"));
        }
        session.invalidate();
        System.out.println("Web session invalidated. Logout Successful");
        return ResponseEntity.ok(Map.of("Response", "Logout Successful"));
    }

    //Get session status
    @GetMapping("/status")
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
