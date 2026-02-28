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

## Building

This project uses a legacy Android source layout (`src/main/`) without Gradle or Maven build files. To build it:

1. Import the project into **Android Studio** (or IntelliJ IDEA).
2. Add the physics engine JARs from `src/main/assets/` (`PhysicsEngine_v134.jar`) to the module's compile classpath.
3. Build and run on a device or emulator running Android 2.1 (API 7) or later.

## Credits

- **Game development:** Brian Purgert
- **Physics engine:** Alexander Adensamer — [Emini Physics](http://www.emini.at)

## License

Copyright © 2010 Brian Purgert. All rights reserved.  
Physics engine copyright © 2010 Alexander Adensamer (Emini Physics). All rights reserved.
