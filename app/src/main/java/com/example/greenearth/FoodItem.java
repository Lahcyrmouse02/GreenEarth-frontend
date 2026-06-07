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

    // Tambahan: Atribut untuk menyimpan URL gambar dari database MySQL
    @SerializedName("image_url")
    private String imageUrl;

    public String getName() { return name; }
    public String getImpactLabel() { return impactLabel; }
    public float getCarbonValue() { return carbonValue; }

    // Tambahan: Getter untuk Glide
    public String getImageUrl() { return imageUrl; }

    // Metode bawaan tetap dipertahankan sebagai cadangan (fallback)
    public int getImageResId() {
        return android.R.drawable.ic_menu_gallery;
    }
}