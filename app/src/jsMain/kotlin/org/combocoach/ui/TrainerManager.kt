package org.combocoach.ui

import org.combocoach.domain.*
import org.combocoach.service.ComboTrainer

/**
 * Manages trainer instance and coordinates between UI and training logic.
 * Acts as a facade to simplify trainer interactions and maintain state.
 * 
 * This class serves as the business logic layer between the UI (ComboCoachApp)
 * and the domain service (ComboTrainer), encapsulating all training-related
 * state and operations.
 */
class TrainerManager {
    private var config = TrainingConfiguration()
    private var trainer = ComboTrainer(config)
    private var currentDisplayedActions = mutableListOf<Action>()
    
    // Callbacks that UI can set
    var onActionDisplay: ((Action, Int, Int) -> Unit)? = null
    var onCombinationComplete: ((Int) -> Unit)? = null
    var onWaitingBetweenCombinations: ((Int) -> Unit)? = null
    
    init {
        setupCallbacks()
    }
    
    /**
     * Get current configuration
     */
    fun getConfig(): TrainingConfiguration = config
    
    /**
     * Update configuration and reinitialize trainer
     */
    fun updateConfiguration(newConfig: TrainingConfiguration) {
        config = newConfig
        trainer.updateConfiguration(config)
        setupCallbacks()
        NotificationManager.success("Configuration applied!")
    }
    
    /**
     * Start interval training session
     */
    fun startTraining() {
        currentDisplayedActions.clear()
        trainer.startIntervalTraining()
    }
    
    /**
     * Stop current training session
     */
    fun stopTraining() {
        trainer.getIntervalTrainer().stop()
    }
    
    /**
     * Pause current training session
     */
    fun pauseTraining() {
        trainer.getIntervalTrainer().pause()
    }
    
    /**
     * Resume paused training session
     */
    fun resumeTraining() {
        trainer.getIntervalTrainer().resume()
    }
    
    /**
     * Check if training is currently active
     */
    fun isTrainingActive(): Boolean {
        return trainer.getIntervalTrainer().isActive()
    }
    
    /**
     * Check if training is paused
     */
    fun isTrainingPaused(): Boolean {
        return trainer.getIntervalTrainer().isPaused()
    }
    
    /**
     * Generate a preview combination
     * Returns a pair of (combo actions, formatted string)
     */
    fun generatePreview(): Pair<List<Action>, String> {
        val combo = trainer.generateFlowingCombo()
        val formatted = trainer.formatCombination(combo)
        return Pair(combo, formatted)
    }
    
    /**
     * Setup internal callbacks from interval trainer
     */
    private fun setupCallbacks() {
        val intervalTrainer = trainer.getIntervalTrainer()
        
        intervalTrainer.onActionDisplay = { action, index, total, _ ->
            currentDisplayedActions.add(action)
            onActionDisplay?.invoke(action, index, total)
        }
        
        intervalTrainer.onCombinationComplete = { comboNumber ->
            onCombinationComplete?.invoke(comboNumber)
            currentDisplayedActions.clear()
        }
        
        intervalTrainer.onWaitingBetweenCombinations = { secondsRemaining ->
            onWaitingBetweenCombinations?.invoke(secondsRemaining)
        }
    }
    
    /**
     * Get current displayed actions (for preview updates during training)
     */
    fun getCurrentDisplayedActions(): List<Action> = currentDisplayedActions
}
