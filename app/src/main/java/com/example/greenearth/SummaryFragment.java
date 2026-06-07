package com.example.greenearth;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummaryFragment extends Fragment {

    private TextView txtTotalSaved, txtTodayFootprint, txtGoalStatus, txtMomentumTitle, txtMomentumDesc;

    // Komponen baru untuk Breakdown & Reset
    private RecyclerView rvBreakdown;
    private BreakdownAdapter breakdownAdapter;
    private List<BreakdownItem> breakdownList = new ArrayList<>();
    private Button btnResetData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        txtTotalSaved = view.findViewById(R.id.txtTotalSaved);
        txtTodayFootprint = view.findViewById(R.id.txtTodayFootprint);
        txtGoalStatus = view.findViewById(R.id.txtGoalStatus);
        txtMomentumTitle = view.findViewById(R.id.txtMomentumTitle);
        txtMomentumDesc = view.findViewById(R.id.txtMomentumDesc);

        // 1. Inisialisasi RecyclerView Breakdown
        rvBreakdown = view.findViewById(R.id.rvBreakdown);
        rvBreakdown.setLayoutManager(new LinearLayoutManager(getContext()));
        breakdownAdapter = new BreakdownAdapter(breakdownList);
        rvBreakdown.setAdapter(breakdownAdapter);

        // 2. Inisialisasi Tombol Reset
        btnResetData = view.findViewById(R.id.btnResetData);
        if (btnResetData != null) {
            btnResetData.setOnClickListener(v -> resetDataToday());
        }

        // 3. Tarik data dari server saat dibuka
        fetchSummaryData();

        return view;
    }

    private void fetchSummaryData() {
        RetrofitClient.getApi().getSummary().enqueue(new Callback<SummaryResponse>() {
            @Override
            public void onResponse(@NonNull Call<SummaryResponse> call, @NonNull Response<SummaryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    float todayCarbon = response.body().getData().getTodaysFootprints();
                    float savedCarbon = response.body().getData().getCarbonSaved();

                    if (txtTotalSaved != null) txtTotalSaved.setText(String.format("%.1f kg CO₂", savedCarbon));
                    if (txtTodayFootprint != null) txtTodayFootprint.setText(String.format("%.1f kg", todayCarbon));

                    applyThresholdLogic(todayCarbon);

                    // 4. Update List Breakdown di layar
                    if (response.body().getData().getBreakdownList() != null) {
                        breakdownList.clear();
                        breakdownList.addAll(response.body().getData().getBreakdownList());
                        breakdownAdapter.notifyDataSetChanged();
                    }

                } else {
                    if (getContext() != null) Toast.makeText(getContext(), "Gagal memuat ringkasan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SummaryResponse> call, @NonNull Throwable t) {
                if (getContext() != null) Toast.makeText(getContext(), "Error jaringan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetDataToday() {
        RetrofitClient.getApi().resetTodayData().enqueue(new Callback<LogResponse>() {
            @Override
            public void onResponse(@NonNull Call<LogResponse> call, @NonNull Response<LogResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Data hari ini direset!", Toast.LENGTH_SHORT).show();
                    // Tarik ulang data dari server (otomatis mengosongkan layar)
                    fetchSummaryData();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LogResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Gagal mereset data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyThresholdLogic(float todayCarbon) {
        // Logika warna Hijau/Oranye/Merah tetap sama seperti kodemu sebelumnya
        if (txtGoalStatus == null || txtMomentumTitle == null || txtMomentumDesc == null) return;

        if (todayCarbon <= 5.0f) {
            txtGoalStatus.setText("Goal: Under 10 kg (Excellent)");
            txtGoalStatus.setTextColor(0xFF2E7D32);
            txtMomentumTitle.setText("Great momentum!");
            txtMomentumTitle.setTextColor(0xFF2E7D32);
            txtMomentumDesc.setText("Luar biasa! Emisi harianmu sangat rendah hari ini. Terus pertahankan kebiasaan ramah lingkunganmu.");
        } else if (todayCarbon <= 10.0f) {
            txtGoalStatus.setText("Goal: Under 10 kg (Warning)");
            txtGoalStatus.setTextColor(0xFFEF6C00);
            txtMomentumTitle.setText("On Track!");
            txtMomentumTitle.setTextColor(0xFFEF6C00);
            txtMomentumDesc.setText("Bagus, kamu masih berada di ambang batas wajar. Coba kurangi sedikit emisi besok.");
        } else {
            txtGoalStatus.setText("Goal: Over 10 kg (Critical)");
            txtGoalStatus.setTextColor(0xFFD84315);
            txtMomentumTitle.setText("High Emissions");
            txtMomentumTitle.setTextColor(0xFFD84315);
            txtMomentumDesc.setText("Perhatian! Emisi hari ini cukup tinggi. Pertimbangkan untuk menggunakan transportasi umum besok.");
        }
    }
}