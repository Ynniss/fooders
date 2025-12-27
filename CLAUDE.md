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

### Dependency Injection (Dagger Hilt)

The app uses Dagger Hilt for dependency injection:
- `FoodersApplication` is annotated with `@HiltAndroidApp`
- All injectable activities/fragments use `@AndroidEntryPoint`
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

### Presentation Layer (MVVM)

The app follows MVVM architecture with Android Architecture Components:

**UI Structure** (`ui/` package):
- `login/` - User authentication
- `main/` - Main activity container with bottom navigation
- `home/` - Home screen
- `scan/` - Barcode scanning with CameraX and ML Kit
  - `viewpager/` - Product details tabs (Score, Characteristics, Ingredients, Environment)
- `profile/` - User profile
  - `viewpager/` - Profile tabs (Success achievements, Rankings)
- `history/` - Scan history
- `editproduct/` - Product editing interface
- `photo/` - Photo capture and cropping (MVP pattern for this module)
- `settings/` - App settings

**App Bar Pattern**:
- The app uses a centralized toolbar in `MainActivity` (`activity_main.xml`)
- Toolbar height: `?attr/actionBarSize` (standard 56dp)
- Toolbar styling: Clean Material Design with solid color background themed by user preference (Orange/Avocado/Cherry)
- Elevation: 4dp (standard Material Design for scrolled app bars)
- The toolbar is hidden for `loginFragment` and `scanFragment` via `hideActionBarForSpecificScreens()`
- Fragments that need content below the toolbar should use `@dimen/content_padding_below_toolbar` (56dp) for consistent top spacing
- This matches the toolbar height exactly for a clean, modern look
- Example usage in `fragment_home.xml`, `fragment_history.xml`, and `fragment_profile.xml`

### Navigation

Navigation is handled via Android Navigation Component with Safe Args:
- Main navigation graph: `res/navigation/navigation_graph.xml`
- Entry point: `LoginFragment`
- Main screens accessible via bottom navigation: Home, History, Profile
- Navigation uses custom animations (from_right, to_left, from_left, to_right)

### Data Persistence

**DataStore** (via `DataStoreManager`):
- User session management (username)
- Theme preference (Orange, Avocado, Cherry)
- Async operations using Kotlin Coroutines Flow

### Key Features

**Barcode Scanning**:
- Uses CameraX for camera preview
- ML Kit Barcode Scanning for barcode detection
- Custom `BarcodeAnalyzer` utility for processing
- Manual barcode entry option via `ManualScanFragment`

**Product Management**:
- View product information with tabbed interface (ViewPager2)
- Edit product details and images
- Integration with OpenFoodFacts for data submission
- Image cropping using Android-Image-Cropper library

**User Features**:
- Login system with session persistence
- Statistics tracking and rankings
- Success/achievement system
- Theme customization (Orange, Avocado, Cherry)
- Firebase Cloud Messaging for push notifications

**Theme System**:
- Dynamic theme switching without restart
- Themes stored in DataStore
- MainActivity recreates on theme change (detected in `onResume`)

### Dependencies & Libraries

**Core**:
- Kotlin 1.9.0
- Android SDK 34 (compileSdk 34, targetSdk 34, minSdk 21)
- Java 11 compatibility
- ViewBinding enabled
- `kotlin-parcelize` for Parcelable support

**Key Libraries**:
- Dagger Hilt 2.48 - Dependency injection
- Navigation Component 2.7.4 - Navigation with Safe Args
- CameraX 1.3.0 - Camera functionality (stable)
- ML Kit Barcode Scanning 17.2.0 - Barcode detection
- Retrofit 2.9.0 + Gson - Network calls
- DataStore 1.0.0 - Preferences storage (stable)
- Glide 4.16.0 - Image loading
- Lottie 6.1.0 - Animations
- Firebase (BOM 32.7.0) - Analytics and Messaging
- Android-Image-Cropper 4.3.2 - Image cropping

### Common Patterns

**ViewModels**:
- Injected via Hilt (`@HiltViewModel`)
- Use LiveData for UI state
- Repository pattern for data access

**Resource Wrapper**:
- `utils/Resource.kt` - Sealed class for Success/Error states
- Used for wrapping API responses

**Coroutines**:
- Repositories use `suspend` functions
- ViewModels launch coroutines in `viewModelScope`
- Activities/Fragments use `lifecycleScope`

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

- The app requires camera permissions for scanning
- Storage permissions are declared for image handling
- Firebase services require `google-services.json` (not in repo, provided via CI secrets)
- The photo module uses MVP pattern while the rest uses MVVM
- AGP 8.1.4+ requires namespace declaration in build.gradle instead of package in AndroidManifest
- Activities with intent filters must declare android:exported="true" for Android 12+ (SDK 31+)
- KAPT (used by Hilt) requires JVM arguments in `gradle.properties` to access JDK compiler modules in Java 11+ (`--add-opens` flags are set for both `org.gradle.jvmargs` and `kotlin.daemon.jvmargs`)
- Gradle build cache is disabled (`org.gradle.caching=false`) to prevent JDK image transformation cache corruption issues with AGP 8.1.4 + Gradle 8.12
- Heap sizes: Gradle daemon 4GB, Kotlin daemon 2GB
