package com.example.ecommercephone.Login_CreateAccount_2Factor;

import android.Manifest;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.ecommercephone.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FCMService extends FirebaseMessagingService {
    private static final String TAG = "FCM_DEBUG";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        //Add trigger 2fa so Home Class can detect it and trigger the 2fa screen
        SharedPreferences pref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        pref.edit()
                .putBoolean("trigger_2fa_flag", true) // Use a clear, specific key
                .apply();
        System.out.println("******Checking flag in onMessageReceived: " + pref.getBoolean("trigger_2fa_flag", false));

        //Prepare the Intent for the 2FA Activity
        Intent intent = new Intent(this, TwoFactorAuthentication.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Pass data if existing
        if (remoteMessage.getData().size() > 0) {
            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }

        //CHECK FOREGROUND STATUS
        if (isAppInForeground()) {
            Log.d(TAG, "App is in foreground. Launching Activity directly.");
            startActivity(intent);
        } else {
            Log.d(TAG, "App is in background. Triggering Notification.");
            sendNotification(remoteMessage, intent);
        }
    }

    private boolean isAppInForeground() {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND);
    }

    private void sendNotification(RemoteMessage remoteMessage, Intent intent) {

        //Add trigger 2fa so Home Class can detect it and trigger the 2fa screen
        SharedPreferences pref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        pref.edit()
                .putBoolean("trigger_2fa_flag", true) // Use a clear, specific key
                .apply();
        System.out.println("******Checking flag in sendNotification: " + pref.getBoolean("trigger_2fa_flag", false));

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        String title = "Security Alert";
        String body = "New login attempt detected.";

        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ALERTS_CHANNEL")
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(101, builder.build());
        } else {
            Log.e(TAG, "Cannot show notification: Permission not granted.");
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        SharedPreferences pref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String email = pref.getString("email", null);

        if (email != null) {
            Log.d(TAG, "New Token: " + token);
            sendTokenToBackend(email, token);
        }
    }

    private void sendTokenToBackend(String email, String token) {
        Map<String, String> FCMtoken = new HashMap<>();
        FCMtoken.put("FCMtoken", token);
        FCMtoken.put("email", email);

        String apiUrl = com.example.ecommercephone.BuildConfig.API_URL;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        apiService.fcmToken(FCMtoken).enqueue(new retrofit2.Callback<Map<String, Object>>() {
            @Override
            public void onResponse(retrofit2.Call<Map<String, Object>> call, retrofit2.Response<Map<String, Object>> response) {
                Log.d(TAG, "Token updated on server: " + response.isSuccessful());
            }

            @Override
            public void onFailure(retrofit2.Call<Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "Token update failed: " + t.getMessage());
            }
        });
    }
}