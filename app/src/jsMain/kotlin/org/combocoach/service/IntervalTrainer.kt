package org.combocoach.service

import org.combocoach.domain.*
import kotlinx.browser.window

/**
 * Interval-based trainer that generates and displays combinations one action at a time.
 * 
 * This trainer:
 * - Generates a complete flowing combination
 * - Displays each action with a configurable interval
 * - Automatically starts next combination after interval
 * - Calls back to UI to display each action
 * - Tracks current position throughout the combination
 */
class IntervalTrainer(
    private val config: TrainingConfiguration = TrainingConfiguration()
) {
    private val generator = FlowCombinationGenerator(config)
    private var currentCombination: List<Action> = emptyList()
    private var currentActionIndex = 0
    private var timerId: Int? = null
    private var isRunning = false
    private var totalCombinationsCompleted = 0
    
    /**
     * Callback function to display each action
     * Parameters: (action, actionIndex, totalActions, isComplete)
     */
    var onActionDisplay: ((Action, Int, Int, Boolean) -> Unit)? = null
    
    /**
     * Callback when combination is complete
     * Parameters: (combinationNumber)
     */
    var onCombinationComplete: ((Int) -> Unit)? = null
    
    /**
     * Callback when waiting between combinations
     * Parameters: (secondsRemaining)
     */
    var onWaitingBetweenCombinations: ((Int) -> Unit)? = null
    
    /**
     * Starts continuous interval-based training
     */
    fun startContinuousTraining() {
        stop() // Stop any existing training
        totalCombinationsCompleted = 0
        startNextCombination()
    }
    
    /**
     * Starts a new interval-based combination
     */
    private fun startNextCombination() {
        currentCombination = generator.generateFlowingCombination()
        currentActionIndex = 0
        isRunning = true
        
        // Display first action immediately
        displayCurrentAction()
        
        // Schedule remaining actions
        scheduleNextAction()
    }
    
    /**
     * Stops the current training session
     */
    fun stop() {
        timerId?.let { window.clearTimeout(it) }
        timerId = null
        isRunning = false
        currentActionIndex = 0
        currentCombination = emptyList() // Clear combination (avoids isPaused() returning true)
    }
    
    /**
     * Returns total combinations completed in current session
     */
    fun getTotalCombinationsCompleted(): Int = totalCombinationsCompleted
    
    /**
     * Pauses the current combination
     */
    fun pause() {
        timerId?.let { window.clearTimeout(it) }
        timerId = null
        isRunning = false
    }
    
    /**
     * Resumes a paused combination
     */
    fun resume() {
        if (!isRunning && currentActionIndex < currentCombination.size) {
            isRunning = true
            scheduleNextAction()
        }
    }
    
    /**
     * Returns true if a combination is currently running
     */
    fun isActive(): Boolean = isRunning
    
    /**
     * Returns true if training is paused (has combination in progress but not running)
     */
    fun isPaused(): Boolean = !isRunning && currentCombination.isNotEmpty() && currentActionIndex < currentCombination.size
    
    /**
     * Returns the current combination
     */
    fun getCurrentCombination(): List<Action> = currentCombination
    
    /**
     * Returns the current progress (0.0 to 1.0)
     * Note: currentActionIndex points to the next action to display,
     * so we add 1 to reflect the action currently being shown
     */
    fun getProgress(): Float {
        if (currentCombination.isEmpty()) return 0f
        // Add 1 because currentActionIndex is the next to display, but we've already shown the current one
        return (currentActionIndex + 1).toFloat() / currentCombination.size.toFloat()
    }
    
    private fun displayCurrentAction() {
        if (currentActionIndex < currentCombination.size) {
            val action = currentCombination[currentActionIndex]
            val isComplete = currentActionIndex == currentCombination.size - 1
            
            onActionDisplay?.invoke(
                action,
                currentActionIndex + 1, // 1-based for display
                currentCombination.size,
                isComplete
            )
        }
    }
    
    private fun scheduleNextAction() {
        if (!isRunning) return // If not running, do nothing
        
        // Schedule next action display based on configured interval
        // Set a timeout for action interval (this does the following: waits, then displays next action)
        timerId = window.setTimeout({
            currentActionIndex++
            
            if (currentActionIndex < currentCombination.size) {
                displayCurrentAction()
                scheduleNextAction()
            } else {
                // Combination complete
                totalCombinationsCompleted++
                onCombinationComplete?.invoke(totalCombinationsCompleted)
                
                // Wait before starting next combination
                scheduleNextCombination()
            }
        }, config.actionIntervalMs)
    }
    
    private fun scheduleNextCombination() {
        if (!isRunning) return // If not running, do nothing
        
        // Show countdown between combinations
        val intervalSeconds = config.combinationIntervalMs / 1000
        var secondsRemaining = intervalSeconds
        
        fun countdown() {
            if (!isRunning) return
            
            if (secondsRemaining > 0) {
                onWaitingBetweenCombinations?.invoke(secondsRemaining)
                secondsRemaining--
                timerId = window.setTimeout({ countdown() }, 1000)
            } else {
                // Start next combination
                startNextCombination()
            }
        }
        
        countdown()
    }
}
