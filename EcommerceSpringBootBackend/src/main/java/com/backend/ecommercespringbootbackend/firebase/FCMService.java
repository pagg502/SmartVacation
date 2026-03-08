package com.backend.ecommercespringbootbackend.firebase;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;


@Service
public class FCMService {
    public String sendNotification(String token, String title, String body) throws FirebaseMessagingException {
        // Define the visual notification
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        // Define Android-specific settings (Priority and Channel)
        AndroidConfig androidConfig = AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                        .setChannelId("ALERTS_CHANNEL") // Matches your Android code
                        .build())
                .build();

        // The actual message
        Message message = Message.builder()
                .setToken(token) // The Android Device Token
                .setNotification(notification)
                .putData("command", "trigger_2fa") // Custom data for your onMessageReceived
                .setAndroidConfig(androidConfig)
                .build();

        return FirebaseMessaging.getInstance().send(message);
    }
}