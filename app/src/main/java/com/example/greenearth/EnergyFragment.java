package com.example.greenearth; // Sesuaikan dengan package kamu

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class EnergyFragment extends Fragment {

    private ProgressBar progressBarEnergy;
    private TextView textProgressPercent;

    private int completedHabits = 0;
    private final int TOTAL_HABITS = 4; // Asumsi ada 4 kebiasaan harian

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_energy, container, false);

        progressBarEnergy = view.findViewById(R.id.progressBarEnergy);
        textProgressPercent = view.findViewById(R.id.textProgressPercent);

        // Set awal ke 0%
        updateProgress();

        // Setup Habit 1 (Contoh untuk "Unplug standby appliances")
        CardView cardHabit1 = view.findViewById(R.id.cardHabit1);
        RadioButton radioHabit1 = view.findViewById(R.id.radioHabit1);

        if (cardHabit1 != null && radioHabit1 != null) {
            // Menonaktifkan klik langsung pada radio button agar user klik Card-nya saja
            radioHabit1.setClickable(false);

            cardHabit1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Balikkan state (jika belum dicentang jadi dicentang, dan sebaliknya)
                    boolean isChecked = !radioHabit1.isChecked();
                    radioHabit1.setChecked(isChecked);

                    // Ubah warna background Card sebagai penanda visual
                    if (isChecked) {
                        cardHabit1.setCardBackgroundColor(0xFFEAF6F0); // Hijau pudar
                        completedHabits++;
                    } else {
                        cardHabit1.setCardBackgroundColor(0xFFFFFFFF); // Putih
                        completedHabits--;
                    }
                    updateProgress();
                }
            });
        }

        // (Opsional) Lakukan copy-paste blok logika Habit 1 di atas untuk Habit 2, 3, dan 4

        return view;
    }

    private void updateProgress() {
        // Hitung persentase: (jumlah selesai / total) * 100
        int percentage = (int) (((float) completedHabits / TOTAL_HABITS) * 100);

        progressBarEnergy.setProgress(percentage);
        textProgressPercent.setText(percentage + "%");
    }
}