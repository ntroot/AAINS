# AAINS (Android Auto Apps Installer)

**AAINS**, Android Auto için 3. parti uygulamaları (YouTube, ekran yansıtma, araç telemetri ve medya oynatıcıları) root gerektirmeden, kolay ve sınırsız bir şekilde indirip kurmanızı sağlayan açık kaynaklı bir Android uygulamasıdır.

---

## 👨‍💻 Geliştirici & İletişim

- **Geliştirici:** NTRooT
- **E-posta:** [ntroot@gmail.com](mailto:ntroot@gmail.com)

---

## 🙏 Teşekkür & Fork Bilgisi

Bu proje, **Gabriele Rizzo ([shmykelsa](https://github.com/shmykelsa))** tarafından geliştirilen açık kaynaklı **[AAAD (Android Auto Apps Downloader)](https://github.com/shmykelsa/AAAD)** projesinden fork edilerek geliştirilmiştir. 

Android Auto ekosistemine sundukları öncü katkılar, yenilikçi fikirler ve bu projeye zemin oluşturan açık kaynak temelleri için **Gabriele Rizzo**'ya ve tüm AAAD topluluğuna en içten teşekkürlerimizi ve saygılarımızı sunarız.

Ayrıca araçlarımızda bu deneyimi yaşamamızı sağlayan tüm 3. parti Android Auto uygulama geliştiricilerine (Annexhack, Andrey Pavlenko, Slashmax, Kiran Kumar, agronick, jilleb, kododake, FrankieNL, ns130291, Martoreto) teşekkür ederiz.

---

## 🚀 Öne Çıkan Özellikler

- **Tamamen Ücretsiz & Sınırsız:** Aylık indirme limiti veya ödeme engelleri olmaksızın dilediğiniz uygulamayı dilediğiniz zaman indirin.
- **Android 14+ & Shizuku Desteği:** Android 14 ve üzeri sürümlerdeki kısıtlamaları aşmak için **Shizuku API** entegrasyonu. Google Play Store kimliği (`com.android.vending`) atanarak sessiz ve garantili kurulum yapılır.
- **3 Katmanlı Kurulum Motoru:**
  1. *Öncelik 1 (Shizuku):* Kablosuz ADB / rootless yetkili kurulum (Önerilen).
  2. *Öncelik 2 (PackageInstaller Oturumu):* `setInstallerPackageName("com.android.vending")` oturumu.
  3. *Öncelik 3 (Sistem Intent):* Standart FileProvider sistem paket yükleyicisi.
- **Modern Material 3 Arayüzü:** Kategori filtreleme çipleri (Medya & Video, Ekran Yansıtma, Telemetri, Araçlar), anlık indirme ilerleme çubuğu (%0-100) ve durum rozetleri.
- **Doğrulanmış & Güncel Uygulama Kataloğu:**
  - **CarStream:** Android Auto için YouTube ve web tarayıcısı (v2.0.5).
  - **Fermata Auto:** Gelişmiş medya oynatıcı, IPTV ve web tarayıcı (v2.0.2).
  - **Screen2Auto & AA Mirror & AA Mirror Plus & AA Stream:** Telefon ekranını araç ekranına tam yansıtma.
  - **AA Torque & Performance Monitor:** OBD-II sensör ve VAG araç telemetri verileri.
  - **AA Browser, Nav2Contacts, Widgets for AA, AA Passenger:** Yardımcı araçlar.
- **Dahili Android Auto Kurulum Rehberi:** Araç ekranında uygulamaların görünebilmesi için gerekli "Geliştirici Ayarları" ve "Bilinmeyen Kaynaklar" adımlarını gösteren interaktif rehber.

---

## 🛠️ Kurulum ve Derleme

Projeyi Gradle ile derleyebilirsiniz:

```bash
# Projeyi derleyin
./gradlew assembleDebug
```

Derlenen APK dosyasını (`AAINS-v2.2-unlimited.apk`) telefonunuza yükleyip hemen kullanmaya başlayabilirsiniz.