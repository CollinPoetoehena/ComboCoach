package org.combocoach.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith

class TrainingConfigurationTest {
    
    @Test
    fun `default configuration has sensible values`() {
        val config = TrainingConfiguration()
        
        assertEquals(1000, config.actionIntervalMs)
        assertEquals(3000, config.combinationIntervalMs)
        assertEquals(TrainingMode.BOTH, config.trainingMode)
        assertEquals(OutputMode.VISUAL, config.outputMode)
        assertTrue(config.useNumberNotation)
        assertEquals(Stance.ORTHODOX, config.stance)
        assertEquals(3, config.minActions)
        assertEquals(8, config.maxActions)
        assertEquals(0.5f, config.offenseRatio)
    }
    
    @Test
    fun `can create custom configuration`() {
        val config = TrainingConfiguration(
            actionIntervalMs = 2000,
            combinationIntervalMs = 5000,
            trainingMode = TrainingMode.ATTACK_ONLY,
            outputMode = OutputMode.AUDIO,
            useNumberNotation = false,
            stance = Stance.SOUTHPAW,
            minActions = 5,
            maxActions = 10,
            offenseRatio = 0.8f
        )
        
        assertEquals(2000, config.actionIntervalMs)
        assertEquals(5000, config.combinationIntervalMs)
        assertEquals(TrainingMode.ATTACK_ONLY, config.trainingMode)
        assertEquals(OutputMode.AUDIO, config.outputMode)
        assertFalse(config.useNumberNotation)
        assertEquals(Stance.SOUTHPAW, config.stance)
        assertEquals(5, config.minActions)
        assertEquals(10, config.maxActions)
        assertEquals(0.8f, config.offenseRatio)
    }
    
    @Test
    fun `rejects negative action interval`() {
        assertFailsWith<IllegalArgumentException> {
            TrainingConfiguration(actionIntervalMs = -100)
        }
    }
    
    @Test
    fun `rejects zero action interval`() {
        assertFailsWith<IllegalArgumentException> {
            TrainingConfiguration(actionIntervalMs = 0)
        }
    }
    
    @Test
    fun `rejects negative combination interval`() {
        assertFailsWith<IllegalArgumentException> {
            TrainingConfiguration(combinationIntervalMs = -100)
        }
    }
    
    @Test
    fun `rejects zero combination interval`() {
        assertFailsWith<IllegalArgumentException> {
            TrainingConfiguration(combinationIntervalMs = 0)
        }
    }
    
    @Test
    fun `rejects zero or negative min actions`() {
        assertFailsWith<IllegalArgumentException> {
            TrainingConfiguration(minActions = 0)
        }
        assertFailsWith<IllegalArgumentException> {
            TrainingConfiguration(minActions = -1)
        }
    }
    
    @Test
    fun `rejects max actions less than min actions`() {
        assertFailsWith<IllegalArgumentException> {
            TrainingConfiguration(minActions = 5, maxActions = 3)
        }
    }
    
    @Test
    fun `allows max actions equal to min actions`() {
        val config = TrainingConfiguration(minActions = 5, maxActions = 5)
        assertEquals(5, config.minActions)
        assertEquals(5, config.maxActions)
    }
    
    @Test
    fun `rejects offense ratio less than zero`() {
        assertFailsWith<IllegalArgumentException> {
            TrainingConfiguration(offenseRatio = -0.1f)
        }
    }
    
    @Test
    fun `rejects offense ratio greater than one`() {
        assertFailsWith<IllegalArgumentException> {
            TrainingConfiguration(offenseRatio = 1.1f)
        }
    }
    
    @Test
    fun `allows offense ratio at boundaries`() {
        val config1 = TrainingConfiguration(offenseRatio = 0f)
        assertEquals(0f, config1.offenseRatio)
        
        val config2 = TrainingConfiguration(offenseRatio = 1f)
        assertEquals(1f, config2.offenseRatio)
    }
    
    @Test
    fun `training mode ATTACK_ONLY includes offense`() {
        assertTrue(TrainingMode.ATTACK_ONLY.includeOffense())
        assertFalse(TrainingMode.ATTACK_ONLY.includeDefense())
    }
    
    @Test
    fun `training mode DEFENSE_ONLY includes defense`() {
        assertFalse(TrainingMode.DEFENSE_ONLY.includeOffense())
        assertTrue(TrainingMode.DEFENSE_ONLY.includeDefense())
    }
    
    @Test
    fun `training mode BOTH includes both offense and defense`() {
        assertTrue(TrainingMode.BOTH.includeOffense())
        assertTrue(TrainingMode.BOTH.includeDefense())
    }
}
