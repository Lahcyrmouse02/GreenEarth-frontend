package com.example.greenearth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnergyFragment extends Fragment {

    private TextView txtProgressPercentage;
    private CheckBox cbHabit1, cbHabit2, cbHabit3, cbHabit4, cbHabit5;
    private Button btnSaveEnergy; // Tambahkan tombol save

    private final float TOTAL_HABITS = 5.0f;
    private int currentCheckedCount = 0; // Variabel penyimpan jumlah centang

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_energy, container, false);

        txtProgressPercentage = view.findViewById(R.id.txtProgressPercentage);
        cbHabit1 = view.findViewById(R.id.cbHabit1);
        cbHabit2 = view.findViewById(R.id.cbHabit2);
        cbHabit3 = view.findViewById(R.id.cbHabit3);
        cbHabit4 = view.findViewById(R.id.cbHabit4);
        cbHabit5 = view.findViewById(R.id.cbHabit5);

        // Pastikan di fragment_energy.xml kamu sudah menambah <Button android:id="@+id/btnSaveEnergy" .../>
        btnSaveEnergy = view.findViewById(R.id.btnSaveEnergy);

        CompoundButton.OnCheckedChangeListener checkListener = (buttonView, isChecked) -> calculateProgress();

        cbHabit1.setOnCheckedChangeListener(checkListener);
        cbHabit2.setOnCheckedChangeListener(checkListener);
        cbHabit3.setOnCheckedChangeListener(checkListener);
        cbHabit4.setOnCheckedChangeListener(checkListener);
        cbHabit5.setOnCheckedChangeListener(checkListener);

        // LOGIKA TOMBOL SAVE ENERGY
        if (btnSaveEnergy != null) {
            btnSaveEnergy.setOnClickListener(v -> {
                if (currentCheckedCount == 0) {
                    Toast.makeText(getContext(), "Ceklis minimal 1 kebiasaan!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Asumsi: 1 centang menghemat 0.5 kg CO2. Makin banyak diceklis, angka penghematan makin besar!
                float carbonSavedValue = currentCheckedCount * 0.5f;

                // Kirim data dengan TYPE "Energy"
                LogRequest request = new LogRequest("Energy", currentCheckedCount + " tasks completed", carbonSavedValue);

                // Tembak API POST ke server
                RetrofitClient.getApi().logMeal(request).enqueue(new Callback<LogResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LogResponse> call, @NonNull Response<LogResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Habits Saved! Karbon dihemat: " + carbonSavedValue + " kg", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LogResponse> call, @NonNull Throwable t) {
                        Log.e("API_ERROR", "Gagal Save Energy: " + t.getMessage());
                    }
                });
            });
        }

        return view;
    }

    private void calculateProgress() {
        currentCheckedCount = 0;

        if (cbHabit1.isChecked()) currentCheckedCount++;
        if (cbHabit2.isChecked()) currentCheckedCount++;
        if (cbHabit3.isChecked()) currentCheckedCount++;
        if (cbHabit4.isChecked()) currentCheckedCount++;
        if (cbHabit5.isChecked()) currentCheckedCount++;

        int percentage = (int) ((currentCheckedCount / TOTAL_HABITS) * 100);
        txtProgressPercentage.setText(percentage + "%");
    }
}