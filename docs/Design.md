# Design & Architecture

This document outlines the high-level design, architecture layers, boxing flow system, design patterns, and key design decisions of the ComboCoach application.

## High Level Design (HLD)
```
┌───────────────────────────────────────────────────────────┐
│                     Browser (Client)                      │
├───────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────┐  │
│  │            ComboCoachApp (Orchestrator)             │  │
│  └─────────────────────────────────────────────────────┘  │
│  ┌─────────────┬─────────────────┬─────────────────────┐  │
│  │ UI Layer    │ Service Layer   │   Domain Layer      │  │
│  │             │                 │                     │  │
│  │ Components  │ ComboTrainer    │   Business Logic    │  │
│  │ Managers    │ IntervalTrainer │   Entities          │  │
│  │ State       │ Generator       │   Rules             │  │
│  └─────────────┴─────────────────┴─────────────────────┘  │
└───────────────────────────────────────────────────────────┘
```

## Architecture Layers

```
UI Layer (depends on ↓)
    ↓
Service Layer (depends on ↓)
    ↓
Domain Layer (no dependencies)
```

### Domain Layer

**Purpose**: Core business logic

**Key Classes**:
- `Action` (sealed class) - Strikes and defense
- `Position` (enum) - NEUTRAL, LEAD_EXTENDED, REAR_EXTENDED
- `Stance` (enum) - ORTHODOX, SOUTHPAW
- `TrainingConfiguration` - Immutable config

### Service Layer

**Purpose**: Application logic

**Key Classes**:
- `FlowCombinationGenerator` - Creates valid combinations
- `IntervalTrainer` - Manages timing
- `ComboTrainer` - Facade for trainers

### UI Layer

**Purpose**: User interface

**Structure**: Atomic design (atoms → molecules → organisms)

**Key Classes**:
- `ComboCoachApp` - Main orchestrator
- `TrainerManager` - Business logic facade
- `TrainingStateManager` - State management

## Boxing Flow System

**Core Principle**: Every action changes fighter position. Only valid actions allowed from each position.

**Positions**:
- NEUTRAL: Any action valid
- LEAD_EXTENDED: Rear hand actions + jab valid
- REAR_EXTENDED: Lead hand actions valid

**Example Flow**:
```
NEUTRAL → Jab (1) → LEAD_EXTENDED
LEAD_EXTENDED → Cross (2) → REAR_EXTENDED
REAR_EXTENDED → Lead Hook (3) → LEAD_EXTENDED
```

**Position Validation**:
```kotlin
Position.canPerformAction(action, stance): Boolean
Position.after(action, stance): Position
```

## Design Patterns

1. **Sealed Classes** - Type-safe action hierarchy
2. **Facade** - ComboTrainer, TrainerManager
3. **Observer** - Callbacks for UI updates
4. **State** - TrainingStateManager
5. **Atomic Design** - UI component structure

## Key Design Decisions

### 1. Position-Based Flow
**Why**: Ensures realistic combinations based on boxing mechanics
**Alternative**: Random generation (rejected - unrealistic)

### 2. Immutable Configuration
**Why**: Prevents accidental modification, safe to pass around
**Alternative**: Mutable objects (rejected - side effects)

### 3. Frontend-Only
**Why**: No user data, instant access, privacy, simplicity
**Alternative**: Backend (rejected - unnecessary complexity)

### 4. Sealed Classes for Actions
**Why**: Type safety, exhaustive when expressions, data in defense actions
**Alternative**: Enums (rejected - can't hold data)

### 5. Vercel for Deployment
**Why**: 
- Zero-config deployment for static sites
- Automatic HTTPS and CDN distribution
- Perfect fit for Kotlin/JS single-page applications
- Free tier sufficient for personal projects
- GitHub integration for CI/CD
- Instant rollbacks and preview deployments
**Alternative**: GitHub Pages (considered - less flexible), Self-hosting (rejected - unnecessary maintenance overhead)

---
# UI Component Architecture

This document describes the UI architecture following atomic design principles.

## Architecture Overview

The application now follows a **hierarchical component structure** with three levels:

### 1. **Atoms** (Smallest Components)
Located in: `org.combocoach.ui.atoms/`

- **ButtonAtom.kt** - Reusable button component with enable/disable functionality

### 2. **Molecules** (Combinations of Atoms)
Located in: `org.combocoach.ui.molecules/`

- **ControlPanelMolecule.kt** - Action buttons panel (Configuration, Start, Stop, Preview)
- **ConfigFormMolecule.kt** - Configuration form with all training settings
- **ActionCardMolecule.kt** - Displays current action or waiting state
- **TrainingStatsMolecule.kt** - Shows training statistics (combos completed)
- **PreviewDisplayMolecule.kt** - Displays combo preview with formatted text
- **StrikeLegendMolecule.kt** - Strike notation reference

### 3. **Organisms** (Complex Components)
Located in: `org.combocoach.ui.components/`

- **DisplayAreaComponent.kt** - Main display area (organism)
  - Contains: Control Panel + Content Area
  - Manages display modes: Welcome, Configuration, Training, Preview
  
- **HeaderComponent.kt** - Application header

### 4. **Managers** (State & Logic)
Located in: `org.combocoach.ui/`

- **TrainerManager.kt** - Business logic facade
  - Manages ComboTrainer instance
  - Handles training lifecycle
  - Provides clean API for UI components
  
- **TrainingStateManager.kt** - State management
  - Tracks training state
  - Manages configuration
  - Handles state transitions

## Display Area Structure

The `DisplayAreaComponent` is the main interactive area with two sections:

```
┌────────────────────────────────────────────────────────────────────────┐
│         Display Area (Organism)                                        │
├────────────────────────────────────────────────────────────────────────┤
│  ┌───────────────────────────────────────────────────────────────────┐ │
│  │    Control Panel (Molecule)                                       │ │
│  │  [⚙️ Config] [▶️ Start] [⏹️ Stop] [👁️ Preview]                  │ │
│  └───────────────────────────────────────────────────────────────────┘ │
│  ┌───────────────────────────────────────────────────────────────────┐ │
│  │    Content Area                                                   │ │
│  │    (Shows: Config Form | Training | Preview | Welcome)            │ │
│  └───────────────────────────────────────────────────────────────────┘ │
└────────────────────────────────────────────────────────────────────────┘
```

## Display Modes

The Content Area can display different modes:

1. **WELCOME** - Initial welcome message
2. **CONFIGURATION** - Configuration form (ConfigFormMolecule)
3. **TRAINING** - Active training session with stats, action cards, progress bar
4. **PREVIEW** - Combo preview display

## Benefits of This Architecture

✅ **Atomic Design** - Clear hierarchy from atoms → molecules → organisms
✅ **Single Display Area** - All content shown in one unified component
✅ **Consistent Control Panel** - Always visible with all main actions
✅ **Mode-Based Content** - Content area switches based on user interaction
✅ **Reusable Components** - Atoms and molecules can be used independently
✅ **Separation of Concerns** - Each component has a single responsibility
✅ **Easy Testing** - Components can be tested in isolation
✅ **Maintainability** - Easy to find and modify specific functionality

## File Structure

```
app/src/jsMain/kotlin/org/combocoach/ui/
├── ComboCoachApp.kt                   # Main orchestrator
├── NotificationManager.kt             # Toast notifications
├── TrainerManager.kt                  # Business logic facade
├── TrainingStateManager.kt            # State management
├── atoms/
│   └── ButtonAtom.kt                  # Button component
├── molecules/
│   ├── ActionCardMolecule.kt          # Action display
│   ├── ConfigFormMolecule.kt          # Config form
│   ├── ControlPanelMolecule.kt        # Control buttons
│   ├── PreviewDisplayMolecule.kt      # Preview display
│   ├── StrikeLegendMolecule.kt        # Strike reference
│   └── TrainingStatsMolecule.kt       # Stats display
└── components/
    ├── DisplayAreaComponent.kt        # Main display (organism)
    └── HeaderComponent.kt             # App header
```

## Component Responsibilities

### ComboCoachApp (Main Orchestrator)
- Entry point for the application
- Initializes managers and components
- Renders the main application structure
- Minimal business logic (delegated to managers)

### TrainerManager (Business Logic Facade)
- Manages ComboTrainer instance
- Handles training lifecycle (start, stop, pause)
- Coordinates with IntervalTrainer
- Provides clean API for UI components
- Sets up trainer callbacks

### TrainingStateManager (State Management)
- Tracks current training state
- Manages configuration
- Handles state transitions
- Provides state query methods
- Ensures consistent state updates

### DisplayAreaComponent (Main UI Organism)
- Contains Control Panel and Content Area
- Manages display mode switching
- Coordinates between sub-components
- Provides methods for showing different states

### Molecules
- Self-contained UI components
- Combine atoms to create functional units
- Handle their own rendering logic
- Expose clean APIs for parent components

### Atoms
- Smallest reusable components
- No business logic
- Pure presentation components
- Highly reusable across the application
