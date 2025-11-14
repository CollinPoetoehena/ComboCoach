package org.combocoach

import org.combocoach.service.CombinationGenerator
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class CombinationGeneratorTest {
    
    @Test
    fun `generator creates combination within specified range`() {
        val generator = CombinationGenerator(minElements = 3, maxElements = 5)
        val combo = generator.generateCombination()
        
        assertTrue(combo.elements.size in 3..5, "Combination should have 3-5 elements")
    }
    
    @Test
    fun `generator creates multiple combinations`() {
        val generator = CombinationGenerator()
        val combos = generator.generateCombinations(10)
        
        assertEquals(10, combos.size, "Should generate 10 combinations")
    }
    
    @Test
    fun `combinations have unique IDs`() {
        val generator = CombinationGenerator()
        val combos = generator.generateCombinations(5)
        val ids = combos.map { it.id }.toSet()
        
        assertEquals(5, ids.size, "All combinations should have unique IDs")
    }
}
