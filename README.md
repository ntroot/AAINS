# AAINS (Android Auto Apps Installer)

**AAINS** is an open-source Android application that allows you to easily and limitlessly download and install third-party apps for Android Auto (YouTube, screen mirroring, vehicle telemetry, and media players) without requiring root.

---

## ⚠️ Disclaimer

- This application is developed for educational, testing, and research purposes only.
- Driving while distracted can cause serious injury or death. Do not watch videos, interact with complex apps, or use distracting features while operating a moving vehicle. Always keep your eyes on the road and comply with local traffic laws and regulations.
- The developers assume no liability or responsibility for any damages, traffic violations, accidents, system malfunctions, or warranty voids resulting from the use of this software or any third-party applications installed through it. Use at your own risk.
- Android, Android Auto, and Google Play are trademarks of Google LLC. This project is not affiliated with, authorized, maintained, or endorsed by Google LLC.

---


## 🙏 Acknowledgements & Fork Information

This project is a fork of **[AAAD (Android Auto Apps Downloader)](https://github.com/shmykelsa/AAAD)**, originally developed by **Gabriele Rizzo ([shmykelsa](https://github.com/shmykelsa))**. 

We extend our sincere gratitude and respect to **Gabriele Rizzo** and the entire AAAD community for their pioneering contributions to the Android Auto ecosystem, innovative ideas, and the open-source foundation that made this project possible.

Special thanks also go to all third-party Android Auto app developers (Annexhack, Andrey Pavlenko, Slashmax, Kiran Kumar, agronick, jilleb, kododake, FrankieNL, ns130291, Martoreto) who make this in-car experience possible.

---

## 🚀 Key Features

- **Completely Free & Unlimited:** Download any app at any time without monthly download limits or paywalls.
- **Android 14+ & Shizuku Support:** Integrated with the **Shizuku API** to bypass restrictions on Android 14 and above. Silent and guaranteed installation is achieved by assigning the Google Play Store installer identity (`com.android.vending`).
- **3-Tier Installation Engine:**
  1. *Priority 1 (Shizuku):* Wireless ADB / rootless elevated installation (Recommended).
  2. *Priority 2 (PackageInstaller Session):* Session-based installation with `setInstallerPackageName("com.android.vending")`.
  3. *Priority 3 (System Intent):* Standard FileProvider system package installer fallback.
- **Modern Material 3 UI:** Category filter chips (Media & Video, Screen Mirroring, Telemetry, Tools), real-time download progress bar (0–100%), and status badges.
- **Verified & Up-to-Date App Catalog:**
  - **CarStream:** YouTube and web browser for Android Auto (v2.0.5).
  - **Fermata Auto:** Advanced media player, IPTV, and web browser (v2.0.2).
  - **Screen2Auto & AA Mirror & AA Mirror Plus & AA Stream:** Full phone screen mirroring to car displays.
  - **AA Torque & Performance Monitor:** OBD-II sensor and VAG vehicle telemetry data.
  - **AA Browser, Nav2Contacts, Widgets for AA, AA Passenger:** Utility tools.
- **Built-in Android Auto Setup Guide:** An interactive walkthrough covering the "Developer Settings" and "Unknown Sources" steps required for apps to appear on the head unit display.

---

## 🛠️ Build & Installation

Build the project using Gradle:

```bash
# Build the project
./gradlew assembleDebug
```
