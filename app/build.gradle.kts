/*
 * ComboCoach - Kotlin/JS Web Application
 * Build configuration for a Kotlin multiplatform JavaScript application
 */

// Apply the Kotlin multiplatform plugin for cross-platform Kotlin development
plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

// Configure Maven Central as the dependency repository
repositories {
    mavenCentral()
}

kotlin {
    // Configure JavaScript compilation with IR (Intermediate Representation) backend
    // IR backend provides better performance and optimization compared to legacy backend
    js(IR) {
        // Browser configuration - for web application deployment
        browser {
            // Webpack configuration for bundling the application
            // See detailed docs: https://kotlinlang.org/docs/js-project-setup.html#webpack-task
            // NOTE: use commonWebpackConfig to set options common to both dev and prod builds
            commonWebpackConfig {
                outputFileName = "combocoach.js"
            }
            
            // Test configuration for browser-based tests
            testTask {
                // Browser tests are DISABLED for this project
                // Reason: Business logic is tested via Node.js (faster, no browser dependencies)
                // Browser tests are only needed for DOM manipulation and UI integration testing
                // See docs/Testing.md for detailed testing strategy
                enabled = false
            }
        }
        
        // Node.js configuration - enables running tests in Node.js environment
        // This is our primary test environment because:
        // - Much faster than browser tests (1-2s vs 10-30s)
        // - No browser installation required
        // - Perfect for unit testing business logic
        // - Works seamlessly in CI/CD pipelines
        nodejs {
            // Configure test task to always show test output
            testTask {
                testLogging {
                    // Show detailed events during test execution
                    events("passed", "skipped", "failed")
                    // Show standard output and error streams
                    showStandardStreams = false
                    // Display the cause of failures
                    showCauses = true
                    showExceptions = true
                    showStackTraces = true
                    // Show test count summary after execution
                    afterSuite(KotlinClosure2({ desc: TestDescriptor, result: TestResult ->
                        if (desc.parent == null) { // Only print summary for the root suite
                            println("\nTest Results: ${result.resultType}")
                            println("   Tests run: ${result.testCount}")
                            println("   Passed: ${result.successfulTestCount}")
                            println("   Failed: ${result.failedTestCount}")
                            println("   Skipped: ${result.skippedTestCount}")
                            println("   Duration: ${result.endTime - result.startTime}ms\n")
                        }
                    }))
                }
            }
        }
        
        // Generate executable JavaScript file (can be run directly)
        binaries.executable()
    }
    
    // Source sets define the structure of source code and dependencies
    sourceSets {
        // Main source set - production code that runs in the browser
        val jsMain by getting {
            dependencies {
                // kotlinx-html: DSL for building HTML in Kotlin
                // Used for creating the web UI programmatically
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.11.0")
            }
        }
        
        // Test source set - test code that runs in Node.js
        val jsTest by getting {
            dependencies {
                // Kotlin test framework for JavaScript
                // Provides @Test annotations, assertions (assertEquals, assertTrue, etc.)
                implementation(kotlin("test-js"))
            }
        }
    }
}
