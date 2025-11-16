# ToPWR WearOS Companion App

A native WearOS companion app for ToPWR that displays real-time parking availability.

## Features

- **Real-time Parking Data**: Fetches live parking availability from the ToPWR API
- **Native WearOS UI**: Built with Jetpack Compose for Wear OS
- **Material 3 Expressive Tiles**: Quick-access tiles with beautiful, dynamic design
  - Shows top 3 parkings with most available spaces
  - Color-coded cards (green/orange/red) based on availability
  - Bold, expressive typography
  - Tap to open full app
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

## Using Tiles

### Adding the Parking Tile to Your Watch

1. **Open the Tile Carousel**: On your WearOS watch, swipe left/right from the watch face
2. **Add a New Tile**: Scroll to the end and tap the "+" button
3. **Select ToPWR Parkings**: Find and tap "Parkings" in the list
4. **View Live Data**: The tile will now show real-time parking availability

### Tile Features

- **Top 3 Parkings**: Shows the 3 parkings with the most available spaces
- **Color-Coded Cards**:
  - 🟢 **Green**: Plenty of spaces (>50% available)
  - 🟠 **Orange**: Some spaces (20-50% available)
  - 🔴 **Red**: Limited spaces (<20% available)
  - ⚫ **Grey**: No spaces available
- **Expressive Design**: Bold numbers, modern typography, smooth rounded corners
- **Tap to Open**: Tap anywhere on the tile to open the full app
- **Auto-Refresh**: Updates automatically when you view the tile

### Material 3 Expressive Design

The tile uses Material 3's expressive design language with:
- **Dynamic colors** that adapt to parking availability
- **Bold typography** (up to 900 weight) for emphasis
- **Large numbers** (28dp) for at-a-glance readability
- **Smooth corners** (20-28dp radius) for a modern look
- **Proper spacing** and visual hierarchy

## Project Structure

```
android/wear/
├── src/main/
│   ├── java/com/solvro/topwr/wear/
│   │   ├── MainActivity.kt                # Main Compose UI
│   │   ├── api/
│   │   │   └── ParkingApiService.kt       # API client
│   │   ├── model/
│   │   │   └── Parking.kt                 # Data models
│   │   └── tile/
│   │       ├── ParkingTileService.kt      # Tile service
│   │       ├── ParkingTileRenderer.kt     # Material 3 tile UI
│   │       └── ParkingTileState.kt        # Tile state management
│   ├── res/
│   │   └── values/
│   │       └── strings.xml                # String resources
│   └── AndroidManifest.xml
├── build.gradle                            # Module build configuration
├── proguard-rules.pro
└── README.md
```

## Dependencies

- **Jetpack Compose for Wear OS**: Modern UI toolkit
- **Wear Tiles with Material 3**: Quick-access tiles with expressive design
- **Horologist**: Google's library for WearOS best practices
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

You can extend this implementation with:

1. **Favorites**: Save favorite parking spots
2. **Notifications**: Alert when spaces become available
3. **Map Integration**: Show parking locations on a map
4. **Complications**: Add watch face complications showing parking data
5. **Data Sync**: Share data between phone and watch via Data Layer API
6. **Offline Support**: Cache parking data locally with Room database
7. **More API Endpoints**: Add news, calendar, or other features from the main app
8. **Advanced Tiles**: Add multiple tile layouts or customization options

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
