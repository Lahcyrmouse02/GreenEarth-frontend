package com.example.greenearth; // Sesuaikan dengan nama package-mu

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransitFragment extends Fragment {

    private TextView textCarbonResult, textDistanceValue;
    private Slider sliderDistance;
    private ChipGroup chipGroupTransit;
    private Button btnLogJourney;

    private String currentMode = "Car"; // Default pilihan awal

    // Variabel tambahan untuk menyimpan hasil hitung angka murni (tanpa teks kg)
    private float currentTotalCarbon = 0.0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transit, container, false);

        // 1. Hubungkan variabel Java dengan ID di XML
        textCarbonResult = view.findViewById(R.id.textCarbonResult);
        textDistanceValue = view.findViewById(R.id.textDistanceValue);
        sliderDistance = view.findViewById(R.id.sliderDistance);
        chipGroupTransit = view.findViewById(R.id.chipGroupTransit);
        btnLogJourney = view.findViewById(R.id.btnLogJourney);

        // 2. Deteksi saat pengguna memilih kendaraan lain (Car/Bus/Train)
        chipGroupTransit.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                if (!checkedIds.isEmpty()) {
                    Chip selectedChip = group.findViewById(checkedIds.get(0));
                    currentMode = selectedChip.getText().toString();
                    calculateEmissions();
                }
            }
        });

        // 3. Deteksi saat Slider digeser
        sliderDistance.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                calculateEmissions();
            }
        });

        // 4. Aksi saat tombol Log ditekan (Simpan ke Database)
        btnLogJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cegah penyimpanan jika jarak masih 0
                if (currentTotalCarbon <= 0) {
                    Toast.makeText(getContext(), "Tentukan jarak perjalanan terlebih dahulu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Ambil jarak dari slider
                float distance = sliderDistance.getValue();

                // Buat deskripsi perjalanan dinamis (Contoh: "15.0 km • Bus")
                String deskripsiPerjalanan = String.format("%.1f km • %s", distance, currentMode);

                // Bungkus ke LogRequest dengan tipe "Transit"
                LogRequest request = new LogRequest("Transit", deskripsiPerjalanan, currentTotalCarbon);

                // Tembak API ke Backend
                // (Catatan: Tetap panggil .logMeal() karena endpoint-nya sama-sama ke /api/logs)
                RetrofitClient.getApi().logMeal(request).enqueue(new Callback<LogResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LogResponse> call, @NonNull Response<LogResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Perjalanan berhasil dicatat!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Gagal mencatat perjalanan.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LogResponse> call, @NonNull Throwable t) {
                        Log.e("API_ERROR", "Gagal Save Transit: " + t.getMessage());
                        Toast.makeText(getContext(), "Error Jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }

    // Method khusus untuk melakukan perhitungan matematika
    private void calculateEmissions() {
        // 1. Ambil nilai jarak dari slider
        float distance = sliderDistance.getValue();

        // 2. Update teks jarak di layar
        textDistanceValue.setText(String.format("%.0f km", distance));

        // 3. Minta CarbonCalculator untuk menghitung
        double totalCarbon = CarbonCalculator.calculateTransitEmission(distance, currentMode);

        // 4. Simpan nilai murninya ke variabel global yang baru kita buat
        currentTotalCarbon = (float) totalCarbon;

        // 5. Update teks hasil di layar
        textCarbonResult.setText(String.format("%.1f kg CO₂", currentTotalCarbon));
    }
}