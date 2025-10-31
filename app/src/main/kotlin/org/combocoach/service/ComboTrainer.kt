package org.combocoach.service

import org.combocoach.domain.Combination

/**
 * Service for managing training sessions
 */
class ComboTrainer(
    private val generator: CombinationGenerator = CombinationGenerator()
) {
    
    private val trainingHistory = mutableListOf<Combination>()
    
    /**
     * Starts a training session with specified number of rounds
     */
    fun startTrainingSession(rounds: Int): List<Combination> {
        val combinations = generator.generateCombinations(rounds)
        trainingHistory.addAll(combinations)
        return combinations
    }
    
    /**
     * Gets a single random combination
     */
    fun getRandomCombo(): Combination {
        val combo = generator.generateCombination()
        trainingHistory.add(combo)
        return combo
    }
    
    /**
     * Gets training history
     */
    fun getHistory(): List<Combination> = trainingHistory.toList()
    
    /**
     * Clears training history
     */
    fun clearHistory() {
        trainingHistory.clear()
    }
}
