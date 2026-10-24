# Typing Speed Test and Performance Analyzer

**College Java OOP Project — Modern JavaFX Desktop UI & Analytics Studio**

Built for Member 1's module: JavaFX Application Entry Point, Main Dashboard, Dynamic Navigation, Design System, Reusable Components, and Smooth UI Transitions.

---

## 🎨 Visual & Design System

- **Background:** Dark Navy (`#0B1120`) with elevated surface cards (`#111C30`)
- **Primary Accent:** Royal Blue (`#4F7CFF`) with hover transitions
- **Success & Accuracy:** Emerald Teal (`#10B981`)
- **Warning & Streaks:** Amber (`#F59E0B`)
- **Typography:** Modern clean sans (`-apple-system`, `Inter`, `Segoe UI`, `Roboto`)
- **Icons:** Crisp, native Lucide-style JavaFX vector icons (`IconUtils`) with zero external font dependencies
- **Animations:** Hardware-accelerated JavaFX transitions (`FadeTransition`, `TranslateTransition`, `ScaleTransition`, `ParallelTransition`) with 150–220ms natural easing curves

---

## 🏗️ Architecture & Member Integration Points

This application is decoupled using interfaces and the `ServiceLocator` pattern so other team members can plug in their work without touching the UI code.

```
src/main/java/com/typing/
├── MainApp.java                       # Application entry point & stage configuration
├── component/
│   ├── MetricCard.java                # Reusable summary stat card with icon badge
│   ├── SidebarNavButton.java          # Sidebar button with active indicator pill
│   └── ConfirmationDialog.java        # Modal dialog with smooth backdrop fade & pop-in
├── controller/
│   ├── MainLayoutController.java      # Sidebar & header shell controller
│   ├── DashboardController.java       # Summary cards, quick start, chart & history preview
│   ├── TypingTestController.java      # Dedicated test screen with live HUD & input area
│   ├── HistoryController.java         # Filterable test records table & empty state
│   └── SettingsController.java        # User profile & test preferences
├── model/
│   ├── TestDifficulty.java            # Easy, Medium, Hard with styling badges
│   ├── TestDuration.java              # 30s, 60s, 120s presets
│   ├── TestResult.java                # Completed test record with computed grades
│   ├── TypingMetrics.java             # Live session snapshot (WPM, Acc, Errors)
│   └── UserProfile.java               # User info & career stats
├── navigation/
│   ├── NavigationManager.java         # Centralized screen switcher with slide & fade
│   └── ViewType.java                  # Screen view enumeration & titles
├── service/
│   ├── ITypingEngineService.java      # [Member 2] Typing calculation & real-time metrics
│   ├── IHistoryService.java           # [Member 3] Data persistence & analytics queries
│   ├── IUserProfileService.java       # [Member 4] User profile & preferences
│   ├── ServiceLocator.java            # Dependency injection registry for swapping mocks
│   └── mock/
│       ├── MockTypingEngineService.java # Interactive live typing & countdown engine
│       ├── MockHistoryService.java      # Preloaded sample logs & trend analytics
│       └── MockUserProfileService.java  # Sample profile provider
└── util/
    ├── AnimationUtils.java            # Tactile button scales, page slides & modal entrances
    └── IconUtils.java                 # Lucide vector SVG icon paths
```

### 🤝 How Other Team Members Integrate:
To replace the mock implementations with your production backend, simply register your implementation in `ServiceLocator`:
```java
// Member 2: Connect real typing calculation engine
ServiceLocator.getInstance().registerTypingEngine(new RealTypingEngineService());

// Member 3: Connect SQLite / File database
ServiceLocator.getInstance().registerHistoryService(new SqliteHistoryService());

// Member 4: Connect user profile manager
ServiceLocator.getInstance().registerUserProfileService(new UserPreferencesService());
```

---

## 🚀 How to Run the Application

### Prerequisites:
- Java 17+ (or Java 21 / 26)
- Maven 3.8+ (or use the included `./mvnw` script)

### Commands:
```bash
# 1. Compile the project
./mvnw clean compile

# 2. Launch the JavaFX desktop application
./mvnw javafx:run
```

If Maven is installed globally on your path:
```bash
mvn clean compile
mvn javafx:run
```
