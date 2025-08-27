# 🌤️ Android Weather & Air Quality App

An **Android app built in Java** that displays:

* 📅 **4-day hourly weather forecast** (Karachi)
* 🌍 **Real-time Air Quality Index (AQI)** (Karachi)
* 🚀 **Splash screen** as app launcher
* 🔀 **Navigation widget** to switch between activities

This project uses **free public APIs** (no signup required).

## 📸 Screenshots
<img width="3780" height="1890" alt="Your paragraph text" src="https://github.com/user-attachments/assets/ed183123-e707-4971-a846-6daf040598c1" />


---

## 📱 Features

* **Weather Screen** → Fetches and shows hourly forecast for 4 days (Karachi).
* **Air Quality Screen** → Fetches live AQI values for Karachi.
* **Splash Screen** → Clean intro screen before app loads.
* **Activity Navigation** → Buttons to move between Weather and Air Quality screens.
* **Built with Java** in Android Studio.

---

## Tech Stack

* **Language:** Java
* **Framework:** Android SDK
* **IDE:** Android Studio
* **APIs Used:**

  * 🌦️ [Wttr.in](https://wttr.in) → free weather API
  * 🌍 [AQICN / WAQI API](https://aqicn.org/api/) → free demo endpoint for Air Quality

---

## Getting Started

### Prerequisites

* Install [Android Studio](https://developer.android.com/studio)
* Minimum SDK: **24 (Android 7.0)**
* Target SDK: **36**

### Setup

```bash
# Clone this repository
git clone https://github.com/Nawal-Shahid/android-weather-airquality-app

# Open project in Android Studio
# Let Gradle sync automatically
```

### Run

* Connect your Android device or emulator.
* Hit ▶️ **Run** in Android Studio.

---

## Project Structure

```
app/src/main/java/com/example/lab4app/
 ├── SplashActivity.java       # Splash screen
 ├── MainActivity.java         # Main menu with navigation
 ├── WeatherActivity.java      # Weather forecast screen
 └── AirQualityActivity.java   # Air quality screen

app/src/main/res/layout/
 ├── activity_splash.xml
 ├── activity_main.xml
 ├── activity_weather.xml
 └── activity_air_quality.xml
```



## Learning Outcomes

* ✅ Integrating **REST APIs** in Android.
* ✅ Handling **AsyncTask** for networking.
* ✅ Using **Activities & Navigation** in Android.
* ✅ Implementing **Splash Screen & Launcher activity**.

---

## 📌 Tags

`android` `java` `weather` `air-quality` `open-source` `student-project` `karachi` `mobile-app` `android-studio` `api-integration`

---

## 📜 License

This project is for **educational purposes** (Lab assignment). Feel free to fork, modify, and improve.

---
