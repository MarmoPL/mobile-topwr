# Android Studio Configuration Guide

## Setting Up the WearOS Module in Android Studio

### 1. Open the Project

1. **Launch Android Studio**
2. **Open the project**:
   - Click `File` → `Open`
   - Navigate to `/home/user/mobile-topwr/android`
   - Click `OK`

3. **Wait for Gradle sync**:
   - Android Studio will automatically detect the `wear` module
   - Wait for "Gradle sync finished" in the status bar
   - If sync fails, click `File` → `Sync Project with Gradle Files`

### 2. Verify Module Configuration

1. **Check Project Structure**:
   - Open `File` → `Project Structure` (or press `Ctrl+Alt+Shift+S`)
   - Under `Modules`, you should see:
     - `mobile-topwr.android.app` (main Flutter app)
     - `mobile-topwr.android.wear` (your new WearOS module)

2. **Verify settings.gradle**:
   - Should show:
     ```gradle
     include ":app"
     include ":wear"
     ```

### 3. Configure the API URL

1. **Open ParkingApiService.kt**:
   - Navigate to: `android/wear/src/main/java/com/solvro/topwr/wear/api/ParkingApiService.kt`
   - Find line ~26

2. **Replace the placeholder URL**:
   ```kotlin
   // Line 26 - BEFORE
   val url = "YOUR_PARKING_API_URL_HERE"

   // AFTER (get this from your .env file)
   val url = "https://your-actual-parking-api.com/endpoint"
   ```

3. **To find your API URL**:
   - Open `lib/config/env.dart` in the Flutter project
   - Look for `parkingApiUrl`
   - Copy that URL value

### 4. Set Up WearOS Emulator (Recommended for Development)

#### Option A: Create a New WearOS Emulator

1. **Open Device Manager**:
   - Click the phone icon in the toolbar, or
   - `Tools` → `Device Manager`

2. **Create Virtual Device**:
   - Click `Create Device`
   - Select **Wear OS** tab
   - Choose a device (recommended: **Wear OS Large Round** or **Wear OS Small Round**)
   - Click `Next`

3. **Select System Image**:
   - Choose API level **30 or higher** (WearOS 3.0+)
   - Recommended: **API 33** (WearOS 4.0)
   - Click `Download` if not already installed
   - Click `Next`

4. **Configure AVD**:
   - Name it (e.g., "WearOS_API33")
   - Click `Finish`

#### Option B: Use Physical WearOS Device

1. **Enable Developer Options on Watch**:
   - Go to `Settings` → `System` → `About`
   - Tap `Build number` 7 times
   - Developer options enabled!

2. **Enable ADB Debugging**:
   - Go to `Settings` → `Developer options`
   - Enable `ADB debugging`
   - Enable `Debug over Wi-Fi` (for wireless)

3. **Connect via USB** (if watch supports it):
   - Connect watch to computer via USB
   - Accept debugging prompt on watch
   - Watch should appear in Android Studio's device dropdown

4. **Connect via Wi-Fi** (most common):
   - On watch: Note the IP address shown in `Debug over Wi-Fi`
   - On computer, open Terminal in Android Studio:
     ```bash
     adb connect <WATCH_IP>:5555
     ```
   - Example: `adb connect 192.168.1.100:5555`
   - Watch should now appear in device dropdown

### 5. Select Run Configuration

1. **Open Run Configuration Dropdown**:
   - Top toolbar, next to the Run button
   - Click the dropdown (shows current configuration)

2. **Edit Configurations**:
   - Click `Edit Configurations...`
   - Click `+` (Add New Configuration)
   - Select `Android App`

3. **Configure Wear Module**:
   - **Name**: `wear` (or "ToPWR WearOS")
   - **Module**: Select `mobile-topwr.android.wear`
   - **Installation option**: Default APK
   - Click `OK`

4. **Alternative (Simpler)**:
   - Just select `wear` from the module dropdown if it appears automatically

### 6. Build and Run

1. **Select the wear configuration** from the dropdown
2. **Select your WearOS device/emulator** from the device dropdown
3. **Click the Run button** (green play icon) or press `Shift+F10`

4. **First build will**:
   - Download dependencies (Tiles, Horologist, etc.)
   - Compile Kotlin code
   - Build APK
   - Install on watch

5. **Monitor build progress**:
   - Check `Build` tab at the bottom
   - Watch for any errors

### 7. Testing the Tiles

1. **After app installs**:
   - The app should launch automatically on the watch

2. **Add the Tile**:
   - On watch face, swipe left/right
   - Scroll to the end, tap `+`
   - Find "Parkings" in the list
   - Tap to add

3. **View Tile**:
   - Swipe to see your new tile
   - Should show parking data with colors

### 8. Debugging

#### Enable Logcat for WearOS

1. **Open Logcat**:
   - `View` → `Tool Windows` → `Logcat`

2. **Filter by package**:
   - In filter box, type: `package:com.solvro.topwr.wear`

3. **Common log tags to watch**:
   - `ParkingTileService`
   - `ParkingApiService`
   - `System.err` (for errors)

#### Debug the Tile Service

1. **Set breakpoints** in:
   - `ParkingTileService.kt` (line where API is called)
   - `ParkingTileRenderer.kt` (rendering logic)

2. **Attach debugger**:
   - Click the bug icon instead of run
   - Trigger tile refresh on watch
   - Debugger should stop at breakpoints

### 9. Common Issues & Solutions

#### Issue: "Module not found"
**Solution**:
- Sync Gradle: `File` → `Sync Project with Gradle Files`
- Invalidate caches: `File` → `Invalidate Caches` → `Invalidate and Restart`

#### Issue: "Cannot resolve symbol Material3"
**Solution**:
- Check internet connection
- Sync Gradle again
- Check `build.gradle` has all dependencies

#### Issue: "Device not detected"
**Solution**:
- For emulator: Restart the emulator
- For physical: Run `adb devices` in terminal to verify
- Try: `adb kill-server` then `adb start-server`

#### Issue: "Tile not showing up"
**Solution**:
- Uninstall and reinstall the app
- Check AndroidManifest.xml has tile service registered
- Check Logcat for tile errors

#### Issue: "API returns error"
**Solution**:
- Verify API URL is correct in `ParkingApiService.kt`
- Check internet connection on watch
- Test API URL in browser first
- Check Logcat for network errors

### 10. Tips for Development

1. **Hot Reload doesn't work**: WearOS apps require full rebuild for changes

2. **Speed up builds**:
   - Use `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)` for faster iterations
   - Enable `File` → `Settings` → `Build, Execution, Deployment` → `Compiler` → `Compile independent modules in parallel`

3. **Preview Tiles**:
   - You can preview tiles in Android Studio using the `@Preview` annotation
   - Check `ParkingTileRenderer.kt` bottom for preview

4. **Test on emulator first**:
   - Faster iteration
   - Easier debugging
   - Then test on real device for final validation

5. **Use Android Studio's profiler**:
   - `View` → `Tool Windows` → `Profiler`
   - Monitor memory, CPU, network usage

### 11. Project Structure in Android Studio

```
Project View (Android):
├── app (Flutter mobile app)
└── wear (WearOS module)
    ├── manifests
    │   └── AndroidManifest.xml
    ├── java
    │   └── com.solvro.topwr.wear
    │       ├── MainActivity
    │       ├── api
    │       │   └── ParkingApiService
    │       ├── model
    │       │   └── Parking
    │       └── tile
    │           ├── ParkingTileService
    │           ├── ParkingTileRenderer
    │           └── ParkingTileState
    └── res
        ├── drawable
        ├── mipmap
        └── values
            └── strings.xml
```

### 12. Keyboard Shortcuts (Useful)

- `Ctrl+F9`: Build project
- `Shift+F10`: Run app
- `Shift+F9`: Debug app
- `Ctrl+N`: Find class
- `Ctrl+Shift+N`: Find file
- `Alt+Enter`: Quick fix
- `Ctrl+Alt+L`: Format code
- `Ctrl+/`: Comment/uncomment line

## Next Steps

After successful setup:

1. **Customize the tile** with your branding
2. **Add more features** (favorites, notifications)
3. **Test on multiple WearOS versions**
4. **Optimize performance** using Profiler
5. **Add more API endpoints** from the main app

## Getting Help

- **Android Studio issues**: Check `Help` → `Show Log in Files`
- **Build errors**: Read full stack trace in Build output
- **Runtime errors**: Check Logcat with package filter
- **WearOS docs**: https://developer.android.com/training/wearables

---

**Quick Start Checklist:**
- [ ] Project opened in Android Studio
- [ ] Gradle sync completed
- [ ] API URL configured in ParkingApiService.kt
- [ ] WearOS emulator/device set up
- [ ] Run configuration created for `wear` module
- [ ] App successfully installed on watch
- [ ] Tile added to watch face
- [ ] Parking data displaying correctly
