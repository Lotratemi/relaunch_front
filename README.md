# Relaunch — Frontend

Mobile coaching app built with Kotlin Multiplatform, targeting Android and iOS.

Backend repo: [github.com/Lotratemi/relaunch_back](https://github.com/Lotratemi/relaunch_back)

---

## Prerequisites

- Android Studio Panda 2 (2025.3.2) or higher, with the Kotlin Multiplatform Mobile plugin enabled
- JDK 21 — provided automatically by the Gradle wrapper, no manual installation needed
- Git
- *(iOS only)* a Mac with Xcode installed
- Access to the GitHub repository (ask the tech lead)

---

## Installation

```bash
# Clone the repository
git clone https://github.com/Lotratemi/relaunch_front.git
cd relaunch_front

# Open in Android Studio
# File > Open > select the relaunch_front folder
# Wait for the Gradle sync to complete
```

---

## Backend URL

The backend URL is configured in `ApiClient.kt`:

```kotlin
object ApiClient {
    var baseUrl: String = "http://10.0.2.2:8080" // Android emulator
    // var baseUrl: String = "http://localhost:8080" // iOS simulator
}
```

> `10.0.2.2` maps to `localhost` from inside an Android emulator.  
> Switch to your machine's local IP when testing on a physical device.

The HTTP client is configured in `composeApp/src/commonMain/kotlin/data/ApiClient.kt`.  
All API calls go through `ApiService`, which is instantiated there.

No API keys are stored in the frontend — they are all managed on the backend side.

> Authentication is not yet implemented. No token is sent with requests for now.  
> This will be addressed in a future sprint.

---

## Running the project

The backend must be running before launching the frontend.  
See the [backend repo](https://github.com/Lotratemi/relaunch_back) for setup instructions.

### Android

From Android Studio — select the `androidApp` run configuration then press Run.

From the terminal:

```bash
.\gradlew.bat :composeApp:assembleDebug   # Windows
./gradlew :composeApp:assembleDebug       # macOS / Linux
```

### iOS (Mac only)

From Android Studio — select the `iosApp` configuration and an iOS simulator then press Run.

Alternatively, open the `/iosApp` folder in Xcode and run from there.  
iOS-specific SwiftUI code also goes in that folder.

---

## Screens

The app navigation is managed as a state machine in `App.kt`. The screens in order are:

1. **Onboarding** — user creation
2. **Profiling invite** — option to start profiling or skip to coaching
3. **Profiling** — AI-driven profiling session
4. **Profiling result** — summary of the profiling
5. **Coach** — main coaching chat with the AI
6. **Dashboard** — progress overview

---

## Conventions

Branches:

| Branch | Purpose |
|---|---|
| `main` | Stable code — never push directly |
| `develop` | Integration branch |
| `feat/feature-name` | New feature |
| `fix/bug-name` | Bug fix |

Commit messages follow the [Conventional Commits](https://www.conventionalcommits.org) format:

```
feat: add dashboard screen
fix: fix crash on profiling result when result is null
docs: update README
```

Every PR to `develop` requires approval from at least one other team member.

---

*Keep this file up to date with every major technical change.*
