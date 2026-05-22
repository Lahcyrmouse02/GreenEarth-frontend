package com.example.greenearth; // Pastikan ini greenearth

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

// KATA "public" DI BAWAH INI WAJIB ADA AGAR BISA DIAKSES FRAGMENT
public interface ApiService {

    @GET("api/foods")
    Call<FoodResponse> getFoods(@Query("category") String category);

    @POST("api/logs")
    Call<LogResponse> logMeal(@Body LogRequest request);
}