package com.backend.ecommercespringbootbackend.webSocket;

import com.backend.ecommercespringbootbackend.upgrades.LoginCheckSessionDTO;
import com.backend.ecommercespringbootbackend.upgrades.LoginCheckSessionDTORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api")
@Controller
public class WebSocketController {
    private final LoginCheckSessionDTORepository loginCheckSessionDTORepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    //Constructor
    public WebSocketController(LoginCheckSessionDTORepository loginCheckSessionDTORepository) {
        this.loginCheckSessionDTORepository = loginCheckSessionDTORepository;
    }

    @PostMapping("/2fa/approve")
    public ResponseEntity<Void> approve2FA(@RequestBody Map<String, String> confirmation) {
        //Retrieve customer
        LoginCheckSessionDTO customerDTO = loginCheckSessionDTORepository.findByEmail(confirmation.get("email"));

        if (customerDTO != null) {
            //Create HashMap to send via WebSocket
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", "APPROVED");
            hashMap.put("customer", customerDTO);
            System.out.println("Received APPROVED from 2FA-Notification. Notifying Frontend to proceed.");
            // Send to a specific path unique to this email
            messagingTemplate.convertAndSend("/messages/2fa/" + customerDTO.getEmail(), hashMap);

            return ResponseEntity.ok().build();
        } else {
            System.out.println("Customer not found for email: " + confirmation.get("email"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PostMapping("/2fa/deny")
    public ResponseEntity<Void> deny2FA(@RequestBody Map<String, String> confirmation) {
        //Retrieve customer
        LoginCheckSessionDTO customerDTO = loginCheckSessionDTORepository.findByEmail(confirmation.get("email"));

        if (customerDTO != null) {
            //Create HashMap to send via WebSocket
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", "DENIED");
            System.out.println("Received DENIED from 2FA-Notification. Notifying Frontend about denial.");
            //Send to a specific path unique to this email
            messagingTemplate.convertAndSend("/messages/2fa/" + customerDTO.getEmail(), hashMap);

            return ResponseEntity.ok().build();
        } else {
            System.out.println("Customer not found for email: " + confirmation.get("email"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
