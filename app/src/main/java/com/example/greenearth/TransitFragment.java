package com.example.greenearth; // Sesuaikan dengan nama package-mu

import android.os.Bundle;
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

public class TransitFragment extends Fragment {

    private TextView textCarbonResult, textDistanceValue;
    private Slider sliderDistance;
    private ChipGroup chipGroupTransit;
    private Button btnLogJourney;

    // Asumsi faktor emisi (kg CO2 per mil)
    private double emissionFactor = 0.41; // Default untuk "Car"

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
                    // Ambil chip yang sedang diklik
                    Chip selectedChip = group.findViewById(checkedIds.get(0));
                    String mode = selectedChip.getText().toString();

                    // Ubah faktor pengali karbon berdasarkan kendaraan
                    if (mode.equals("Car")) {
                        emissionFactor = 0.41;
                    } else if (mode.equals("Bus")) {
                        emissionFactor = 0.17;
                    } else if (mode.equals("Train")) {
                        emissionFactor = 0.05;
                    }

                    // Hitung ulang jika mode berubah
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

        // 4. Aksi saat tombol Log ditekan
        btnLogJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Untuk sementara kita tampilkan pesan pop-up (Toast)
                // Nanti ini akan mengirim data ke Summary/Database
                String currentCarbon = textCarbonResult.getText().toString();
                Toast.makeText(getContext(), "Perjalanan disimpan: " + currentCarbon, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Method khusus untuk melakukan perhitungan matematika
    private void calculateEmissions() {
        // Ambil nilai jarak dari slider
        float distance = sliderDistance.getValue();

        // Update teks jarak (misal: "15 mi")
        textDistanceValue.setText(String.format("%.0f mi", distance));

        // Rumus: Jarak * Faktor Emisi
        double totalCarbon = distance * emissionFactor;

        // Update teks hasil karbon (misal: "6.2 kg CO2") dengan 1 angka di belakang koma
        textCarbonResult.setText(String.format("%.1f kg CO2", totalCarbon));
    }
}