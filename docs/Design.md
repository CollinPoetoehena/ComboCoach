# Design & Architecture

TODO: extend this a bit and use the latest version of the code/setup for this in the future.


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
│  │ Managers    │ IntervalTrainer │  Entities           │  │
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

See [UIArchitecture.md](UIArchitecture.md) for UI component details.
