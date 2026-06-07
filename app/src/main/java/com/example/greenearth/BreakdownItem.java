package com.example.greenearth;

import com.google.gson.annotations.SerializedName;

public class BreakdownItem {

    @SerializedName("type")
    private String type; // "Transit", "Energy", "Food"

    @SerializedName("title")
    private String description; // Contoh: "15 miles • Bus"

    @SerializedName("total_carbon")
    private float carbonValue;

    public String getType() { return type; }
    public String getDescription() { return description; }
    public float getCarbonValue() { return carbonValue; }
}