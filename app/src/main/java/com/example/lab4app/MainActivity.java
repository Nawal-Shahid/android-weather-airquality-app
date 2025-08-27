package com.example.lab4app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnWeather = findViewById(R.id.btnWeather);
        Button btnAir = findViewById(R.id.btnAir);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WeatherActivity.class));
            }
        });

        btnAir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AirQualityActivity.class));
            }
        });

        // Setup Bottom Navigation
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        // Set the menu for the bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Already on home, just highlight it
                    return true;
                } else if (itemId == R.id.nav_weather) {
                    startActivity(new Intent(MainActivity.this, WeatherActivity.class));
                    return true;
                } else if (itemId == R.id.nav_air_quality) {
                    startActivity(new Intent(MainActivity.this, AirQualityActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set the home item as selected when returning to this activity
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }
}