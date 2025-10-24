package org.example.ui

import org.example.domain.Combination
import org.example.service.ComboTrainer

/**
 * Console-based user interface for the ComboCoach application
 */
class ConsoleUI(private val trainer: ComboTrainer) {
    
    fun start() {
        var running = true
        
        while (running) {
            printMenu()
            
            val input = readLine()?.trim()
            if (input == null) {
                println("No input detected. Exiting...")
                break
            }
            
            when (input) {
                "1" -> generateSingleCombo()
                "2" -> startTrainingSession()
                "3" -> viewHistory()
                "4" -> clearHistory()
                "5" -> showLegend()
                "6" -> {
                    running = false
                    println("Thanks for training with ComboCoach!")
                }
                else -> println("Invalid option. Please try again.")
            }
            
            if (running) {
                println("\nPress Enter to continue...")
                readLine()
            }
        }
    }
    
    private fun printMenu() {
        println("\n" + "=".repeat(50))
        println("COMBOCOACH - MAIN MENU")
        println("=".repeat(50))
        println("1. Generate Single Combination")
        println("2. Start Training Session")
        println("3. View Training History")
        println("4. Clear History")
        println("5. Show Legend")
        println("6. Exit")
        println("=".repeat(50))
        print("Select an option: ")
    }
    
    private fun generateSingleCombo() {
        println("\n" + "-".repeat(50))
        println("RANDOM COMBINATION")
        println("-".repeat(50))
        
        val combo = trainer.getRandomCombo()
        displayCombination(combo)
    }
    
    private fun startTrainingSession() {
        println("\n" + "-".repeat(50))
        println("TRAINING SESSION")
        println("-".repeat(50))
        print("Enter number of rounds (1-20): ")
        
        val rounds = readLine()?.toIntOrNull()?.coerceIn(1, 20) ?: 5
        println("\nStarting $rounds-round training session...\n")
        
        val combinations = trainer.startTrainingSession(rounds)
        
        combinations.forEachIndexed { index, combo ->
            println("Round ${index + 1}:")
            displayCombination(combo)
            println()
            
            if (index < combinations.size - 1) {
                println("Press Enter for next round...")
                readLine()
            }
        }
        
        println("Training session complete!")
    }
    
    private fun viewHistory() {
        println("\n" + "-".repeat(50))
        println("TRAINING HISTORY")
        println("-".repeat(50))
        
        val history = trainer.getHistory()
        
        if (history.isEmpty()) {
            println("No training history yet. Start training to build your history!")
        } else {
            history.forEachIndexed { index, combo ->
                print("${index + 1}. ")
                displayCombination(combo)
            }
            println("\nTotal combinations: ${history.size}")
        }
    }
    
    private fun clearHistory() {
        println("\n" + "-".repeat(50))
        print("Are you sure you want to clear history? (y/n): ")
        
        if (readLine()?.trim()?.lowercase() == "y") {
            trainer.clearHistory()
            println("History cleared!")
        } else {
            println("Operation cancelled.")
        }
    }
    
    private fun showLegend() {
        println("\n" + "-".repeat(50))
        println("COMBOCOACH LEGEND")
        println("-".repeat(50))
        println("\nSTRIKES:")
        println("  1 - Jab")
        println("  2 - Cross")
        println("  3 - Lead Hook")
        println("  4 - Rear Hook")
        println("  5 - Lead Uppercut")
        println("  6 - Rear Uppercut")
        
        println("\nDEFENSIVE MOVES:")
        println("  Slip Left/Right - Move head off centerline")
        println("  Roll Left/Right - Circular shoulder movement")
        println("  Duck - Lower body under strikes")
        println("  Block - Use gloves to absorb strikes")
        println("  Parry - Redirect incoming strikes")
        
        println("\nOPPONENT STRIKES (marked with ⚠️):")
        println("  Defend against these incoming strikes!")
        println("  Each opponent strike suggests defensive moves")
        
        println("\nCOLORS:")
        println("  Colors provide visual cues for combinations")
        println("  Use them to quickly identify combo types")
    }
    
    private fun displayCombination(combo: Combination) {
        val colorizedOutput = combo.color.colorize(combo.toString())
        println(colorizedOutput)
    }
}
