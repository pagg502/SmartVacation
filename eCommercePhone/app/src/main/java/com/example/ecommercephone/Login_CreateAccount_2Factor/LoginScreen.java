package com.example.ecommercephone.Login_CreateAccount_2Factor;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import com.example.ecommercephone.Home;
import com.example.ecommercephone.R;
import com.google.firebase.messaging.FirebaseMessaging;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginScreen extends AppCompatActivity {
    String apiUrl = com.example.ecommercephone.BuildConfig.API_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if token exists, meaning user is already logged in
        SharedPreferences pref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String savedToken = pref.getString("auth_token", null);

        if (savedToken != null) {
            String command = getIntent().getStringExtra("command");
            // Use a Handler to delay finish() and redirection to avoid ActivityTransaction crashes on Android 10+
            new Handler(Looper.getMainLooper()).post(() -> {
                if ("trigger_2fa".equals(command)) {
                    Intent intent = new Intent(LoginScreen.this, TwoFactorAuthentication.class);
                    if (getIntent().getExtras() != null) {
                        intent.putExtras(getIntent().getExtras());
                    }
                    startActivity(intent);
                } else {
                    startActivity(new Intent(LoginScreen.this, Home.class));
                }
                finish();
            });
            return;
        }

        // Request Permission for Android 13+ ONLY if not redirecting
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        setContentView(R.layout.activity_login_screen);
        
        Button login = (Button) findViewById(R.id.login_button);
        Button createAccount = (Button) findViewById(R.id.Create_Account_button);
        
        EditText inputtedUsername = (EditText) findViewById(R.id.username_input);
        EditText inputtedPassword = (EditText) findViewById(R.id.password_input);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = inputtedUsername.getText().toString();
                String password = inputtedPassword.getText().toString();

                if (username.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Sanitize email and password input
                String sanitizedEmail = username.trim().toLowerCase();
                String sanitizedPassword = password.trim();

                //Get FCM Token
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(task -> {
                            String FCMToken = ""; // Default to empty
                            if (task.isSuccessful() && task.getResult() != null) {
                                FCMToken = task.getResult();
                            } else {
                                System.out.println("Firebase token retrieved failed: " + task.getException());
                            }
                            login(sanitizedEmail, sanitizedPassword, apiUrl, FCMToken);
                        });
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginScreen.this, Create_User_Account.class));
            }
        });
    }

    private void login(String sanitizedEmail, String sanitizedPassword, String apiUrl, String FCMToken) {
        // Create the Map for the @Body
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", sanitizedEmail);
        credentials.put("password", sanitizedPassword);
        credentials.put("fcmToken", FCMToken);

        // Build Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        //Call the API
        apiService.login(credentials).enqueue(new retrofit2.Callback<Map<String, Object>>() {
            @Override
            public void onResponse(retrofit2.Call<Map<String, Object>> call, retrofit2.Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //Add response.body() to a Map
                    Map<String, Object> result = response.body();

                    // Get the token from the map
                    String phoneToken = (String) result.get("phoneToken");
                    String firstName = (String) result.get("firstName");
                    String email = (String) result.get("email");

                    SharedPreferences pref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                    pref.edit()
                            .putString("auth_token", phoneToken)
                            .putString("firstName", firstName)
                            .putString("email", email)
                            .apply();

                    Toast.makeText(LoginScreen.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginScreen.this, Home.class));
                    finish(); // Close login screen so user can't go back to it
                }else {
                    System.out.println("Something went wrong during login! See logs for more info");
                    Toast.makeText(LoginScreen.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(LoginScreen.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("Network Error: " + t.getMessage());
            }
        });
    }
}