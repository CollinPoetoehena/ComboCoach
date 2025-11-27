package org.combocoach.service

import org.combocoach.domain.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class ComboTrainerTest {
    
    @Test
    fun createsTrainerWithDefaultConfiguration() {
        val trainer = ComboTrainer()
        
        assertNotNull(trainer.config)
        assertEquals(TrainingConfiguration(), trainer.config)
    }
    
    @Test
    fun createsTrainerWithCustomConfiguration() {
        val config = TrainingConfiguration(
            minActions = 5,
            maxActions = 10,
            trainingMode = TrainingMode.ATTACK_ONLY
        )
        val trainer = ComboTrainer(config)
        
        assertEquals(config, trainer.config)
    }
    
    @Test
    fun generateFlowingComboReturnsValidCombination() {
        val trainer = ComboTrainer(TrainingConfiguration(minActions = 3, maxActions = 8))
        
        val combo = trainer.generateFlowingCombo()
        
        assertTrue(combo.size >= 3)
        assertTrue(combo.size <= 8)
    }
    
    @Test
    fun generateFlowingComboRespectsTrainingMode() {
        val trainer = ComboTrainer(TrainingConfiguration(
            trainingMode = TrainingMode.ATTACK_ONLY,
            minActions = 5,
            maxActions = 5
        ))
        
        val combo = trainer.generateFlowingCombo()
        
        assertTrue(combo.all { it.isOffensive() })
    }
    
    @Test
    fun formatCombinationFormatsActionsCorrectly() {
        val trainer = ComboTrainer(TrainingConfiguration(useNumberNotation = true))
        val combo = listOf(Action.Jab, Action.Cross, Action.LeadHook)
        
        val formatted = trainer.formatCombination(combo)
        
        assertEquals("1 2 3", formatted)
    }
    
    @Test
    fun updateConfigurationChangesTrainerConfiguration() {
        val trainer = ComboTrainer()
        val newConfig = TrainingConfiguration(
            minActions = 10,
            maxActions = 15,
            trainingMode = TrainingMode.DEFENSE_ONLY
        )
        
        trainer.updateConfiguration(newConfig)
        
        assertEquals(newConfig, trainer.config)
    }
    
    @Test
    fun updateConfigurationAffectsGeneratedCombinations() {
        val trainer = ComboTrainer(TrainingConfiguration(
            trainingMode = TrainingMode.ATTACK_ONLY,
            minActions = 3,
            maxActions = 3
        ))
        
        // Generate with attack only
        val attackCombo = trainer.generateFlowingCombo()
        assertTrue(attackCombo.all { it.isOffensive() })
        
        // Update to defense only
        trainer.updateConfiguration(TrainingConfiguration(
            trainingMode = TrainingMode.DEFENSE_ONLY,
            minActions = 3,
            maxActions = 3
        ))
        
        val defenseCombo = trainer.generateFlowingCombo()
        assertTrue(defenseCombo.all { it.isDefensive() })
    }
    
    @Test
    fun getIntervalTrainerReturnsIntervalTrainer() {
        val trainer = ComboTrainer()
        
        val intervalTrainer = trainer.getIntervalTrainer()
        
        assertNotNull(intervalTrainer)
    }
    
    @Test
    fun multipleComboGenerationsProduceDifferentResults() {
        val trainer = ComboTrainer(TrainingConfiguration(minActions = 5, maxActions = 8))
        
        val combos = List(10) { trainer.generateFlowingCombo() }
        
        // Check that not all combinations are identical (due to randomness)
        val uniqueCombos = combos.map { it.map { action -> action.toNumber() }.joinToString() }.toSet()
        assertTrue(uniqueCombos.size > 1, "Expected some variety in generated combinations")
    }
}
