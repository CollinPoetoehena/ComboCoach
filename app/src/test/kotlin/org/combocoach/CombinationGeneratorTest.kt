package org.combocoach

import org.combocoach.domain.TrainingConfiguration
import org.combocoach.domain.TrainingMode
import org.combocoach.service.FlowCombinationGenerator
import kotlin.test.Test
import kotlin.test.assertTrue

class CombinationGeneratorTest {
    
    @Test
    fun `generator creates combination within specified range`() {
        val config = TrainingConfiguration(minActions = 3, maxActions = 5)
        val generator = FlowCombinationGenerator(config)
        val combo = generator.generateFlowingCombination()
        
        assertTrue(combo.size in 3..5, "Combination should have 3-5 actions")
    }
    
    @Test
    fun `combinations have proper flow`() {
        val config = TrainingConfiguration(minActions = 5, maxActions = 10)
        val generator = FlowCombinationGenerator(config)
        val combo = generator.generateFlowingCombination()
        
        assertTrue(generator.validateFlow(combo), "Combination should have proper flow")
    }
    
    @Test
    fun `generator respects training mode`() {
        val config = TrainingConfiguration(
            minActions = 5,
            maxActions = 10,
            trainingMode = TrainingMode.ATTACK_ONLY
        )
        val generator = FlowCombinationGenerator(config)
        val combo = generator.generateFlowingCombination()
        
        assertTrue(combo.all { it.isOffensive() }, "All actions should be offensive in ATTACK_ONLY mode")
    }
}
