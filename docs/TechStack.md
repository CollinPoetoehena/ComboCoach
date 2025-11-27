# Technology Stack

TODO: extend this a bit and use the latest version of the code/setup for this in the future.


## Overview

ComboCoach is a **frontend-only** application built with Kotlin/JS. No backend, database, or authentication required.

**Why frontend-only?**
- Algorithm-based generation (no user data to store)
- Instant access (no accounts)
- Complete privacy (nothing leaves browser)
- Free to deploy

## Core Technologies

### Kotlin/JS with IR Compiler

- Kotlin/JS details: https://kotlinlang.org/docs/js-project-setup.html
- Modern type-safe language compiled to JavaScript
- Null safety, sealed classes, data classes
- Smaller bundles with IR compiler
- Excellent IDE support

### kotlinx-html

- Type-safe HTML generation in Kotlin
- Compile-time validation
- No runtime template parsing

### Gradle + Webpack

- Kotlin DSL build scripts
- Automatic webpack bundling
- Hot reload development server: `./gradlew jsBrowserDevelopmentRun --continuous`

### Testing

- **kotlin-test-js**: Kotlin's official testing library for JavaScript
- **Mocha**: Fast, reliable test runner for Node.js
- **Node.js**: Test execution environment (browser tests disabled)
- **Run tests**: `./gradlew jsNodeTest`
- **Philosophy**: Focus on business logic testing with Node.js for speed and simplicity

See [Testing.md](Testing.md) for detailed testing strategy and best practices.

## Architecture

### Clean Architecture Layers

1. **Domain** - Pure business logic, no dependencies
2. **Service** - Application logic, uses domain
3. **UI** - Presentation, uses service and domain

### Atomic Design for UI

Atoms → Molecules → Components → App

### State Management

- `TrainingStateManager` - centralized state
- `TrainerManager` - business logic facade
- No Redux/MobX needed (simple app)

## Dependencies

**Runtime**: Only `kotlinx-html-js:0.11.0`

**Dev**: `kotlin-test-js` + Gradle plugins

**Philosophy**: Minimal dependencies = smaller bundle, better security, easier maintenance

## Why Kotlin/JS?

**Advantages**:
- Superior type system (null safety, sealed classes, data classes)
- Compile-time safety catches errors early
- Type-safe HTML generation
- Modern language features (extension functions, smart casts)
- Excellent IDE support

**Trade-offs**:
- Larger bundle size than plain JS/TS (~150KB gzipped)
- Steeper learning curve
- Smaller community than TypeScript

**Verdict**: Type safety and developer experience worth the trade-offs for this project.