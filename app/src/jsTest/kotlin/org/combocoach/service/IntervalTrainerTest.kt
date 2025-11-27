package org.combocoach.service

import org.combocoach.domain.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

/**
 * Tests for IntervalTrainer that don't rely on browser timing APIs.
 * Tests focus on state management, configuration, and non-timing logic.
 */
class IntervalTrainerTest {
    
    @Test
    fun createsTrainerWithDefaultConfiguration() {
        val trainer = IntervalTrainer()
        
        assertNotNull(trainer)
    }
    
    @Test
    fun createsTrainerWithCustomConfiguration() {
        val config = TrainingConfiguration(
            minActions = 5,
            maxActions = 10,
            actionIntervalMs = 2000
        )
        val trainer = IntervalTrainer(config)
        
        assertNotNull(trainer)
    }
    
    @Test
    fun trainerStartsAsInactive() {
        val trainer = IntervalTrainer()
        
        assertFalse(trainer.isActive())
        assertFalse(trainer.isPaused())
    }
    
    @Test
    fun trainerStartsAsNotPaused() {
        val trainer = IntervalTrainer()
        
        assertFalse(trainer.isPaused())
    }
    
    @Test
    fun getCurrentCombinationReturnsEmptyListInitially() {
        val trainer = IntervalTrainer()
        
        val combo = trainer.getCurrentCombination()
        
        assertTrue(combo.isEmpty())
    }
    
    @Test
    fun getProgressReturnsZeroInitially() {
        val trainer = IntervalTrainer()
        
        val progress = trainer.getProgress()
        
        assertEquals(0f, progress)
    }
    
    @Test
    fun getTotalCombinationsCompletedReturnsZeroInitially() {
        val trainer = IntervalTrainer()
        
        val total = trainer.getTotalCombinationsCompleted()
        
        assertEquals(0, total)
    }
    
    @Test
    fun stopSetsTrainerToInactive() {
        val trainer = IntervalTrainer()
        
        trainer.stop()
        
        assertFalse(trainer.isActive())
    }
    
    @Test
    fun stopClearsCurrentCombination() {
        val trainer = IntervalTrainer()
        
        trainer.stop()
        
        assertTrue(trainer.getCurrentCombination().isEmpty())
    }
    
    @Test
    fun multipleStopsDontCauseErrors() {
        val trainer = IntervalTrainer()
        
        trainer.stop()
        trainer.stop()
        trainer.stop()
        
        assertFalse(trainer.isActive())
    }
    
    @Test
    fun callbacksCanBeSet() {
        val trainer = IntervalTrainer()
        var actionCallbackCalled = false
        var combinationCallbackCalled = false
        var waitingCallbackCalled = false
        
        trainer.onActionDisplay = { _, _, _, _ -> actionCallbackCalled = true }
        trainer.onCombinationComplete = { _ -> combinationCallbackCalled = true }
        trainer.onWaitingBetweenCombinations = { _ -> waitingCallbackCalled = true }
        
        assertNotNull(trainer.onActionDisplay)
        assertNotNull(trainer.onCombinationComplete)
        assertNotNull(trainer.onWaitingBetweenCombinations)
    }
    
    @Test
    fun trainerWithATTACK_ONLYModeConfiguration() {
        val config = TrainingConfiguration(
            trainingMode = TrainingMode.ATTACK_ONLY,
            minActions = 5,
            maxActions = 5
        )
        val trainer = IntervalTrainer(config)
        
        assertNotNull(trainer)
        assertFalse(trainer.isActive())
    }
    
    @Test
    fun trainerWithDEFENSE_ONLYModeConfiguration() {
        val config = TrainingConfiguration(
            trainingMode = TrainingMode.DEFENSE_ONLY,
            minActions = 5,
            maxActions = 5
        )
        val trainer = IntervalTrainer(config)
        
        assertNotNull(trainer)
        assertFalse(trainer.isActive())
    }
    
    @Test
    fun trainerWithBOTHModeConfiguration() {
        val config = TrainingConfiguration(
            trainingMode = TrainingMode.BOTH,
            minActions = 5,
            maxActions = 5
        )
        val trainer = IntervalTrainer(config)
        
        assertNotNull(trainer)
        assertFalse(trainer.isActive())
    }
    
    @Test
    fun trainerWithSouthpawStanceConfiguration() {
        val config = TrainingConfiguration(stance = Stance.SOUTHPAW)
        val trainer = IntervalTrainer(config)
        
        assertNotNull(trainer)
    }
    
    @Test
    fun trainerAcceptsVariousIntervalConfigurations() {
        val config1 = TrainingConfiguration(
            actionIntervalMs = 500,
            combinationIntervalMs = 1000
        )
        val trainer1 = IntervalTrainer(config1)
        assertNotNull(trainer1)
        
        val config2 = TrainingConfiguration(
            actionIntervalMs = 2000,
            combinationIntervalMs = 5000
        )
        val trainer2 = IntervalTrainer(config2)
        assertNotNull(trainer2)
    }
}
