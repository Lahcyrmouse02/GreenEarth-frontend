package com.example.greenearth;

import org.junit.Test;
import static org.junit.Assert.*;

public class CarbonCalculatorTest {

    @Test
    public void testCarEmission_isCorrect() {
        // Persiapan (Arrange)
        double distance = 10.0; // 10 mil
        String mode = "Car";

        // Eksekusi (Act)
        double result = CarbonCalculator.calculateTransitEmission(distance, mode);

        // Validasi (Assert)
        // 10 mil * 0.41 (faktor mobil) harusnya menghasilkan 4.1
        // Angka 0.001 di belakang adalah delta (toleransi perbedaan desimal)
        assertEquals(4.1, result, 0.001);
    }

    @Test
    public void testBusEmission_isCorrect() {
        double result = CarbonCalculator.calculateTransitEmission(10.0, "Bus");
        // 10 mil * 0.17 = 1.7
        assertEquals(1.7, result, 0.001);
    }

    @Test
    public void testTrainEmission_isCorrect() {
        double result = CarbonCalculator.calculateTransitEmission(10.0, "Train");
        // 10 mil * 0.05 = 0.5
        assertEquals(0.5, result, 0.001);
    }

    @Test
    public void testInvalidMode_returnsZero() {
        // Menguji bagaimana jika sistem mengirim kendaraan yang tidak dikenali
        double result = CarbonCalculator.calculateTransitEmission(10.0, "Pesawat");
        assertEquals(0.0, result, 0.001);
    }
}