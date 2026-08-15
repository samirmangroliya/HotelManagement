# HotelManagement

HotelManagement is a full-stack Kotlin Multiplatform (KMP) project that provides a comprehensive solution for hotel booking and management. It targets multiple platforms including Android, iOS, Web, Desktop (JVM), and a Backend Server using Ktor.

## Project Overview

This project demonstrates the power of sharing code across different platforms while maintaining the flexibility to implement platform-specific features when needed.

### Key Modules

*   **[:composeApp](./composeApp)**: The primary module for UI code shared across Android, iOS, Desktop, and Web using Compose Multiplatform.
*   **[:server](./server)**: A Ktor-based backend application that handles API requests, user authentication, and database persistence.
*   **[:network](./network)**: A shared networking layer containing the Ktor HTTP client and `ApiService` for communicating with the backend.
*   **[:core](./core)**: Shared data models (e.g., `Hotel`, `Room`, `User`, `Booking`) used by both client and server.
*   **[:di](./di)**: Centralized dependency injection configuration using Koin.

### Key Features

*   **User Authentication**: Secure login and registration flows.
*   **Hotel Discovery**: Browse a catalog of hotels and view detailed information.
*   **Room Management**: Explore available rooms within a hotel.
*   **Booking System**: Create and manage hotel bookings seamlessly across platforms.

## Configuration: Server URL & IP Address

To ensure the client applications can connect to the server, you may need to configure the server's IP address and port.

### 1. Client-Side (Base URL)
The client applications use a central constant for the API base URL. Update this to match your server's IP address:
*   **File**: `network/src/commonMain/kotlin/com/samir/network/Constants.kt`
*   **Constant**: `BASE_URL` here you can use your own IP address to run app in android emulator
*   *Note: Use `http://10.0.2.2:8081/` for Android Emulators to reach a server running on your local machine.*

### 2. Server-Side (Host & Port)
The server's running configuration can be adjusted here:
*   **File**: `server/src/main/kotlin/com/samir/hotelmanagement/Constants.kt`
*   **Constants**: `SERVER_HOST` and `SERVER_PORT`

---

## Getting Started

### Build and Run Server

To start the backend server:
- **macOS/Linux**: `./gradlew :server:run`
- **Windows**: `.\gradlew.bat :server:run`

### Build and Run Android Application

- **macOS/Linux**: `./gradlew :composeApp:assembleDebug`
- **Windows**: `.\gradlew.bat :composeApp:assembleDebug`

### Build and Run Desktop (JVM) Application

- **macOS/Linux**: `./gradlew :composeApp:run`
- **Windows**: `.\gradlew.bat :composeApp:run`

### Build and Run Web Application (Wasm)

- **macOS/Linux**: `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`
- **Windows**: `.\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun`

### Build and Run iOS Application

Open the `iosApp` directory in Xcode or use the run configuration in Android Studio.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html) and [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/).
