package com.example.ecommercephone.Login_CreateAccount_2Factor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.ecommercephone.Home;
import com.example.ecommercephone.R;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class TwoFactorAuthentication extends AppCompatActivity {
    //Retrieve API_URL variable
    String apiUrl = com.example.ecommercephone.BuildConfig.API_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_factor_authentication);
        //Implement Time Out for 2FA
        //Create a Handler the Alarm Clock manager and AtomicBoolean
        final AtomicBoolean isTimedOut = new AtomicBoolean(false);

        final Runnable timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                // This code runs ONLY if 15 seconds pass
                isTimedOut.set(true);
                System.out.println("Connection timed out!");
            }
        };

        //Start the timer
        new Handler(Looper.getMainLooper()).postDelayed(timeoutRunnable, 15000);

        //Get saved email from shared preferences
        SharedPreferences pref = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        //Build retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        ImageView approve = (ImageView) findViewById(R.id.icon_approve);
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTimedOut.get()) {
                    Toast.makeText(TwoFactorAuthentication.this, "Connection timed out!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TwoFactorAuthentication.this, Home.class));
                    finish();
                    return;
                }

                //Create the HashMap for the @Body
                Map<String, String> confirmation = new HashMap<>();
                confirmation.put("status", "APPROVED");
                confirmation.put("email", pref.getString("email", null));

                //Call the API
                apiService.twoFaConfirmation(confirmation).enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                        if (response.isSuccessful()) {

                            Toast.makeText(TwoFactorAuthentication.this, "Login Approved!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(TwoFactorAuthentication.this, Home.class));
                            finish();
                        }else {
                            System.out.println("Email not found on the server.");
                            Toast.makeText(TwoFactorAuthentication.this, "Error. Authentication failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                        Toast.makeText(TwoFactorAuthentication.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println("Network Error: " + t.getMessage());
                    }
                });
            }
        });

        ImageView deny = (ImageView) findViewById(R.id.icon_deny);
        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTimedOut.get()) {
                    Toast.makeText(TwoFactorAuthentication.this, "Connection timed out!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TwoFactorAuthentication.this, Home.class));
                    finish();
                    return;
                }

                //Create the HashMap for the @Body
                Map<String, String> confirmation = new HashMap<>();
                confirmation.put("status", "DENIED");
                confirmation.put("email", pref.getString("email", null));

                //Call the API
                apiService.twoFaDenied(confirmation).enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(TwoFactorAuthentication.this, "Login Denied!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(TwoFactorAuthentication.this, Home.class));
                            finish();
                        }else {
                            System.out.println("Email not found on the server.");
                            Toast.makeText(TwoFactorAuthentication.this, "Error. Authentication failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                        Toast.makeText(TwoFactorAuthentication.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println("Network Error: " + t.getMessage());
                    }
                });
            }
        });
    }
}