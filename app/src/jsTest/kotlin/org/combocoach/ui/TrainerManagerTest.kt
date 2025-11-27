package org.combocoach.ui

import org.combocoach.domain.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class TrainerManagerTest {
    
    @Test
    fun `creates with default configuration`() {
        val manager = TrainerManager()
        
        assertNotNull(manager.getConfig())
        assertEquals(TrainingConfiguration(), manager.getConfig())
    }
    
    @Test
    fun `getConfig returns current configuration`() {
        val manager = TrainerManager()
        
        val config = manager.getConfig()
        
        assertEquals(1000, config.actionIntervalMs)
        assertEquals(3000, config.combinationIntervalMs)
        assertEquals(TrainingMode.BOTH, config.trainingMode)
    }
    
    @Test
    fun `updateConfiguration updates configuration`() {
        val manager = TrainerManager()
        val newConfig = TrainingConfiguration(
            minActions = 5,
            maxActions = 10,
            trainingMode = TrainingMode.ATTACK_ONLY
        )
        
        manager.updateConfiguration(newConfig)
        
        assertEquals(newConfig, manager.getConfig())
    }
    
    @Test
    fun `generatePreview returns combination and formatted string`() {
        val manager = TrainerManager()
        
        val (combo, formatted) = manager.generatePreview()
        
        assertTrue(combo.isNotEmpty())
        assertTrue(formatted.isNotEmpty())
    }
    
    @Test
    fun `generatePreview respects configuration`() {
        val manager = TrainerManager()
        val config = TrainingConfiguration(
            minActions = 5,
            maxActions = 5,
            trainingMode = TrainingMode.ATTACK_ONLY
        )
        manager.updateConfiguration(config)
        
        val (combo, _) = manager.generatePreview()
        
        assertEquals(5, combo.size)
        assertTrue(combo.all { it.isOffensive() })
    }
    
    @Test
    fun `generatePreview with number notation formats correctly`() {
        val manager = TrainerManager()
        val config = TrainingConfiguration(
            useNumberNotation = true,
            minActions = 3,
            maxActions = 3,
            trainingMode = TrainingMode.ATTACK_ONLY
        )
        manager.updateConfiguration(config)
        
        val (combo, formatted) = manager.generatePreview()
        
        // Should contain numbers and spaces
        assertTrue(formatted.matches(Regex("[1-6D ]+[1-6D]")))
    }
    
    @Test
    fun `training starts as inactive`() {
        val manager = TrainerManager()
        
        assertFalse(manager.isTrainingActive())
        assertFalse(manager.isTrainingPaused())
    }
    
    @Test
    fun `stopTraining sets training to inactive`() {
        val manager = TrainerManager()
        
        manager.stopTraining()
        
        assertFalse(manager.isTrainingActive())
    }
    
    @Test
    fun `getCompletedCombos returns zero initially`() {
        val manager = TrainerManager()
        
        assertEquals(0, manager.getCompletedCombos())
    }
    
    @Test
    fun `getCurrentCombination returns empty list initially`() {
        val manager = TrainerManager()
        
        val combo = manager.getCurrentCombination()
        
        assertTrue(combo.isEmpty())
    }
    
    @Test
    fun `getProgress returns zero initially`() {
        val manager = TrainerManager()
        
        val progress = manager.getProgress()
        
        assertEquals(0f, progress)
    }
    
    @Test
    fun `getCurrentDisplayedActions returns empty list initially`() {
        val manager = TrainerManager()
        
        val actions = manager.getCurrentDisplayedActions()
        
        assertTrue(actions.isEmpty())
    }
    
    @Test
    fun `callbacks can be set`() {
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
    fun `updateConfiguration with different training modes`() {
        val manager = TrainerManager()
        
        // Test ATTACK_ONLY
        manager.updateConfiguration(TrainingConfiguration(
            trainingMode = TrainingMode.ATTACK_ONLY,
            minActions = 3,
            maxActions = 3
        ))
        val (attackCombo, _) = manager.generatePreview()
        assertTrue(attackCombo.all { it.isOffensive() })
        
        // Test DEFENSE_ONLY
        manager.updateConfiguration(TrainingConfiguration(
            trainingMode = TrainingMode.DEFENSE_ONLY,
            minActions = 3,
            maxActions = 3
        ))
        val (defenseCombo, _) = manager.generatePreview()
        assertTrue(defenseCombo.all { it.isDefensive() })
    }
    
    @Test
    fun `updateConfiguration with different stances`() {
        val manager = TrainerManager()
        
        manager.updateConfiguration(TrainingConfiguration(stance = Stance.ORTHODOX))
        assertEquals(Stance.ORTHODOX, manager.getConfig().stance)
        
        manager.updateConfiguration(TrainingConfiguration(stance = Stance.SOUTHPAW))
        assertEquals(Stance.SOUTHPAW, manager.getConfig().stance)
    }
    
    @Test
    fun `updateConfiguration with different intervals`() {
        val manager = TrainerManager()
        
        manager.updateConfiguration(TrainingConfiguration(
            actionIntervalMs = 2000,
            combinationIntervalMs = 5000
        ))
        
        assertEquals(2000, manager.getConfig().actionIntervalMs)
        assertEquals(5000, manager.getConfig().combinationIntervalMs)
    }
    
    @Test
    fun `multiple preview generations produce different results`() {
        val manager = TrainerManager()
        manager.updateConfiguration(TrainingConfiguration(minActions = 5, maxActions = 8))
        
        val previews = List(10) { manager.generatePreview() }
        val formattedStrings = previews.map { it.second }.toSet()
        
        // Due to randomness, we should get some variety
        assertTrue(formattedStrings.size > 1, "Expected variety in generated previews")
    }
    
    @Test
    fun `configuration with min and max actions`() {
        val manager = TrainerManager()
        
        manager.updateConfiguration(TrainingConfiguration(
            minActions = 4,
            maxActions = 6
        ))
        
        // Generate multiple previews to test range
        repeat(10) {
            val (combo, _) = manager.generatePreview()
            assertTrue(combo.size >= 4)
            assertTrue(combo.size <= 6)
        }
    }
    
    @Test
    fun `stopTraining can be called multiple times`() {
        val manager = TrainerManager()
        
        manager.stopTraining()
        manager.stopTraining()
        manager.stopTraining()
        
        assertFalse(manager.isTrainingActive())
    }
}
