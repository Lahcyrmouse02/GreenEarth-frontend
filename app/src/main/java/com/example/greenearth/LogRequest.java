package com.example.greenearth;

import com.google.gson.annotations.SerializedName;

public class LogRequest {
    @SerializedName("type")
    private String type; // Akan diisi "Food", "Transit", atau "Energy"

    @SerializedName("title")
    private String title; // Keterangan aktivitas (misal: "15 miles • Bus")

    @SerializedName("total_carbon")
    private float totalCarbon; // Nilai karbonnya

    // Constructor Baru dengan 3 Parameter
    public LogRequest(String type, String title, float totalCarbon) {
        this.type = type;
        this.title = title;
        this.totalCarbon = totalCarbon;
    }

    // Getter dan Setter (Opsional, tapi baik untuk dokumentasi)
    public String getType() { return type; }
    public String getTitle() { return title; }
    public float getTotalCarbon() { return totalCarbon; }
}