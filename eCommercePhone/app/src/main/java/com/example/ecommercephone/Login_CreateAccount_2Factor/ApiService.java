package com.example.ecommercephone.Login_CreateAccount_2Factor;

import retrofit2.Call;

import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login/phone")
    Call<Map<String, Object>> login(@Body Map<String, String> request);

    @HTTP(method = "DELETE", path = "logout/phone", hasBody = true)
    Call<Map<String, Object>> logout(@Body Map<String, String> request);

    @POST("login/fcmToken")
    Call<Map<String, Object>> fcmToken(@Body Map<String, String> request);

    @POST("2fa/approve")
    Call<Void> twoFaConfirmation(@Body Map<String, String> request);

    @POST("2fa/deny")
    Call<Void> twoFaDenied(@Body Map<String, String> request);
}
