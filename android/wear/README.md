# ToPWR WearOS Companion App

A native WearOS companion app for ToPWR that displays real-time parking availability.

## Features

- **Real-time Parking Data**: Fetches live parking availability from the ToPWR API
- **Native WearOS UI**: Built with Jetpack Compose for Wear OS
- **Standalone App**: Works independently without requiring the phone app

## Setup Instructions

### 1. Configure API URL

Before building the app, you need to configure the parking API URL:

1. Open `android/wear/src/main/java/com/solvro/topwr/wear/api/ParkingApiService.kt`
2. Find the line with `YOUR_PARKING_API_URL_HERE`
3. Replace it with the actual parking API URL from your `.env` file
   - The URL is stored in `lib/config/env.dart` as `parkingApiUrl`
   - Example: `"https://api.example.com/parkings"`

```kotlin
// Before
val url = "YOUR_PARKING_API_URL_HERE"

// After
val url = "https://your-actual-api-url.com/parkings"
```

### 2. Build the WearOS App

#### Using Android Studio:

1. Open the project in Android Studio
2. Select the `wear` module from the configuration dropdown
3. Connect a WearOS device or start a WearOS emulator
4. Click "Run" or press Shift+F10

#### Using Command Line:

```bash
cd android
./gradlew :wear:assembleDebug
```

The APK will be generated at: `android/wear/build/outputs/apk/debug/wear-debug.apk`

### 3. Install on WearOS Device

#### Via Android Studio:
- Select your WearOS device from the device dropdown
- Click "Run"

#### Via ADB:
```bash
adb install android/wear/build/outputs/apk/debug/wear-debug.apk
```

#### Via Wireless Debugging:
1. Enable Developer Options on your WearOS watch
2. Enable ADB debugging and Wireless debugging
3. Connect via `adb connect <watch-ip>:5555`
4. Install the APK

## Project Structure

```
android/wear/
├── src/main/
│   ├── java/com/solvro/topwr/wear/
│   │   ├── MainActivity.kt           # Main Compose UI
│   │   ├── api/
│   │   │   └── ParkingApiService.kt  # API client
│   │   └── model/
│   │       └── Parking.kt            # Data models
│   ├── res/
│   │   └── values/
│   │       └── strings.xml           # String resources
│   └── AndroidManifest.xml
├── build.gradle                       # Module build configuration
├── proguard-rules.pro
└── README.md
```

## Dependencies

- **Jetpack Compose for Wear OS**: Modern UI toolkit
- **OkHttp**: HTTP client for API calls
- **Kotlinx Serialization**: JSON parsing
- **Coroutines**: Async operations

## API Integration

The app connects to the same parking API used by the main Flutter app:

### Endpoint
- **URL**: Configured in `ParkingApiService.kt`
- **Method**: GET
- **Response Format**:
```json
{
  "places": [
    {
      "id": "1",
      "parking_id": "P1",
      "liczba_miejsc": "25",
      "symbol": "A1",
      "nazwa": "Parking A",
      "places": "100",
      "address": "Wrocław, ul. Example 1",
      "geo_lat": "51.1079",
      "geo_lan": "17.0385",
      "trend": "0",
      "aktywny": "1",
      "open_hour": "06:00:00",
      "close_hour": "22:00:00"
    }
  ]
}
```

### Key Fields
- `liczba_miejsc`: Available parking spaces
- `places`: Total parking spaces
- `nazwa`: Parking name
- `address`: Location address
- `trend`: Availability trend (0=steady, 1=increasing, -1=decreasing)

## Development Notes

### Requirements
- **Minimum SDK**: 30 (WearOS 3.0+)
- **Target SDK**: 35
- **Kotlin**: 2.2.0
- **Compose Compiler**: 1.5.15

### Future Enhancements

This is a minimal base implementation. You can extend it with:

1. **Favorites**: Save favorite parking spots
2. **Notifications**: Alert when spaces become available
3. **Map Integration**: Show parking locations on a map
4. **Complications**: Add watch face complications
5. **Tiles**: Quick access tiles for parking data
6. **Data Sync**: Share data between phone and watch
7. **Offline Support**: Cache parking data locally
8. **More API Endpoints**: Add news, calendar, or other features

### Adding New Features

To add support for other APIs from the main app:

1. Create a new data model in `model/` package
2. Add a new API service in `api/` package
3. Create a new Compose screen
4. Update navigation in `MainActivity.kt`

### Testing

The app can be tested on:
- **Physical WearOS device** (recommended)
- **WearOS emulator** in Android Studio
- Use Android Studio's "Running Devices" to test UI

## Troubleshooting

### App doesn't connect to API
- Verify the API URL is correct in `ParkingApiService.kt`
- Ensure the watch has internet connectivity
- Check if the API requires authentication

### Build errors
- Sync Gradle files: File → Sync Project with Gradle Files
- Clean build: `./gradlew clean`
- Check that Kotlin and Compose versions match

### App not appearing on watch
- Verify WearOS device is connected: `adb devices`
- Check that the app installed successfully
- Look for the app in the watch's app drawer

## License

Part of the ToPWR project.
