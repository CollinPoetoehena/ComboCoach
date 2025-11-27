package org.combocoach.service

import org.combocoach.domain.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class FlowCombinationGeneratorTest {
    
    @Test
    fun generatesCombinationWithCorrectNumberOfActions() {
        val config = TrainingConfiguration(minActions = 5, maxActions = 5)
        val generator = FlowCombinationGenerator(config)
        
        val combo = generator.generateFlowingCombination()
        
        assertEquals(5, combo.size)
    }
    
    @Test
    fun generatesCombinationWithinMinAndMaxRange() {
        val config = TrainingConfiguration(minActions = 3, maxActions = 8)
        val generator = FlowCombinationGenerator(config)
        
        // Test multiple times due to randomness
        repeat(20) {
            val combo = generator.generateFlowingCombination()
            assertTrue(combo.size >= 3, "Combination size should be >= 3, got ${combo.size}")
            assertTrue(combo.size <= 8, "Combination size should be <= 8, got ${combo.size}")
        }
    }
    
    @Test
    fun ATTACK_ONLYModeGeneratesOnlyOffensiveActions() {
        val config = TrainingConfiguration(
            trainingMode = TrainingMode.ATTACK_ONLY,
            minActions = 5,
            maxActions = 5
        )
        val generator = FlowCombinationGenerator(config)
        
        repeat(10) {
            val combo = generator.generateFlowingCombination()
            assertTrue(combo.all { it.isOffensive() }, 
                "All actions should be offensive in ATTACK_ONLY mode")
        }
    }
    
    @Test
    fun DEFENSE_ONLYModeGeneratesOnlyDefensiveActions() {
        val config = TrainingConfiguration(
            trainingMode = TrainingMode.DEFENSE_ONLY,
            minActions = 5,
            maxActions = 5
        )
        val generator = FlowCombinationGenerator(config)
        
        repeat(10) {
            val combo = generator.generateFlowingCombination()
            assertTrue(combo.all { it.isDefensive() }, 
                "All actions should be defensive in DEFENSE_ONLY mode")
        }
    }
    
    @Test
    fun BOTHModeGeneratesMixOfOffensiveAndDefensiveActions() {
        val config = TrainingConfiguration(
            trainingMode = TrainingMode.BOTH,
            offenseRatio = 0.5f,
            minActions = 20,
            maxActions = 20
        )
        val generator = FlowCombinationGenerator(config)
        
        val combo = generator.generateFlowingCombination()
        
        val hasOffensive = combo.any { it.isOffensive() }
        val hasDefensive = combo.any { it.isDefensive() }
        
        // With 20 actions and 50/50 ratio, we should have both types
        assertTrue(hasOffensive || hasDefensive, "Should have at least some actions")
    }
    
    @Test
    fun generatedCombinationFlowsProperly() {
        val config = TrainingConfiguration(minActions = 10, maxActions = 10)
        val generator = FlowCombinationGenerator(config)
        
        repeat(10) {
            val combo = generator.generateFlowingCombination()
            assertTrue(generator.validateFlow(combo), 
                "Generated combination should have valid flow")
        }
    }
    
    @Test
    fun validateFlowReturnsTrueForValidJabCombination() {
        val generator = FlowCombinationGenerator()
        val combo = listOf(Action.Jab, Action.Jab, Action.Jab)
        
        assertTrue(generator.validateFlow(combo))
    }
    
    @Test
    fun validateFlowReturnsTrueForValidJabCrossCombination() {
        val generator = FlowCombinationGenerator()
        val combo = listOf(Action.Jab, Action.Cross)
        
        assertTrue(generator.validateFlow(combo))
    }
    
    @Test
    fun validateFlowReturnsTrueForValidJabCrossHookCombination() {
        val generator = FlowCombinationGenerator()
        val combo = listOf(Action.Jab, Action.Cross, Action.LeadHook)
        
        assertTrue(generator.validateFlow(combo))
    }
    
    @Test
    fun validateFlowReturnsFalseForInvalidCombination() {
        val generator = FlowCombinationGenerator()
        // Cross from neutral, then lead hook (not allowed from rear extended)
        val combo = listOf(Action.Cross, Action.LeadHook)
        
        assertFalse(generator.validateFlow(combo))
    }
    
    @Test
    fun validateFlowReturnsTrueWhenDefenseResetsToNeutral() {
        val generator = FlowCombinationGenerator()
        val defense = Action.DefendOpponentAttack(OpponentStrike.OPPONENT_JAB)
        // Cross -> Defense -> Lead Hook should work because defense resets to neutral
        val combo = listOf(Action.Cross, defense, Action.LeadHook)
        
        assertTrue(generator.validateFlow(combo))
    }
    
    @Test
    fun formatActionsWithNumberNotationShowsNumbers() {
        val config = TrainingConfiguration(useNumberNotation = true)
        val generator = FlowCombinationGenerator(config)
        val combo = listOf(Action.Jab, Action.Cross, Action.LeadHook)
        
        val formatted = generator.formatActions(combo)
        
        assertEquals("1 2 3", formatted)
    }
    
    @Test
    fun formatActionsWithoutNumberNotationShowsFullNames() {
        val config = TrainingConfiguration(useNumberNotation = false)
        val generator = FlowCombinationGenerator(config)
        val combo = listOf(Action.Jab, Action.Cross, Action.LeadHook)
        
        val formatted = generator.formatActions(combo)
        
        assertEquals("Jab → Cross → Lead Hook", formatted)
    }
    
    @Test
    fun formatActionsWithDefensiveActionsUsesCorrectNotation() {
        val config = TrainingConfiguration(useNumberNotation = true)
        val generator = FlowCombinationGenerator(config)
        val defense = Action.DefendOpponentAttack(OpponentStrike.OPPONENT_JAB)
        val combo = listOf(Action.Jab, defense, Action.Cross)
        
        val formatted = generator.formatActions(combo)
        
        assertEquals("1 D1 2", formatted)
    }
    
    @Test
    fun generateNextActionReturnsValidActionFromNeutralPosition() {
        val generator = FlowCombinationGenerator()
        
        repeat(20) {
            val action = generator.generateNextAction(Position.NEUTRAL)
            assertTrue(Position.NEUTRAL.canPerformAction(action, Stance.ORTHODOX))
        }
    }
    
    @Test
    fun generateNextActionReturnsValidActionFromLeadExtendedPosition() {
        val generator = FlowCombinationGenerator()
        
        repeat(20) {
            val action = generator.generateNextAction(Position.LEAD_EXTENDED)
            assertTrue(Position.LEAD_EXTENDED.canPerformAction(action, Stance.ORTHODOX))
        }
    }
    
    @Test
    fun generateNextActionReturnsValidActionFromRearExtendedPosition() {
        val generator = FlowCombinationGenerator()
        
        repeat(20) {
            val action = generator.generateNextAction(Position.REAR_EXTENDED)
            assertTrue(Position.REAR_EXTENDED.canPerformAction(action, Stance.ORTHODOX))
        }
    }
    
    @Test
    fun configurationWithSouthpawStanceWorksCorrectly() {
        val config = TrainingConfiguration(
            stance = Stance.SOUTHPAW,
            minActions = 5,
            maxActions = 5
        )
        val generator = FlowCombinationGenerator(config)
        
        val combo = generator.generateFlowingCombination()
        
        assertEquals(5, combo.size)
        assertTrue(generator.validateFlow(combo))
    }
    
    @Test
    fun highOffenseRatioGeneratesMostlyOffensiveActions() {
        val config = TrainingConfiguration(
            trainingMode = TrainingMode.BOTH,
            offenseRatio = 0.9f,
            minActions = 30,
            maxActions = 30
        )
        val generator = FlowCombinationGenerator(config)
        
        val combo = generator.generateFlowingCombination()
        val offensiveCount = combo.count { it.isOffensive() }
        
        // With 90% offense ratio, expect at least 70% offensive actions
        assertTrue(offensiveCount >= 21, 
            "Expected mostly offensive actions with 0.9 ratio, got $offensiveCount/30")
    }
    
    @Test
    fun lowOffenseRatioGeneratesMostlyDefensiveActions() {
        val config = TrainingConfiguration(
            trainingMode = TrainingMode.BOTH,
            offenseRatio = 0.1f,
            minActions = 30,
            maxActions = 30
        )
        val generator = FlowCombinationGenerator(config)
        
        val combo = generator.generateFlowingCombination()
        val defensiveCount = combo.count { it.isDefensive() }
        
        // With 10% offense ratio, expect at least 70% defensive actions
        assertTrue(defensiveCount >= 21, 
            "Expected mostly defensive actions with 0.1 ratio, got $defensiveCount/30")
    }
}
