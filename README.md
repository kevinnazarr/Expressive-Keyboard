# Expressive Keyboard

Aplikasi Android Keyboard bergaya **Material 3 Expressive**, dibangun dengan **Kotlin 2.0.0** dan **Jetpack Compose**.
Kini dikembangkan khusus dengan fokus penuh pada **Bahasa Indonesia** dan **English (Inggris)**.

---

## 🇮🇩 Ringkasan Fitur (Bahasa Indonesia)

- **Bahasa & Tata Letak Terfokus:**
  - Hanya mendukung **Bahasa Indonesia (ID)** dan **English (EN)** dengan tata letak QWERTY yang bersih.
  - Kamus prediksi frekuensi kata bawaan untuk Bahasa Indonesia (`id.txt`) dan Bahasa Inggris (`en.txt`).
  - Koreksi otomatis cerdas, deteksi typo umum Bahasa Indonesia & Inggris, serta kapitalisasi otomatis.
- **Bahasa Antarmuka Aplikasi (I18n):**
  - Pilihan bahasa UI: **Bahasa Indonesia** dan **English** (dapat mengikuti bahasa sistem atau diatur manual).
- **Desain & Tema Expressive:**
  - *Material You Expressive* (warna dinamis mengikuti wallpaper Android / Monet).
  - Pilihan gaya visual: *Material 3*, *Liquid Glass* (efek kaca transparan & blur), *Classic*, *Neon*, dan *Neumorphism*.
  - Dukungan font variabel Nunito bawaan atau font kustom (.ttf / .otf).
  - Foto latar belakang kustom dari galeri di belakang tombol keyboard.
- **Ketik Geser (Glide Typing):**
  - Ketik cepat tanpa mengangkat jari dengan jejak animasi partikel.
- **Emoji & Media:**
  - Emoji Unicode 16 lengkap dengan pencarian dalam Bahasa Indonesia & Inggris.
  - Panel stiker/GIF (mendukung KLIPY API dan Giphy API, serta fallback koleksi lokal).
- **Kecerdasan Buatan (AI Text Proofreading):**
  - Koreksi teks lokal tanpa internet menggunakan Gemini Nano (AICore), atau via Anthropic Claude / LanguageTool.
- **Plugin & Modifikasi (JavaScript):**
  - Mendukung plugin berbasis JavaScript (Rhino) untuk menambah tombol kustom, reaksi kata, panel tambahan, dan efek visual di atas keyboard.

---

## 🇬🇧 Features Summary (English)

- **Focused Languages & Layouts:**
  - Exclusively supports **Bahasa Indonesia (ID)** and **English (EN)** QWERTY layouts.
  - Built-in frequency prediction dictionaries for both Indonesian (`id.txt`) and English (`en.txt`).
  - Smart autocorrect, common typo fixes, and auto-capitalization.
- **App Interface Language (I18n):**
  - Clean bilingual UI options: **Bahasa Indonesia** and **English** (follows system locale or manual selection).
- **Expressive Design & Theming:**
  - Material You Expressive dynamic theming (wallpaper-based Monet palette).
  - Diverse visual styles: Material 3, Liquid Glass, Classic, Neon, and Neumorphism.
  - Built-in rounded variable font (Nunito) or custom fonts (.ttf/.otf).
  - Custom background photo from device gallery.
- **Glide Typing:**
  - Continuous gesture typing with smooth finger trail animation.
- **Media & Emoji:**
  - Full Unicode 16 emoji palette with bilingual search.
  - GIF & sticker panel with KLIPY / Giphy API integration and built-in offline collection.
- **AI Text Proofreading:**
  - Offline on-device text correction via Google Gemini Nano (AICore), plus Claude API and LanguageTool.
- **Extensible JavaScript Plugins & Mods:**
  - Sandboxed JS engine (Rhino) allowing custom actions, extra panels, toolbar buttons, and canvas overlay animations.

---

## Kompilasi dari Source Code (Building from Source)

### Prasyarat Environment:
- **JDK 17** (Temurin 17 / OpenJDK 17)
- **Android SDK Platform 35** (`platforms;android-35`)
- **Android SDK Build-Tools 35.0.0** & **34.0.0**
- File `local.properties` berisi `sdk.dir=<path_android_sdk>`

### Langkah Build:

```bash
git clone https://github.com/kevinnazarr/Expressive-Keyboard.git
cd Expressive-Keyboard

# Pastikan script gradlew executable
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug
```

Hasil file APK debug akan berada di:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## Layanan GIF & API Key (Opsional)
Untuk mengaktifkan pencarian GIF online via KLIPY atau Giphy, Anda dapat menyematkan key di `local.properties`:
```properties
KLIPY_API_KEY=your_klipy_key_here
GIPHY_API_KEY=your_giphy_key_here
```
Atau masukkan langsung melalui menu **Pengaturan → Papan Klip & GIF** di dalam aplikasi. Jika dikosongkan, aplikasi akan menggunakan koleksi GIF bawaan.

---

## Lisensi
[MIT License](LICENSE)
