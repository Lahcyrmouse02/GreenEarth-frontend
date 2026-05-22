package com.example.greenearth;

import java.util.List;

public class FoodResponse {
    private String status;
    private List<FoodItem> data; // Mengambil array JSON "data" dan mengubahnya menjadi List

    public String getStatus() { return status; }
    public List<FoodItem> getData() { return data; }
}