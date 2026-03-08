package com.example.ecommercephone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecommercephone.Login_CreateAccount_2Factor.ApiService;
import com.example.ecommercephone.Login_CreateAccount_2Factor.LoginScreen;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Home extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Retrieve saved First Name
        SharedPreferences pref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String firstName = pref.getString("firstName", "User"); // "User" is the default i

        //Set First Name
        TextView welcomeText = findViewById(R.id.welcome_text);
        welcomeText.setText("Welcome, " + firstName + "!");

        Button logout = (Button) findViewById(R.id.logout_button);

        logout.setOnClickListener(view -> {

            //Clean Store token in the backend
            //Get global variable
            String apiUrl = com.example.ecommercephone.BuildConfig.API_URL;

            //Set Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            //Create API Service
            ApiService apiService = retrofit.create(ApiService.class);

            //Create the Map for the @Body
            Map<String, String> phoneToken = new HashMap<>();
            phoneToken.put("phoneToken", pref.getString("auth_token", null));

            //Make the API call
            apiService.logout(phoneToken).enqueue(new retrofit2.Callback<Map<String, Object>>() {
                @Override
                public void onResponse(retrofit2.Call<Map<String, Object>> call, retrofit2.Response<Map<String, Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        //Add response.body() to a Map
                        Map<String, Object> result = response.body();
                        //Access the preferences to clear phone token and first name
                        SharedPreferences.Editor editor = pref.edit();
                        //Clear the data
                        editor.remove("auth_token");
                        editor.remove("firstName");
                        editor.apply();
                        //Notify user on the phone and change screen to login screen
                        Toast.makeText(Home.this, "Logout Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Home.this, LoginScreen.class));
                        finish();
                    } else {
                        Toast.makeText(Home.this, "Something went wrong! See logs for more info", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(Home.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("Network Error: " + t.getMessage());
                }
            });
        });
    }
}