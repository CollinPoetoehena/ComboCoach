# Testing Guide

TODO: extend this a bit and use the latest version of the code/setup for this in the future.


## Test Framework

- **kotlin-test-js** with Karma and Chrome Headless
- **Run**: `./gradlew test`
- **Report**: `app/build/reports/tests/test/index.html`

## Test Structure

```
app/src/test/kotlin/org/combocoach/
├── AppTest.kt
├── CombinationGeneratorTest.kt
└── (add more tests as needed)
```

## Writing Tests

Use AAA pattern (Arrange, Act, Assert):

```kotlin
@Test
fun `descriptive test name in backticks`() {
    // Arrange
    val config = TrainingConfiguration(minActions = 3)
    val generator = FlowCombinationGenerator(config)
    
    // Act
    val combo = generator.generateFlowingCombination()
    
    // Assert
    assertTrue(combo.size >= 3)
}
```

## Example Tests

### Domain Test

```kotlin
@Test
fun `neutral position allows any action`() {
    val position = Position.NEUTRAL
    val stance = Stance.ORTHODOX
    
    assertTrue(position.canPerformAction(Action.Jab, stance))
    assertTrue(position.canPerformAction(Action.Cross, stance))
}
```

### Service Test

```kotlin
@Test
fun `generates combinations in range`() {
    val config = TrainingConfiguration(minActions = 4, maxActions = 6)
    val generator = FlowCombinationGenerator(config)
    
    repeat(50) {
        val combo = generator.generateFlowingCombination()
        assertTrue(combo.size in 4..6)
    }
}
```

## Best Practices

1. **One test, one thing**: Test single behavior per test
2. **Descriptive names**: Use backticks for readable test names
3. **Independent tests**: No shared state between tests
4. **Test edge cases**: Min/max values, empty cases
5. **Test randomness**: Use repeat() for random behavior

## Coverage Goals

- Domain: 80%+
- Service: 70%+
- UI: 30%+ (complex logic only)

## Running Tests

```bash
# All tests
./gradlew test

# Specific class
./gradlew test --tests CombinationGeneratorTest

# Continuous
./gradlew test --continuous
```
