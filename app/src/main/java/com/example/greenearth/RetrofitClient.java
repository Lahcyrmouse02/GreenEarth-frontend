package com.example.greenearth;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // 10.0.2.2 adalah IP khusus Emulator Android untuk mengakses localhost komputer
    private static final String BASE_URL = "http://10.0.2.2:3000/";
    private static Retrofit retrofit = null;

    public static ApiService getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Pengubah JSON otomatis
                    .build();
        }
        com.example.greenearth.ApiService apiService = retrofit.create(ApiService.class);
        return apiService;
    }
}