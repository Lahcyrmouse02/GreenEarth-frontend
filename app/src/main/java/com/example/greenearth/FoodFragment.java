package com.example.greenearth; // GANTI dengan nama package aplikasimu

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

    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodItem> foodList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFood);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        foodAdapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(foodAdapter);

        // Menjalankan fungsi ambil data
        fetchFoodDataFromBackend();

        Button btnLogMeal = view.findViewById(R.id.btnLogMeal);
        TextView textTotalCarbon = view.findViewById(R.id.textTotalCarbon);

        if (btnLogMeal != null && textTotalCarbon != null) {
            btnLogMeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 1. Ambil teks dari layar (Misal: "15.2 kg CO2")
                    String rawText = textTotalCarbon.getText().toString();

                    // 2. Bersihkan teks agar hanya menyisakan angka (Hapus " kg CO2")
                    String cleanNumber = rawText.replace(" kg CO2", "").trim();

                    try {
                        // 3. Ubah teks angka menjadi tipe Float
                        float totalCarbon = Float.parseFloat(cleanNumber);

                        // 4. Bungkus data ke dalam objek Request
                        LogRequest request = new LogRequest(totalCarbon, "Makan siang dari aplikasi");

                        // 5. Tembak API POST ke Backend Docker
                        RetrofitClient.getApi().logMeal(request).enqueue(new Callback<LogResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<LogResponse> call, @NonNull Response<LogResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    // Berhasil masuk ke MySQL!
                                    Toast.makeText(getContext(), "Sukses: " + response.body().getMessage(), Toast.LENGTH_LONG).show();
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

                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Format angka salah", Toast.LENGTH_SHORT).show();
                    }
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