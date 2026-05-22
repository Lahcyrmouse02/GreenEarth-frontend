package com.example.greenearth;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Menampilkan FoodFragment sebagai halaman pertama kali dibuka
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FoodFragment()).commit();
            bottomNav.setSelectedItemId(R.id.nav_food); // Set highlight menu aktif
        }

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_transit) {
                    selectedFragment = new TransitFragment();
                } else if (itemId == R.id.nav_energy) {
                    selectedFragment = new EnergyFragment();
                } else if (itemId == R.id.nav_food) {
                    selectedFragment = new FoodFragment();
                } else if (itemId == R.id.nav_summary) {
                    selectedFragment = new SummaryFragment(); // Tambahan baru
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }
                return true;
            }
        });
    }
}