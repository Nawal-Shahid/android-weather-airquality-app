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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    private TextView tvWeather;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tvWeather = findViewById(R.id.tvWeather);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if (NetworkUtils.isNetworkAvailable(this)) {
            new FetchWeather().execute();
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            tvWeather.setText("Please check your internet connection and try again.");
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
                    Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_weather) {
                    // Already on weather page, do nothing
                    return true;
                } else if (itemId == R.id.nav_air_quality) {
                    // Go to AirQualityActivity
                    startActivity(new Intent(WeatherActivity.this, AirQualityActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set the weather item as selected when in this activity
        bottomNavigationView.setSelectedItemId(R.id.nav_weather);
    }

    private class FetchWeather extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://wttr.in/Karachi?format=j1");
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
                    tvWeather.setText(result);
                    return;
                }

                JSONObject jsonResponse = new JSONObject(result);
                JSONArray weatherArray = jsonResponse.getJSONArray("weather");
                StringBuilder weatherData = new StringBuilder();
                weatherData.append("Weather Forecast for Karachi\n\n");

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat displayFormat = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());

                for (int i = 0; i < Math.min(4, weatherArray.length()); i++) {
                    JSONObject day = weatherArray.getJSONObject(i);
                    String dateStr = day.getString("date");
                    Date date = dateFormat.parse(dateStr);

                    weatherData.append(displayFormat.format(date)).append(":\n");

                    JSONArray hourlyArray = day.getJSONArray("hourly");
                    for (int j = 0; j < hourlyArray.length(); j += 3) {
                        JSONObject hour = hourlyArray.getJSONObject(j);
                        String time = hour.getString("time");
                        if (time.length() < 4) time = "0" + time;
                        time = time.substring(0, 2) + ":00";

                        String tempC = hour.getString("tempC");
                        String desc = hour.getJSONArray("weatherDesc")
                                .getJSONObject(0)
                                .getString("value");

                        weatherData.append("  ").append(time).append(": ")
                                .append(tempC).append("°C, ").append(desc).append("\n");
                    }
                    weatherData.append("\n");
                }

                tvWeather.setText(weatherData.toString());
            } catch (Exception e) {
                tvWeather.setText("Failed to parse weather data: " + e.getMessage());
            }
        }
    }
}