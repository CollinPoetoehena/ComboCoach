package org.combocoach.service

import org.combocoach.domain.*
import kotlinx.browser.window

/**
 * Interval-based trainer that generates and displays combinations one action at a time.
 * 
 * This trainer:
 * - Generates a complete flowing combination
 * - Displays each action with a configurable interval
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
    
    /**
     * Callback function to display each action
     * Parameters: (action, actionIndex, totalActions, isComplete)
     */
    var onActionDisplay: ((Action, Int, Int, Boolean) -> Unit)? = null
    
    /**
     * Callback when combination is complete
     */
    var onCombinationComplete: (() -> Unit)? = null
    
    /**
     * Starts a new interval-based combination
     */
    fun startCombination() {
        stop() // Stop any existing combination
        
        currentCombination = generator.generateFlowingCombination()
        currentActionIndex = 0
        isRunning = true
        
        // Display first action immediately
        displayCurrentAction()
        
        // Schedule remaining actions
        scheduleNextAction()
    }
    
    /**
     * Stops the current combination
     */
    fun stop() {
        timerId?.let { window.clearTimeout(it) }
        timerId = null
        isRunning = false
        currentActionIndex = 0
    }
    
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
     * Returns the current combination
     */
    fun getCurrentCombination(): List<Action> = currentCombination
    
    /**
     * Returns the current progress (0.0 to 1.0)
     */
    fun getProgress(): Float {
        if (currentCombination.isEmpty()) return 0f
        return currentActionIndex.toFloat() / currentCombination.size.toFloat()
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
        if (!isRunning) return
        
        timerId = window.setTimeout({
            currentActionIndex++
            
            if (currentActionIndex < currentCombination.size) {
                displayCurrentAction()
                scheduleNextAction()
            } else {
                // Combination complete
                isRunning = false
                onCombinationComplete?.invoke()
            }
        }, config.actionIntervalMs)
    }
}
