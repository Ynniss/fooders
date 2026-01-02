<p align="center">
  <img src="app/src/main/ic_launcher-playstore.png" width="150" alt="ForkLife Logo"/>
</p>

<h1 align="center">ForkLife</h1>

<p align="center">
  <strong>Scan. Discover. Eat Better.</strong>
</p>

<p align="center">
  A modern Android app for scanning food products and discovering their nutritional information, environmental impact, and ingredients.
</p>

<p align="center">
  <a href="https://android.com"><img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Platform"/></a>
  <a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-1.9.25-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/></a>
  <a href="https://developer.android.com/jetpack/compose"><img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose"/></a>
  <a href="https://m3.material.io"><img src="https://img.shields.io/badge/Material%203-757575?style=for-the-badge&logo=materialdesign&logoColor=white" alt="Material 3"/></a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Min%20SDK-21-blue?style=flat-square" alt="Min SDK"/>
  <img src="https://img.shields.io/badge/Target%20SDK-35-blue?style=flat-square" alt="Target SDK"/>
  <img src="https://img.shields.io/badge/Version-1.0.0-green?style=flat-square" alt="Version"/>
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=flat-square" alt="License"/>
</p>

---

## Screenshots

<p align="center">
  <img src="screenshots/1_login.png" width="200" alt="Login Screen"/>
  <img src="screenshots/2_home.png" width="200" alt="Home Screen"/>
  <img src="screenshots/3_scan.png" width="200" alt="Scan Screen"/>
  <img src="screenshots/4_product.png" width="200" alt="Product Details"/>
</p>

<p align="center">
  <img src="screenshots/5_ingredients.png" width="200" alt="Ingredients"/>
  <img src="screenshots/6_environment.png" width="200" alt="Environment Impact"/>
  <img src="screenshots/7_profile.png" width="200" alt="Profile"/>
  <img src="screenshots/8_settings.png" width="200" alt="Settings"/>
</p>

---

## Features

### Barcode Scanning
- **Real-time scanning** with CameraX and ML Kit
- **Yuka-style overlay** with elegant scanning frame
- **Manual entry** option for damaged barcodes
- Access to **3+ million products** via OpenFoodFacts

### Product Information
| Nutri-Score | NOVA Group | Eco-Score |
|:-----------:|:----------:|:---------:|
| Nutritional quality rating (A-E) | Food processing level (1-4) | Environmental impact (A-E) |

- **Score Tab** - Overall health and environmental ratings
- **Characteristics Tab** - Nutritional facts and allergens
- **Ingredients Tab** - Full ingredient list with analysis
- **Environment Tab** - Packaging and carbon footprint data

### Personalization
- **3 Beautiful Themes** - Orange (default), Avocado, Cherry
- **Dark Mode Support** - System, Light, or Dark
- **Scan History** - Track all your scanned products
- **User Profiles** - Statistics and achievements

---

## Tech Stack

| Category | Technologies |
|----------|-------------|
| **UI** | Jetpack Compose, Material 3, Lottie Animations |
| **Architecture** | Single Activity, MVVM, Repository Pattern |
| **DI** | Dagger Hilt |
| **Networking** | Retrofit, Gson |
| **Database** | Room, DataStore |
| **Camera** | CameraX, ML Kit Barcode Scanning |
| **Image Loading** | Coil |
| **Navigation** | Navigation Compose (Type-safe) |

---

## Architecture

```
app/
├── data/
│   ├── local/          # Room database & DataStore
│   └── remote/         # Retrofit API & responses
├── di/                 # Hilt modules
├── ui/
│   ├── components/     # Reusable Compose components
│   ├── navigation/     # Navigation graph & routes
│   ├── theme/          # Material 3 theming
│   ├── home/           # Home screen
│   ├── scan/           # Scanner & product details
│   ├── history/        # Scan history
│   ├── profile/        # User profile & achievements
│   └── settings/       # App settings
└── utils/              # Utility classes
```

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 11+
- Android SDK 35

### Build

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/ForkLife.git
cd ForkLife

# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug
```

### Run Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

---

## API

ForkLife uses the [OpenFoodFacts API](https://world.openfoodfacts.org/) - the free, open food products database.

- **No authentication required** for read access
- **3+ million products** worldwide
- **Community-driven** and constantly growing

---

## Color Themes

<table>
  <tr>
    <td align="center">
      <img src="https://via.placeholder.com/60/FF6D00/FFFFFF?text=+" alt="Orange"/>
      <br/>
      <strong>Orange</strong>
      <br/>
      <sub>Default</sub>
    </td>
    <td align="center">
      <img src="https://via.placeholder.com/60/7CB342/FFFFFF?text=+" alt="Avocado"/>
      <br/>
      <strong>Avocado</strong>
      <br/>
      <sub>Fresh Green</sub>
    </td>
    <td align="center">
      <img src="https://via.placeholder.com/60/C62828/FFFFFF?text=+" alt="Cherry"/>
      <br/>
      <strong>Cherry</strong>
      <br/>
      <sub>Bold Red</sub>
    </td>
  </tr>
</table>

---

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## Acknowledgments

- [OpenFoodFacts](https://world.openfoodfacts.org/) for the amazing food database
- [Material Design 3](https://m3.material.io/) for the beautiful design system
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for modern Android UI

---

## License

```
MIT License

Copyright (c) 2024 Vourourou

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

<p align="center">
  Made with :fork_and_knife: by <a href="https://github.com/YOUR_USERNAME">Vourourou</a>
</p>

<p align="center">
  <a href="#top">Back to top</a>
</p>
