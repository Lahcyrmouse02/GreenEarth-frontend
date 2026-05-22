package com.example.greenearth; // Sesuaikan dengan package-mu

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SummaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Hanya menyambungkan Java dengan fragment_summary.xml
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        // Nanti di sini kita akan menambahkan logika untuk mengambil
        // total data emisi harian dari Database Backend / SQLite
        // dan mengubah teks angka di layar secara otomatis.

        return view;
    }
}