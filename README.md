# Typing Speed Test and Performance Analyzer

**Java OOP Desktop Application — Modern JavaFX UI & Analytics Studio**

Built for Member 1's module: Application Entry Point, Architecture, Navigation, Design System, Dynamic Theme Engine (Light/Dark), Reusable Components, and UI Transitions.

---

## 🏗️ Clean Architecture & Codebase Structure

The project follows standard Maven conventions with clean separation of concerns, modular OOP design, and the `ServiceLocator` pattern for seamless team integration:

```
Typing Speed/
├── pom.xml                                    # Maven POM: JavaFX 21, JUnit 5, compiler plugins
├── mvnw                                       # Maven Wrapper script (macOS / Linux)
├── .gitignore                                 # Clean git rules (ignores target/, IDE & OS files)
├── README.md                                  # Project documentation & integration guide
│
├── src/main/java/com/typing/
│   ├── MainApp.java                           # Application entry point & Stage configuration
│   │
│   ├── component/                             # Reusable JavaFX UI Controls
│   │   ├── ConfirmationDialog.java            # Modal dialog with backdrop fade & scale animations
│   │   ├── MetricCard.java                    # Horizontal stretchable stat cards with icon badges
│   │   └── SidebarNavButton.java              # Tactile navigation pill with active indicator
│   │
│   ├── controller/                            # Screen Controllers (FXML Bindings)
│   │   ├── MainLayoutController.java          # App shell: Sidebar, dynamic header, and theme toggle
│   │   ├── DashboardController.java           # Summary cards, Quick Start, WPM chart & test table
│   │   ├── TypingTestController.java          # Test workspace: HUD timers, passage display & input
│   │   ├── ResultsController.java             # Detailed performance breakdown & progression velocity
│   │   ├── HistoryController.java             # Filterable historical session logs & empty state
│   │   └── SettingsController.java            # User profile preferences & theme mode selector
│   │
│   ├── model/                                 # Domain Data Models
│   │   ├── TestDifficulty.java                # Enum: EASY, MEDIUM, HARD with display names & styling
│   │   ├── TestDuration.java                  # Enum: 30s, 60s, 120s duration presets
│   │   ├── TestResult.java                    # Completed test record with computed grades & scores
│   │   ├── TypingMetrics.java                 # Live keystroke metrics snapshot (WPM, Acc, Errors)
│   │   └── UserProfile.java                   # User profile, handle, initials & career stats
│   │
│   ├── navigation/                            # Centralized View Navigation
│   │   ├── NavigationManager.java             # Coordinates screen switching & smooth transitions
│   │   └── ViewType.java                      # Enumeration of all 5 views with FXML routes & titles
│   │
│   ├── service/                               # OOP Service Interfaces & Teammate Integration
│   │   ├── ServiceLocator.java                # Dependency injection registry for swapping mock backends
│   │   ├── ITypingEngineService.java          # [Member 2] Typing metrics calculation & countdown
│   │   ├── IHistoryService.java               # [Member 3] Persistent storage & analytics queries
│   │   ├── IUserProfileService.java           # [Member 4] User profile & preferences persistence
│   │   └── mock/                              # Fully working mock implementations for local testing
│   │       ├── MockTypingEngineService.java   # Real-time countdown & passage generator
│   │       ├── MockHistoryService.java        # Preloaded historical records & trend data
│   │       └── MockUserProfileService.java    # Sample profile provider
│   │
│   ├── theme/                                 # Dynamic Theme Management
│   │   └── ThemeManager.java                  # Manages Swiss Light & Obsidian Dark stylesheets dynamically
│   │
│   └── util/                                  # Utilities & Helpers
│       ├── AnimationUtils.java                # Fade, Slide, Scale, Pulse & Button feedback animations
│       └── IconUtils.java                     # Native JavaFX Lucide vector SVG icons
│
├── src/main/resources/com/typing/
│   ├── css/                                   # Curated Professional Design System
│   │   ├── app.css                            # Base Swiss Minimalist Paper (Light Mode)
│   │   └── dark-theme.css                     # Obsidian Dark Theme Overrides
│   │
│   └── fxml/                                  # Declarative UI Layouts
│       ├── main-layout.fxml                   # Outer application shell (Sidebar + Header + Content)
│       ├── dashboard.fxml                     # Main overview dashboard
│       ├── typing-test.fxml                   # Interactive typing benchmark workspace
│       ├── results.fxml                       # In-depth analytics & velocity view
│       ├── history.fxml                       # Filterable historical logs
│       └── settings.fxml                      # User preferences & theme appearance
│
└── src/test/java/com/typing/                  # Automated Test Suite (JUnit 5)
    ├── model/TestResultTest.java              # Unit tests for grade computation & model rules
    ├── navigation/ViewTypeTest.java           # Verification of all routes & FXML paths
    ├── service/ServiceLocatorTest.java        # Dependency injection registry verification
    └── theme/ThemeManagerTest.java            # Theme switching, listeners & asset classpath tests
```

---

## 🎨 Dual Theme System (Light & Dark)

The application supports instant one-click theme switching without reloading views or losing state:

| Element | Swiss Minimalist (Light) | Obsidian Dark |
| :--- | :--- | :--- |
| **Main Background** | `#F5F5F0` (Warm Paper) | `#0B1120` (Deep Slate) |
| **Sidebar / Header** | `#F0F1EB` / `#FFFFFF` | `#0A0F1D` / `#0F172A` |
| **Cards & Panels** | `#FFFFFF` | `#131C31` |
| **Primary Accent** | `#397568` (Muted Teal) | `#2DD4BF` (Mint Cyan) |
| **Typography** | `#252A2B` (Charcoal) | `#F8FAFC` (Pure Light) |
| **Secondary Text** | `#68716E` | `#94A3B8` |
| **Borders** | `#E1E5DF` | `#1E293B` |

### How to Toggle Themes:
1. **Header Button**: Click the Sun ☀️ / Moon 🌙 icon in the top header.
2. **Keyboard Shortcut**: Press `Cmd + T` (macOS) or `Ctrl + T` (Windows / Linux).
3. **Settings**: Select *Swiss Minimalist (Light)* or *Obsidian Dark* from the **Theme Appearance** dropdown in Settings.

---

## 🤝 Team Member Integration Guide

Other team members can plug their backend modules into the UI without modifying any view or controller code:

```java
// Member 2: Connect real typing engine & keystroke evaluator
ServiceLocator.getInstance().registerTypingEngine(new RealTypingEngineService());

// Member 3: Connect SQLite database or JSON file persistence
ServiceLocator.getInstance().registerHistoryService(new SqliteHistoryService());

// Member 4: Connect user profile manager & preferences storage
ServiceLocator.getInstance().registerUserProfileService(new UserPreferencesService());
```

---

## 🚀 Build & Run Instructions

### Prerequisites:
- Java 17+ (e.g. OpenJDK 17, 21, or compatible)
- Maven (or use `./mvnw`)

### Commands:
```bash
# 1. Compile the code
./mvnw clean compile

# 2. Run unit tests
./mvnw test

# 3. Launch the desktop application
./mvnw javafx:run
```
