# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Fooders is an Android application for scanning and managing food product information. The app integrates with OpenFoodFacts API for product data and uses a custom backend API (Firebase Functions) for user management, statistics, and rankings.

**Package name**: `com.esgi.fooders`
**Application ID**: `com.esgi_project.fooders`

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

**Note:** The project requires `google-services.json` (Firebase configuration) to build successfully. This file is not in the repository and must be provided via CI/CD secrets or placed in `app/google-services.json` for local development.

## Fastlane Distribution

The project uses Fastlane for Firebase App Distribution:

```bash
# Distribute app via Firebase App Distribution
bundle exec fastlane distribute
```

This requires:
- `firebase_credentials.json` (service account credentials)
- `FIREBASE_APP_ID` environment variable
- Release notes in `FirebaseAppDistributionConfig/release_notes.txt`
- Groups configuration in `FirebaseAppDistributionConfig/groups.txt`

## Architecture

### UI Architecture: Jetpack Compose + Material 3

The app uses a **single-activity, multiple-composables architecture** with Jetpack Compose and Material 3:

- **Single Activity**: `MainActivity` hosts the entire app using `setContent { FoodersApp() }`
- **Compose Navigation**: Type-safe navigation with `androidx.navigation:navigation-compose`
- **Material 3**: Full Material You design system with dynamic theming

### Dependency Injection (Dagger Hilt)

The app uses Dagger Hilt for dependency injection:
- `FoodersApplication` is annotated with `@HiltAndroidApp`
- `MainActivity` uses `@AndroidEntryPoint`
- ViewModels use `@HiltViewModel` and are accessed via `hiltViewModel()` in Composables
- DI configuration in `di/AppModule.kt`

Key provided dependencies:
- `FoodersApi` (Retrofit instance with 60s timeouts)
- `LoginRepository`
- `ScanRepository`
- `DataStoreManager` (injected via constructor)

### Data Layer

**API Integration**:
- **Custom Backend**: `https://us-central1-fooders-811cb.cloudfunctions.net/app/fooders/api/`
  - User login, statistics, rankings, and success tracking
- **OpenFoodFacts API**: `https://world.openfoodfacts.org/`
  - Product information retrieval
  - Product image and information modifications

**API Interface**: `data/remote/FoodersApi.kt`

**Repositories**:
- `LoginRepository`: Handles user authentication
- `ScanRepository`: Manages product scanning and information retrieval

**Responses**: Located in `data/remote/responses/` with subdirectories for different response types (ProductInformations, ImageModificationResponse, UserSuccessResponse, RankingResponse)

### Presentation Layer

**UI Structure** (`ui/` package):

```
ui/
├── FoodersApp.kt              # Main app composable with navigation
├── theme/
│   ├── Color.kt              # Color definitions for all themes
│   ├── Type.kt               # Material 3 typography
│   ├── Shape.kt              # Material 3 shapes
│   └── Theme.kt              # FoodersTheme composable
├── navigation/
│   ├── Screen.kt             # Navigation routes
│   └── FoodersNavHost.kt     # Navigation graph
├── components/
│   ├── FoodersTopAppBar.kt   # App bar variants
│   ├── FoodersBottomNavigation.kt
│   ├── FoodersCard.kt        # Card components
│   ├── FoodersButton.kt      # Button variants
│   ├── FoodersTextField.kt   # Text input components
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
├── editproduct/
│   ├── EditProductScreen.kt
│   └── EditProductViewModel.kt
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

Theme is applied via `FoodersTheme` composable:
```kotlin
FoodersTheme(
    colorTheme = FoodersColorTheme.Orange,
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
- Bottom sheet product display
- Manual barcode entry option

**Product Management**:
- Product information with HorizontalPager tabs (Score, Characteristics, Ingredients, Environment)
- Edit product details
- Integration with OpenFoodFacts for data submission

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

## CI/CD

GitHub Actions workflow (`.github/workflows/distribute.yml`) automatically:
1. Triggers on push to `main` branch
2. Sets up Ruby 2.6 and Bundler
3. Decodes Firebase credentials and Google Services JSON from secrets
4. Runs Fastlane distribution

Required secrets:
- `FIREBASE_CREDENTIALS` (base64 encoded)
- `GOOGLE_SERVICES` (base64 encoded)
- `FIREBASE_APP_ID`

## Important Notes

- The app requires camera permissions for scanning (handled via Accompanist Permissions)
- Firebase services require `google-services.json` (not in repo, provided via CI secrets)
- Old fragment-based code is deprecated but still present (will be removed in cleanup)
- AGP 8.10.1 used with Compose enabled
- KAPT (used by Hilt) requires JVM arguments in `gradle.properties`
- Gradle build cache is disabled to prevent cache corruption issues
