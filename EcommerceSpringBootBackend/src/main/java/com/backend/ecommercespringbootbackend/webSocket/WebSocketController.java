package com.backend.ecommercespringbootbackend.webSocket;

import com.backend.ecommercespringbootbackend.s.v.PoweredByNova.ChatHistory;
import com.backend.ecommercespringbootbackend.s.v.PoweredByNova.StartChat;
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
    private final StartChat startChatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    //Constructor
    public WebSocketController(
            LoginCheckSessionDTORepository loginCheckSessionDTORepository,
            SimpMessagingTemplate messagingTemplate,
            StartChat startChatService) {
        this.loginCheckSessionDTORepository = loginCheckSessionDTORepository;
        this.messagingTemplate = messagingTemplate;
        this.startChatService = startChatService;
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
    // In-memory store for chat histories
    private static final Map<String, ChatHistory> chatSessions = new java.util.concurrent.ConcurrentHashMap<>();

    @PostMapping("/aiChat")
    public ResponseEntity<Void> aiChat(@RequestBody Map<String, String> payload) {
        String userInput = payload.get("currentInput");
        String sessionId = payload.get("sessionId");

        if (sessionId == null || sessionId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 1. Retrieve existing history or create a new one
        ChatHistory history = chatSessions.computeIfAbsent(sessionId, k -> new ChatHistory());

        String aiResponse;

        // 2. Check if the session already has previous messages,
        // AND the user sent a fresh window-load trigger (like "Start conversation" or empty input)
        boolean hasPastTurns = !history.getMessages().isEmpty();
        boolean isInitialTrigger = (userInput == null || userInput.equalsIgnoreCase("Start conversation") || userInput.isEmpty());

        if (hasPastTurns && isInitialTrigger) {
            // Instead of restarting or re-greeting with the cold welcome,
            // give a resumption prompt or grab the last assistant state.
            aiResponse = "Welcome back! We were previously discussing your trip. Would you like to review your trip report or make any changes?";
        } else {
            // 3. Normal flow: pass input and history to your service
            aiResponse = startChatService.startChat(userInput, history);
        }

        // 4. Prepare and send WebSocket payload
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("aiResponse", aiResponse);
        messagingTemplate.convertAndSend("/messages/aiChat/" + sessionId, hashMap);

        return ResponseEntity.ok().build();
    }
}
