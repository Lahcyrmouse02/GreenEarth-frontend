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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodFragment extends Fragment {

    // 1. Variabel penampung total karbon
    private float totalBottomFootprint = 0.0f;

    // 2. Deklarasi UI Kotak Bawah
    private TextView textTotalCarbon;

    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodItem> foodList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        // Hubungkan UI kotak bawah
        textTotalCarbon = view.findViewById(R.id.textTotalCarbon);
        Button btnLogMealBottom = view.findViewById(R.id.btnLogMeal);

        // Set teks awal
        if (textTotalCarbon != null) {
            textTotalCarbon.setText("0.0 kg CO2");
        }

        recyclerView = view.findViewById(R.id.recyclerViewFood);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // 3. INISIALISASI ADAPTER DENGAN LISTENER (JEMBATAN KOMUNIKASI)
        foodAdapter = new FoodAdapter(foodList, new FoodAdapter.OnLogMealClickListener() {
            @Override
            public void onMealClicked(float carbonValue) {
                // Saat tombol Log Meal di KARTU diklik, tambahkan angkanya
                totalBottomFootprint += carbonValue;

                // Perbarui teks di kotak cokelat bawah secara Real-Time
                if (textTotalCarbon != null) {
                    textTotalCarbon.setText(String.format("%.1f kg CO2", totalBottomFootprint));
                }
            }
        });
        recyclerView.setAdapter(foodAdapter);

        // Menjalankan fungsi ambil data (GET Makanan)
        fetchFoodDataFromBackend();

        // 4. LOGIKA TOMBOL LOG MEAL BAWAH (POST DATABASE)
        if (btnLogMealBottom != null) {
            btnLogMealBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Cegah pengiriman jika belum ada makanan yang dipilih (total masih 0)
                    if (totalBottomFootprint <= 0) {
                        Toast.makeText(getContext(), "Pilih makanan terlebih dahulu!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LogRequest request = new LogRequest("Food", "Food Log", totalBottomFootprint);


                    // Tembak API POST ke Backend Docker
                    RetrofitClient.getApi().logMeal(request).enqueue(new Callback<LogResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<LogResponse> call, @NonNull Response<LogResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                // Berhasil masuk ke MySQL!
                                Toast.makeText(getContext(), "Sukses: " + response.body().getMessage(), Toast.LENGTH_LONG).show();

                                // (Opsional) Reset ulang angka ke 0 setelah berhasil masuk database
                                totalBottomFootprint = 0.0f;
                                textTotalCarbon.setText("0.0 kg CO2");
                            } else {
                                Toast.makeText(getContext(), "Gagal menyimpan ke database", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<LogResponse> call, @NonNull Throwable t) {
                            Log.e("API_ERROR", "Gagal POST: " + t.getMessage());
                            Toast.makeText(getContext(), "Error jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        return view;
    }

    private void fetchFoodDataFromBackend() {
        RetrofitClient.getApi().getFoods(null).enqueue(new Callback<FoodResponse>() {
            @Override
            public void onResponse(@NonNull Call<FoodResponse> call, @NonNull Response<FoodResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    foodList.clear();
                    foodList.addAll(response.body().getData());
                    foodAdapter.notifyDataSetChanged();
                } else {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Gagal mengambil data dari server", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FoodResponse> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Koneksi Gagal: " + t.getMessage());
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Gagal terhubung ke backend: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}