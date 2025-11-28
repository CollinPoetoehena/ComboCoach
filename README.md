# ComboCoach 🥊

Web-based boxing trainer that generates realistic combinations respecting natural body mechanics and position transitions.

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## Features

- **Flow-Based Combinations**: Position-aware generation (neutral, lead extended, rear extended)
- **Training Modes**: Attack only, defense only, or mixed
- **Interval Training**: Customizable timing between actions
- **Stance Support**: Orthodox and Southpaw
- **Standard Boxing Notation**: 1-6 for strikes, D1-D4 for defense
- **Browser Support**: Tested in Chrome and Edge (other browsers may not support certain features like speech synthesis)

## Prerequisites

- Java OpenJDK: https://www.geeksforgeeks.org/installation-guide/how-to-install-openjdk-in-linux/
- SDKMAN! to install Kotlin: https://sdkman.io/install/
- Optional: Install Homebrew on Linux as package manager: https://docs.brew.sh/Homebrew-on-Linux
- Install Kotlin using SDKMAN! or Homebrew on Linux: https://kotlinlang.org/docs/command-line.html#install-the-compiler
- Gradle included via wrapper: `./gradlew`

## Quick Start

```bash
git clone https://github.com/CollinPoetoehena/ComboCoach.git
cd ComboCoach
./gradlew jsBrowserDevelopmentRun --continuous
```

Access at `http://localhost:8080`

## Usage

1. Click ⚙️ Configuration to set training mode, stance, and intervals
2. Click "Apply Configuration" then "Start Training"
3. Follow on-screen actions
4. Use Pause/Resume/Stop controls as needed

## Further Documentation

- **[Design & Architecture](./docs/Design.md)** - Architecture layers, boxing flow system
- **[TechStack](./docs/TechStack.md)** - Kotlin/JS, frontend-only architecture
- **[Testing](./docs/Testing.md)** - Testing guide and examples
- **[Deployment & CI/CD](./docs/Deployment_CI-CD.md)** - Deployment to Vercel & GitHub Actions CI/CD setup

## License

MIT License - see [LICENSE](LICENSE)