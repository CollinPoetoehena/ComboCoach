# UI Component Architecture

TODO: extend this a bit and use the latest version of the code/setup for this in the future.

This document describes the UI architecture following atomic design principles.

## Architecture Overview

The application now follows a **hierarchical component structure** with three levels:

### 1. **Atoms** (Smallest Components)
Located in: `org.combocoach.ui.components.atoms/`

- **ButtonAtom.kt** - Reusable button component with enable/disable functionality

### 2. **Molecules** (Combinations of Atoms)
Located in: `org.combocoach.ui.components.molecules/`

- **ControlPanelMolecule.kt** - Action buttons panel (Configuration, Start, Stop, Preview)
- **ConfigFormMolecule.kt** - Configuration form with all training settings
- **ActionCardMolecule.kt** - Displays current action or waiting state
- **TrainingStatsMolecule.kt** - Shows training statistics (combos completed)
- **PreviewDisplayMolecule.kt** - Displays combo preview with formatted text

### 3. **Organisms** (Complex Components)
Located in: `org.combocoach.ui.components/`

- **DisplayAreaComponent.kt** - Main display area (organism)
  - Contains: Control Panel + Content Area
  - Manages display modes: Welcome, Configuration, Training, Preview
  
- **HeaderComponent.kt** - Application header
- **StrikeLegendComponent.kt** - Strike notation reference

## Display Area Structure

The `DisplayAreaComponent` is the main interactive area with two sections:

```
┌─────────────────────────────────────────────────┐
│         Display Area (Organism)                 │
├─────────────────────────────────────────────────┤
│  ┌───────────────────────────────────────────┐  │
│  │    Control Panel (Molecule)               │  │
│  │  [⚙️ Config] [▶️ Start] [⏹️ Stop] [👁️ Preview] │
│  └───────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────┐  │
│  │    Content Area                           │  │
│  │    (Shows: Config Form | Training |       │  │
│  │             Preview | Welcome)            │  │
│  └───────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
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
├── ComboCoachApp.kt                    # Main orchestrator
├── NotificationManager.kt              # Toast notifications
└── components/
    ├── atoms/
    │   └── ButtonAtom.kt              # Button component
    ├── molecules/
    │   ├── ActionCardMolecule.kt      # Action display
    │   ├── ConfigFormMolecule.kt      # Config form
    │   ├── ControlPanelMolecule.kt    # Control buttons
    │   ├── PreviewDisplayMolecule.kt  # Preview display
    │   └── TrainingStatsMolecule.kt   # Stats display
    ├── DisplayAreaComponent.kt         # Main display (organism)
    ├── HeaderComponent.kt              # App header
    └── StrikeLegendComponent.kt        # Strike reference
```

## Component Responsibilities

### ComboCoachApp (Main Orchestrator)
- Manages application state (config, trainer, current actions)
- Coordinates between trainer service and UI components
- Handles training lifecycle (start, stop, pause)
- Sets up interval trainer callbacks

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
