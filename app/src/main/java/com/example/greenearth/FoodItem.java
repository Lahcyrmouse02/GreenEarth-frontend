package com.example.greenearth;

import com.google.gson.annotations.SerializedName;

public class FoodItem {
    private int id;
    private String name;
    private String category;

    @SerializedName("carbon_value")
    private float carbonValue;

    @SerializedName("impact_label")
    private String impactLabel;

    // Untuk sementara kita gunakan ikon bawaan karena URL gambar belum ada di DB
    public int getImageResId() {
        return android.R.drawable.ic_menu_gallery;
    }

    public String getName() { return name; }
    public String getImpactLabel() { return impactLabel; }
    public float getCarbonValue() { return carbonValue; }
}