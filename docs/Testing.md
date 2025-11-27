# Testing Guide

## Testing Strategy

ComboCoach uses **Node.js-based unit tests** exclusively for testing business logic and functionality. Browser tests are intentionally disabled because they are unnecessary for validating the application's core behavior.

### Why Node.js Tests Only?

**Node.js tests are sufficient because:**
- ✅ **Business Logic Focus**: Tests validate all domain models, service algorithms, and state management
- ✅ **Fast Execution**: Node.js tests run in 1-2 seconds vs 10-30 seconds for browser tests
- ✅ **CI/CD Friendly**: No browser dependencies, works seamlessly in GitHub Actions and other CI systems
- ✅ **Developer Experience**: Instant feedback during development with no browser startup overhead
- ✅ **Zero Configuration**: Works out-of-the-box without browser installation or environment setup

**When browser tests ARE needed (not applicable to ComboCoach currently):**
- 🌐 Visual regression testing (screenshot comparison)
- 🌐 Cross-browser compatibility verification
- 🌐 Complex DOM manipulation that can't be unit tested
- 🌐 Integration testing with external browser APIs
- 🌐 End-to-end user flows through the entire application

**ComboCoach doesn't need browser tests because:**
- All business logic (combo generation, flow validation, position tracking) is pure computation
- UI rendering uses kotlinx-html DSL which generates predictable HTML
- No complex browser-specific APIs that need integration testing
- Manual testing of the UI is sufficient and more practical for this project given the scope and time constraints

## Test Framework

- **kotlin-test-js**: Kotlin's official testing library for JavaScript
- **Node.js**: Test execution environment
- **Test Location**: `app/src/jsTest/kotlin/org/combocoach/`

## Running Tests

```bash
# Run all tests (Node.js only, browser tests disabled)
./gradlew jsTest

# Run only Node.js tests explicitly
./gradlew jsNodeTest

# Run with detailed output
./gradlew jsNodeTest --info

# Clean and re-run tests
./gradlew clean jsNodeTest

# Run tests continuously (watch mode)
./gradlew jsNodeTest --continuous
```

## Test Reports

After running tests, view the HTML report:
```
app/build/reports/tests/jsNodeTest/index.html
```

The report includes:
- Total tests run, passed, failed
- Execution time per test
- Detailed failure messages with stack traces
- Test organization by package and class

## Best Practices

1. **Test One Behavior**: Each test should verify a single, specific behavior
2. **Descriptive Names**: Use clear, action-oriented names that describe what is being tested
3. **Independent Tests**: Tests should not depend on each other or share mutable state
4. **Fast Tests**: Keep tests fast by avoiding unnecessary delays or complex setup
5. **Readable Assertions**: Use descriptive assertion messages for better debugging
6. **Test Edge Cases**: Include tests for boundary conditions, empty inputs, and error cases
7. **Avoid DOM Dependencies**: For UI tests, test state and logic, not DOM manipulation

### Browser API Tests

Tests requiring browser-specific APIs are not included:
- Web Audio API (for audio feedback)
- Local Storage (for configuration persistence)
- Browser notifications (for system alerts)

These can be added later if automated testing becomes necessary, but manual testing is currently sufficient.

## Continuous Integration

The test suite is designed to run efficiently in CI/CD:

```yaml
# Example GitHub Actions workflow
- name: Run Tests
  run: ./gradlew jsNodeTest
```

Benefits:
- No browser installation required
- Fast execution (~2-3 seconds)
- Deterministic results
- Clear failure messages

## Troubleshooting

### Tests Failing Locally

```bash
# Clean build artifacts and re-run
./gradlew clean jsNodeTest

# Check for Node.js version issues
node --version  # Should be 16+

# Verify Gradle is up to date
./gradlew --version
```

### Test Timeouts

If tests timeout:
1. Check for infinite loops in test code
2. Ensure async operations complete
3. Reduce test iterations for random behavior tests

