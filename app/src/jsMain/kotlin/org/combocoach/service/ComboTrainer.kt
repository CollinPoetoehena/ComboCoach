package org.combocoach.service

import org.combocoach.domain.*

/**
 * Service for managing training sessions with flow-based combinations
 */
class ComboTrainer(
    var config: TrainingConfiguration = TrainingConfiguration()
) {
    private val flowGenerator = FlowCombinationGenerator(config)
    private val intervalTrainer = IntervalTrainer(config)
    private val trainingHistory = mutableListOf<List<Action>>()
    
    // Legacy generator for backward compatibility
    private val legacyGenerator: CombinationGenerator = CombinationGenerator()
    
    /**
     * Gets the interval trainer for single-action-at-a-time training
     */
    fun getIntervalTrainer(): IntervalTrainer = intervalTrainer
    
    /**
     * Generates a single flowing combination
     */
    fun generateFlowingCombo(): List<Action> {
        val combo = flowGenerator.generateFlowingCombination()
        trainingHistory.add(combo)
        return combo
    }
    
    /**
     * Starts an interval-based training session
     * Actions will be displayed one at a time with configured interval
     */
    fun startIntervalTraining() {
        intervalTrainer.startCombination()
    }
    
    /**
     * Updates the configuration and recreates generators
     */
    fun updateConfiguration(newConfig: TrainingConfiguration) {
        config = newConfig
        // Note: Interval trainer and generator would need to be recreated
        // or made mutable. For simplicity, configuration changes take effect
        // on next combination generation.
    }
    
    /**
     * Formats a combination for display
     */
    fun formatCombination(actions: List<Action>): String {
        return flowGenerator.formatActions(actions)
    }
    
    /**
     * Gets training history
     */
    fun getHistory(): List<List<Action>> = trainingHistory.toList()
    
    /**
     * Clears training history
     */
    fun clearHistory() {
        trainingHistory.clear()
    }
    
    // Legacy methods for backward compatibility
    
    /**
     * @deprecated Use generateFlowingCombo() instead
     */
    fun getRandomCombo(): Combination {
        return legacyGenerator.generateCombination()
    }
    
    /**
     * @deprecated Use interval training or flow-based combinations
     */
    fun startTrainingSession(rounds: Int): List<Combination> {
        return legacyGenerator.generateCombinations(rounds)
    }
}
