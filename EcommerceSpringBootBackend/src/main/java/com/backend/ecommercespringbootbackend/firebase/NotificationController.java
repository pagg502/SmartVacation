package com.backend.ecommercespringbootbackend.firebase;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private FCMService fcmService;

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestParam String token) {
        try {
            String response = fcmService.sendNotification(token, "Backend Alert", "The server is calling!");
            return ResponseEntity.ok("Sent: " + response);
        } catch (FirebaseMessagingException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}