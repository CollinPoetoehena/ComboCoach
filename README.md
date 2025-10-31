# ComboCoach

A Kotlin-based boxing training application that generates random combinations to help you improve your skills.

## Features

- **Random Combination Generator**: Creates varied strike combinations using proper boxing notation (1-6)
- **Visual Color Cues**: Each combination is assigned a random color for quick visual identification
- **Defensive Moves**: Includes slips, rolls, ducks, blocks, and parries
- **Opponent Strikes**: Simulates incoming attacks that you need to defend against (marked with ⚠️)
- **Training Sessions**: Run multiple rounds of combinations
- **Training History**: Track your generated combinations
- **OOP Design**: Built with Kotlin best practices using proper object-oriented design

## Strike Notation

- **1** - Jab
- **2** - Cross
- **3** - Lead Hook
- **4** - Rear Hook
- **5** - Lead Uppercut
- **6** - Rear Uppercut

## Getting Started

### Prerequisites

- Java OpenJDK: https://www.geeksforgeeks.org/installation-guide/how-to-install-openjdk-in-linux/
- SDKMAN! to install Kotlin: https://sdkman.io/install/
- Optional: Install Homebrew on Linux as package manager: https://docs.brew.sh/Homebrew-on-Linux
- Install Gradle using SDKMAN! or Homebrew on Linux: https://gradle.org/install/
- Install Kotlin using SDKMAN! or Homebrew on Linux: https://kotlinlang.org/docs/command-line.html#install-the-compiler

### Initialize Kotlin Project (ONLY once)
```sh
gradle init --type kotlin-application --dsl kotlin 
```

### Running the Application

```sh
# Build the project:
./gradlew build

# Run the project
./gradlew jsBrowserDevelopmentRun --continuous

# Run tests:
./gradlew test
```

## Usage

TODO: generate with AI

## Architecture

The application follows clean architecture principles with the following structure:

- **Domain Layer** (`domain/`): Core business entities and enums
  - `Strike` - Attack types
  - `DefensiveMove` - Defensive techniques
  - `OpponentStrike` - Incoming attacks to defend
  - `ComboElement` - Sealed class for combination elements
  - `Combination` - Complete combination with color
  - `ComboColor` - Visual color cues

- **Service Layer** (`service/`): Business logic
  - `CombinationGenerator` - Generates random combinations
  - `ComboTrainer` - Manages training sessions

- **UI Layer** (`ui/`): User interface
  - `ConsoleUI` - Interactive console interface

## Future Enhancements

- Sound effects for strikes and combinations
- Timed combinations with countdown
- Difficulty levels
- Combination patterns and drills
- Statistics and performance tracking
- Custom combination builder
- Mobile/GUI application

## License

This project is generated as a sample application.
