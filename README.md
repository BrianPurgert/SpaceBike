# Space Bike

Space Bike is a side-scrolling physics-based Android game where you pilot a space bike through obstacle-filled levels. Tilt your device or use on-screen controls to lean the bike, and tap the screen to drive forward or reverse. Beat each level before your battery runs out!

## Gameplay

- **Objective:** Reach the end of each level without crashing before the timer expires.
- **Controls:**
  - *Tilt controls* — tilt your device left/right to lean the bike.
  - *Button controls* — tap the right side of the screen to drive forward, the left side to reverse, and the center to brake.
  - *Slider bar* — an alternative touch layout where a horizontal strip controls both steering and throttle.
- **Gravity panel** — in later levels you can rotate gravity in any direction (up, down, left, right) or disable it entirely. Tap the directional arrows in the top-left corner of the screen.
- **Battery meter** — a countdown bar at the top of the screen represents your remaining power. When it reaches zero the bike shuts down.
- **Level progression** — complete a level to unlock the next one. Progress is saved automatically.

## Levels

| # | Name | Time Limit |
|---|------|-----------|
| 1 | Tutorial | 4 min 10 s |
| 2 | Level 1 | 1 min |
| 3 | Level 2 | 2 min 30 s |
| 4 | Level 3 | 2 min 30 s |

## Settings

| Option | Values |
|--------|--------|
| Graphics detail | Low / Medium / High |
| Tilt sensitivity | Slow / Normal / Fast |
| Feedback | Vibrate / Sound |
| Control layout | Normal buttons / Slider bar |

## Project Structure

```
src/main/
├── AndroidManifest.xml
├── assets/
│   ├── fonts/               # Custom fonts (space.ttf, sketch.ttf, Brushed.ttf)
│   └── doc/                 # Emini Physics Engine API documentation
├── java/com/doodle/physics2d/
│   ├── full/spacebike/      # Game logic
│   │   ├── DoodleBikeMain   # Main game Activity (physics loop, input, UI)
│   │   ├── Level            # Level-select and settings Activity
│   │   ├── LevelControl     # Level configuration (world files, gravity, timers)
│   │   ├── GameEvents       # Physics event handler (collisions, win/death)
│   │   ├── SoundManager     # Sound pool wrapper
│   │   └── MyCount          # (stub / unused)
│   └── graphics/            # Rendering layer
│       ├── GraphicsWorld    # Physics world + canvas drawing + sensor input
│       ├── SimulationView   # SurfaceView that hosts the game canvas
│       └── UserImages       # Bitmap loader for bike body and wheels
└── res/
    ├── layout/              # XML layouts (rotategame, level2, level3, …)
    ├── drawable/            # UI assets and shape drawables
    ├── raw/                 # Physics world files (.phy) and audio
    └── values/              # strings.xml, general.xml
```

## Technology

- **Language:** Java
- **Platform:** Android (minSdk 7, targetSdk 20)
- **Physics:** [Emini Physics Engine v1.3.4](http://www.emini.at) by Alexander Adensamer — a 2D fixed-point rigid-body physics library
- **Sensor:** `SensorManager.SENSOR_ACCELEROMETER` for tilt controls
- **Audio:** `SoundPool` for sound effects; `MediaPlayer` for music (imported but managed via `SoundManager`)

## Building an APK

### Prerequisites

- **Java Development Kit (JDK) 11 or 17** — required by the Android Gradle plugin.
- **Android SDK** — install via [Android Studio](https://developer.android.com/studio) or the standalone [command-line tools](https://developer.android.com/studio#command-line-tools-only). Make sure `ANDROID_HOME` (or `ANDROID_SDK_ROOT`) is set.
- **Android SDK Platform 34** — install through Android Studio's SDK Manager or with:
  ```bash
  sdkmanager "platforms;android-34" "build-tools;34.0.0"
  ```

### Build with the Gradle wrapper

If you have the [Gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) installed (or generate it with `gradle wrapper`), you can build from the command line:

```bash
# Debug APK
./gradlew assembleDebug

# The APK will be at:
# build/outputs/apk/debug/SpaceBike-debug.apk
```

To create a **release APK** you must sign it. First create a keystore, then build:

```bash
# Generate a keystore (one-time step)
keytool -genkey -v -keystore my-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 -alias spacebike

# Build the release APK
./gradlew assembleRelease

# Sign the APK
apksigner sign --ks my-release-key.jks \
  build/outputs/apk/release/SpaceBike-release-unsigned.apk
```

### Build with Android Studio

1. Open the project root folder in **Android Studio**.
2. Wait for Gradle sync to complete.
3. Select **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
4. The generated APK will be in `build/outputs/apk/`.

### Install and run

```bash
# Install the debug APK on a connected device or emulator
adb install build/outputs/apk/debug/SpaceBike-debug.apk
```

## Credits

- **Game development:** Brian Purgert
- **Physics engine:** Alexander Adensamer — [Emini Physics](http://www.emini.at)

## License

Copyright © 2010 Brian Purgert. All rights reserved.  
Physics engine copyright © 2010 Alexander Adensamer (Emini Physics). All rights reserved.
