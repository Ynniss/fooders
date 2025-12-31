# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ForkLife is an Android application for scanning and managing food product information developed by Vourourou mobile app studio. The app integrates with OpenFoodFacts API for product data and uses Google Play Games Services for authentication and leaderboards.

**Package name**: `com.vourourou.forklife`
**Application ID**: `com.vourourou.forklife`

## Build Commands

```bash
# Clean and build the project
./gradlew clean build

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Clean build artifacts
./gradlew clean
```

**Note:** The project requires `google-services.json` (Firebase configuration) to build successfully. Place it in `app/google-services.json` for local development.

## Device Testing

**IMPORTANT: Always rebuild and test the app after making changes.**

**Primary device**: Pixel 10 (physical device)
- Connect via USB or wireless debugging
- Run `adb devices` to verify connection
- Install with: `./gradlew installDebug` or `adb install app/build/outputs/apk/debug/app-debug.apk`

**Fallback**: Android Emulator
- Use emulator only if Pixel 10 is NOT connected
- Prefer Pixel-based emulator images for consistency

```bash
# Check connected devices
adb devices

# Install on connected device (prefers physical device)
./gradlew installDebug

# Launch the app
adb shell am start -n com.vourourou.forklife/.ui.main.MainActivity
```

## Architecture

### UI Architecture: Jetpack Compose + Material 3

The app uses a **single-activity, multiple-composables architecture** with Jetpack Compose and Material 3:

- **Single Activity**: `MainActivity` hosts the entire app using `setContent { ForkLifeApp() }`
- **Compose Navigation**: Type-safe navigation with `androidx.navigation:navigation-compose`
- **Material 3**: Full Material You design system with dynamic theming

### Dependency Injection (Dagger Hilt)

The app uses Dagger Hilt for dependency injection:
- `ForkLifeApplication` is annotated with `@HiltAndroidApp`
- `MainActivity` uses `@AndroidEntryPoint`
- ViewModels use `@HiltViewModel` and are accessed via `hiltViewModel()` in Composables
- DI configuration in `di/AppModule.kt`

Key provided dependencies:
- `ForkLifeApi` (Retrofit instance with 60s timeouts)
- `LoginRepository`
- `ScanRepository`
- `DataStoreManager` (injected via constructor)

### Data Layer

**API Integration**:
- **Custom Backend**: `https://us-central1-fooders-811cb.cloudfunctions.net/app/fooders/api/`
  - User login, statistics, rankings, and success tracking
- **OpenFoodFacts API**: `https://world.openfoodfacts.org/`
  - Product information retrieval (READ-only, no authentication required)

**API Interface**: `data/remote/ForkLifeApi.kt`

**Repositories**:
- `LoginRepository`: Handles user authentication
- `ScanRepository`: Manages product scanning and information retrieval

**Responses**: Located in `data/remote/responses/` with subdirectories for different response types (ProductInformations, UserSuccessResponse, RankingResponse)

### Presentation Layer

**UI Structure** (`ui/` package):

```
ui/
├── ForkLifeApp.kt              # Main app composable with navigation
├── theme/
│   ├── Color.kt              # Color definitions for all themes
│   ├── Type.kt               # Material 3 typography
│   ├── Shape.kt              # Material 3 shapes
│   └── Theme.kt              # ForkLifeTheme composable
├── navigation/
│   ├── Screen.kt             # Navigation routes
│   └── ForkLifeNavHost.kt     # Navigation graph
├── components/
│   ├── ForkLifeTopAppBar.kt   # App bar variants
│   ├── ForkLifeBottomNavigation.kt
│   ├── ForkLifeCard.kt        # Card components
│   ├── ForkLifeButton.kt      # Button variants
│   ├── ForkLifeTextField.kt   # Text input components
│   └── LoadingIndicator.kt   # Lottie loading animation
├── login/
│   ├── LoginScreen.kt
│   └── LoginViewModel.kt
├── home/
│   └── HomeScreen.kt         # Feature cards with gradients
├── scan/
│   ├── ScanScreen.kt         # CameraX + barcode scanning
│   ├── ManualScanScreen.kt
│   ├── ScanViewModel.kt
│   ├── ProductInfoSharedViewModel.kt
│   └── details/
│       ├── ScoreTab.kt
│       ├── CharacteristicsTab.kt
│       ├── IngredientsTab.kt
│       └── EnvironmentTab.kt
├── history/
│   └── HistoryScreen.kt      # Coming soon placeholder
├── profile/
│   ├── ProfileScreen.kt      # With HorizontalPager tabs
│   └── ProfileViewModel.kt
├── settings/
│   ├── SettingsScreen.kt
│   └── SettingsViewModel.kt
└── main/
    └── MainActivity.kt       # Single activity entry point
```

### Theme System

**Material 3 with 3 Color Schemes + Dark Mode**:

Color themes:
- **Orange** (default) - Vibrant orange primary
- **Avocado** - Fresh green primary
- **Cherry** - Bold red primary

Dark mode options:
- **System** - Follows device settings
- **Light** - Always light
- **Dark** - Always dark

Theme is applied via `ForkLifeTheme` composable:
```kotlin
ForkLifeTheme(
    colorTheme = ForkLifeColorTheme.Orange,
    darkModePreference = DarkModePreference.System
) {
    // Content
}
```

### Navigation

Navigation is handled via Compose Navigation:
- Routes defined in `Screen.kt` sealed class
- Entry point determined by login state (Login or Home)
- Bottom navigation for Home, History, Profile
- Slide animations for screen transitions

### Data Persistence

**DataStore** (via `DataStoreManager`):
- User session management (username)
- Theme preference (Orange, Avocado, Cherry)
- Dark mode preference (System, Light, Dark)
- Flow-based API for Compose integration

### Key Features

**Barcode Scanning**:
- CameraX with AndroidView wrapper
- ML Kit Barcode Scanning for barcode detection
- Custom `BarcodeAnalyzer` utility for processing
- Yuka-style scanning frame overlay with corner brackets and hint text
- Bottom sheet product display with skeleton loading states
- Manual barcode entry option

**Product Information**:
- Product information with HorizontalPager tabs (Score, Characteristics, Ingredients, Environment)
- READ-only access to OpenFoodFacts database (no account required)

**User Features**:
- Login system with session persistence
- Statistics tracking and rankings
- Success/achievement system
- Theme customization with live preview

**Profile**:
- User info card
- Success/Rankings tabs with HorizontalPager
- Chip filters for ranking categories

### Dependencies & Libraries

**Core**:
- Kotlin 1.9.25
- Android SDK 34 (compileSdk 34, targetSdk 34, minSdk 21)
- Java 11 compatibility
- Compose enabled
- `kotlin-parcelize` for Parcelable support

**Compose Stack**:
- Compose BOM 2024.12.01
- Material 3
- Navigation Compose 2.8.5
- Lifecycle Runtime Compose 2.8.7
- Hilt Navigation Compose 1.2.0
- Lottie Compose 6.6.2

**Image Loading**:
- Coil Compose 2.7.0 (replaces Glide for Compose)

**Key Libraries**:
- Dagger Hilt 2.48 - Dependency injection
- CameraX 1.3.0 - Camera functionality
- ML Kit Barcode Scanning 17.2.0 - Barcode detection
- Retrofit 2.9.0 + Gson - Network calls
- DataStore 1.0.0 - Preferences storage
- Firebase (BOM 32.7.0) - Analytics and Messaging
- Accompanist Permissions 0.36.0 - Runtime permissions

### Common Patterns

**Composables**:
- Screen composables receive navigation callbacks as parameters
- ViewModels accessed via `hiltViewModel()`
- State hoisting for reusable components

**ViewModels**:
- Injected via Hilt (`@HiltViewModel`)
- Use LiveData (observed via `observeAsState`) or StateFlow (collected via `collectAsState`)
- Repository pattern for data access

**Resource Wrapper**:
- `utils/Resource.kt` - Sealed class for Success/Error states
- Used for wrapping API responses

## UI/UX Guidelines

### Text Contrast Requirements
**IMPORTANT: Always ensure proper text contrast for readability.**

- Never use white text on light backgrounds (e.g., light green, yellow, light orange)
- For grade badges (Nutri-Score, Eco-Score, NOVA):
  - Use **black text** on grades B (light green) and C (yellow)
  - Use **white text** on grades A (dark green), D (orange), E (red)
- For theme color checkmarks, use black on Avocado theme (lighter green)
- Test UI in both light and dark modes

### Missing Data Pattern
**Hide elements when data is unavailable instead of showing "N/A".**

- History screen badges: Only show Nutri-Score/Eco-Score badges if grade exists
- Score tab: Only show score cards when data is available; show empty state message if all missing
- Environment tab: Only show Eco-Score and Packaging cards when data exists
- Prefer `?.let { }` pattern to conditionally render UI elements

### Scanning UI
- Barcode scanner includes a Yuka-style overlay with:
  - Semi-transparent dark background
  - Transparent scanning window (75% width, barcode aspect ratio)
  - Rounded corner brackets in primary theme color
  - Hint text: "Placez le code-barres dans le cadre"

## Important Notes

- The app requires camera permissions for scanning (handled via Accompanist Permissions)
- Firebase services require `google-services.json` (not in repo)
- OpenFoodFacts API is READ-only - no account or authentication required for product data
- AGP 8.10.1 used with Compose enabled
- KAPT (used by Hilt) requires JVM arguments in `gradle.properties`
- Gradle build cache is disabled to prevent cache corruption issues
