package com.example.greenearth;

public class CarbonCalculator {

    // Fungsi ini murni matematika, tidak butuh Context atau UI Android
    public static double calculateTransitEmission(double distanceKm, String mode) {
        double emissionFactor = 0.0;
        if (mode.equals("Car")) {
            emissionFactor = 0.255;
        } else if (mode.equals("Bus")) {
            emissionFactor = 0.106;
        } else if (mode.equals("Train")) {
            emissionFactor = 0.031;
        }
        return distanceKm * emissionFactor;
    }
}