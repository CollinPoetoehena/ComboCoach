package org.combocoach.ui

import org.combocoach.domain.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

/**
 * Tests for TrainerManager.
 * Note: Tests that call updateConfiguration() are removed because it requires DOM access
 * via NotificationManager.success(). These scenarios are covered by integration tests
 * in the browser environment.
 */
class TrainerManagerTest {
    
    @Test
    fun createsWithDefaultConfiguration() {
        val manager = TrainerManager()
        
        assertNotNull(manager.getConfig())
        assertEquals(TrainingConfiguration(), manager.getConfig())
    }
    
    @Test
    fun getConfigReturnsCurrentConfiguration() {
        val manager = TrainerManager()
        
        val config = manager.getConfig()
        
        assertEquals(1000, config.actionIntervalMs)
        assertEquals(3000, config.combinationIntervalMs)
        assertEquals(TrainingMode.BOTH, config.trainingMode)
    }
    
    @Test
    fun generatePreviewReturnsCombinationAndFormattedString() {
        val manager = TrainerManager()
        
        val (combo, formatted) = manager.generatePreview()
        
        assertTrue(combo.isNotEmpty())
        assertTrue(formatted.isNotEmpty())
    }
    
    @Test
    fun trainingStartsAsInactive() {
        val manager = TrainerManager()
        
        assertFalse(manager.isTrainingActive())
        assertFalse(manager.isTrainingPaused())
    }
    
    @Test
    fun stopTrainingSetsTrainingToInactive() {
        val manager = TrainerManager()
        
        manager.stopTraining()
        
        assertFalse(manager.isTrainingActive())
    }
    
    @Test
    fun getCompletedCombosReturnsZeroInitially() {
        val manager = TrainerManager()
        
        assertEquals(0, manager.getCompletedCombos())
    }
    
    @Test
    fun getCurrentCombinationReturnsEmptyListInitially() {
        val manager = TrainerManager()
        
        val combo = manager.getCurrentCombination()
        
        assertTrue(combo.isEmpty())
    }
    
    @Test
    fun getProgressReturnsZeroInitially() {
        val manager = TrainerManager()
        
        val progress = manager.getProgress()
        
        assertEquals(0f, progress)
    }
    
    @Test
    fun getCurrentDisplayedActionsReturnsEmptyListInitially() {
        val manager = TrainerManager()
        
        val actions = manager.getCurrentDisplayedActions()
        
        assertTrue(actions.isEmpty())
    }
    
    @Test
    fun callbacksCanBeSet() {
        val manager = TrainerManager()
        var actionCalled = false
        var combinationCalled = false
        var waitingCalled = false
        
        manager.onActionDisplay = { _, _, _ -> actionCalled = true }
        manager.onCombinationComplete = { _ -> combinationCalled = true }
        manager.onWaitingBetweenCombinations = { _ -> waitingCalled = true }
        
        assertNotNull(manager.onActionDisplay)
        assertNotNull(manager.onCombinationComplete)
        assertNotNull(manager.onWaitingBetweenCombinations)
    }
    
    @Test
    fun stopTrainingCanBeCalledMultipleTimes() {
        val manager = TrainerManager()
        
        manager.stopTraining()
        manager.stopTraining()
        manager.stopTraining()
        
        assertFalse(manager.isTrainingActive())
    }
}
