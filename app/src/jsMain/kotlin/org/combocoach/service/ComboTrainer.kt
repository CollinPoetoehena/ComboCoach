package org.combocoach.service

import org.combocoach.domain.*

/**
 * Service for managing training sessions with flow-based combinations.
 * Simplified to focus on active training without history tracking.
 */
class ComboTrainer(
    var config: TrainingConfiguration = TrainingConfiguration()
) {
    private var flowGenerator = FlowCombinationGenerator(config)
    private var intervalTrainer = IntervalTrainer(config)
    
    fun getIntervalTrainer(): IntervalTrainer = intervalTrainer
    
    fun generateFlowingCombo(): List<Action> {
        return flowGenerator.generateFlowingCombination()
    }
    
    fun startIntervalTraining() {
        intervalTrainer.startContinuousTraining()
    }
    
    fun updateConfiguration(newConfig: TrainingConfiguration) {
        config = newConfig
        flowGenerator = FlowCombinationGenerator(config)
        intervalTrainer = IntervalTrainer(config)
    }
    
    fun formatCombination(actions: List<Action>): String {
        return flowGenerator.formatActions(actions)
    }
}
