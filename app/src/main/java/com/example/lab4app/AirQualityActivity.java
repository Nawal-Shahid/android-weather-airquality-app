package com.example.lab4app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AirQualityActivity extends AppCompatActivity {

    private TextView tvAir;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_quality);

        tvAir = findViewById(R.id.tvAir);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if (NetworkUtils.isNetworkAvailable(this)) {
            new FetchAir().execute();
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            tvAir.setText("Please check your internet connection and try again.");
        }

        // Setup Bottom Navigation
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Go to MainActivity
                    Intent intent = new Intent(AirQualityActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_weather) {
                    // Go to WeatherActivity
                    startActivity(new Intent(AirQualityActivity.this, WeatherActivity.class));
                    return true;
                } else if (itemId == R.id.nav_air_quality) {
                    // Already on air quality page, do nothing
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set the air quality item as selected when in this activity
        bottomNavigationView.setSelectedItemId(R.id.nav_air_quality);
    }

    private class FetchAir extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://api.waqi.info/feed/karachi/?token=demo");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                    return result.toString();
                } else {
                    return "Error: " + responseCode;
                }
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result.startsWith("Error:")) {
                    tvAir.setText(result);
                    return;
                }

                JSONObject jsonResponse = new JSONObject(result);
                if (jsonResponse.getString("status").equals("ok")) {
                    JSONObject data = jsonResponse.getJSONObject("data");
                    int aqi = data.getInt("aqi");
                    JSONObject iaqi = data.getJSONObject("iaqi");

                    StringBuilder airQualityData = new StringBuilder();
                    airQualityData.append("Air Quality in Karachi\n\n");
                    airQualityData.append("Overall AQI: ").append(aqi).append("\n\n");

                    // Add AQI interpretation
                    String aqiLevel;
                    if (aqi <= 50) aqiLevel = "Good";
                    else if (aqi <= 100) aqiLevel = "Moderate";
                    else if (aqi <= 150) aqiLevel = "Unhealthy for Sensitive Groups";
                    else if (aqi <= 200) aqiLevel = "Unhealthy";
                    else if (aqi <= 300) aqiLevel = "Very Unhealthy";
                    else aqiLevel = "Hazardous";

                    airQualityData.append("Air Quality: ").append(aqiLevel).append("\n\n");

                    // Add pollutant data if available
                    if (iaqi.has("pm25")) {
                        airQualityData.append("PM2.5: ").append(iaqi.getJSONObject("pm25").getDouble("v")).append(" μg/m³\n");
                    }
                    if (iaqi.has("pm10")) {
                        airQualityData.append("PM10: ").append(iaqi.getJSONObject("pm10").getDouble("v")).append(" μg/m³\n");
                    }
                    if (iaqi.has("no2")) {
                        airQualityData.append("NO2: ").append(iaqi.getJSONObject("no2").getDouble("v")).append(" μg/m³\n");
                    }
                    if (iaqi.has("so2")) {
                        airQualityData.append("SO2: ").append(iaqi.getJSONObject("so2").getDouble("v")).append(" μg/m³\n");
                    }
                    if (iaqi.has("o3")) {
                        airQualityData.append("O3: ").append(iaqi.getJSONObject("o3").getDouble("v")).append(" μg/m³\n");
                    }

                    tvAir.setText(airQualityData.toString());
                } else {
                    tvAir.setText("Failed to get air quality data");
                }
            } catch (Exception e) {
                tvAir.setText("Failed to parse air quality data: " + e.getMessage());
            }
        }
    }
}