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
    fun `creates trainer with default configuration`() {
        val trainer = IntervalTrainer()
        
        assertNotNull(trainer)
    }
    
    @Test
    fun `creates trainer with custom configuration`() {
        val config = TrainingConfiguration(
            minActions = 5,
            maxActions = 10,
            actionIntervalMs = 2000
        )
        val trainer = IntervalTrainer(config)
        
        assertNotNull(trainer)
    }
    
    @Test
    fun `trainer starts as inactive`() {
        val trainer = IntervalTrainer()
        
        assertFalse(trainer.isActive())
        assertFalse(trainer.isPaused())
    }
    
    @Test
    fun `trainer starts as not paused`() {
        val trainer = IntervalTrainer()
        
        assertFalse(trainer.isPaused())
    }
    
    @Test
    fun `getCurrentCombination returns empty list initially`() {
        val trainer = IntervalTrainer()
        
        val combo = trainer.getCurrentCombination()
        
        assertTrue(combo.isEmpty())
    }
    
    @Test
    fun `getProgress returns zero initially`() {
        val trainer = IntervalTrainer()
        
        val progress = trainer.getProgress()
        
        assertEquals(0f, progress)
    }
    
    @Test
    fun `getTotalCombinationsCompleted returns zero initially`() {
        val trainer = IntervalTrainer()
        
        val total = trainer.getTotalCombinationsCompleted()
        
        assertEquals(0, total)
    }
    
    @Test
    fun `stop sets trainer to inactive`() {
        val trainer = IntervalTrainer()
        
        trainer.stop()
        
        assertFalse(trainer.isActive())
    }
    
    @Test
    fun `stop clears current combination`() {
        val trainer = IntervalTrainer()
        
        trainer.stop()
        
        assertTrue(trainer.getCurrentCombination().isEmpty())
    }
    
    @Test
    fun `multiple stops don't cause errors`() {
        val trainer = IntervalTrainer()
        
        trainer.stop()
        trainer.stop()
        trainer.stop()
        
        assertFalse(trainer.isActive())
    }
    
    @Test
    fun `callbacks can be set`() {
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
    fun `trainer with ATTACK_ONLY mode configuration`() {
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
    fun `trainer with DEFENSE_ONLY mode configuration`() {
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
    fun `trainer with BOTH mode configuration`() {
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
    fun `trainer with southpaw stance configuration`() {
        val config = TrainingConfiguration(stance = Stance.SOUTHPAW)
        val trainer = IntervalTrainer(config)
        
        assertNotNull(trainer)
    }
    
    @Test
    fun `trainer accepts various interval configurations`() {
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
