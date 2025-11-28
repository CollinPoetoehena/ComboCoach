package org.combocoach.ui

import org.combocoach.domain.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests for SoundManager.
 */
class SoundManagerTest {
    
    @Test
    fun soundIsEnabledByDefault() {
        // Sound should be enabled by default
        assertTrue(SoundManager.isEnabled(), "Sound should be enabled by default")
    }
    
    @Test
    fun canDisableSound() {
        SoundManager.setEnabled(false)
        assertFalse(SoundManager.isEnabled(), "Sound should be disabled after setEnabled(false)")
        
        // Reset for other tests
        SoundManager.setEnabled(true)
    }
    
    @Test
    fun canEnableSound() {
        SoundManager.setEnabled(false)
        SoundManager.setEnabled(true)
        assertTrue(SoundManager.isEnabled(), "Sound should be enabled after setEnabled(true)")
    }
    
    @Test
    fun defaultVolumeIs30Percent() {
        assertEquals(0.3f, SoundManager.getVolume(), 0.01f, "Default volume should be 0.3 (30%)")
    }
    
    @Test
    fun canSetVolume() {
        SoundManager.setVolume(0.5f)
        assertEquals(0.5f, SoundManager.getVolume(), 0.01f, "Volume should be set to 0.5")
        
        // Reset for other tests
        SoundManager.setVolume(0.3f)
    }
    
    @Test
    fun volumeIsClampedToValidRange() {
        SoundManager.setVolume(1.5f)
        assertEquals(1.0f, SoundManager.getVolume(), 0.01f, "Volume should be clamped to 1.0")
        
        SoundManager.setVolume(-0.5f)
        assertEquals(0.0f, SoundManager.getVolume(), 0.01f, "Volume should be clamped to 0.0")
        
        // Reset for other tests
        SoundManager.setVolume(0.3f)
    }
    
    @Test
    fun speakActionDoesNotThrowErrors() {
        // These methods should not throw errors even if speech synthesis is not available
        val jab = Action.Strike.Jab(Position.HEAD)
        val cross = Action.Strike.Cross(Position.HEAD)
        val slip = Action.Defense(DefensiveMove.SLIP_LEFT)
        
        SoundManager.speakAction(jab)
        SoundManager.speakAction(cross)
        SoundManager.speakAction(slip)
    }
    
    @Test
    fun speakActionDoesNotThrowErrorsWhenDisabled() {
        SoundManager.setEnabled(false)
        
        // Should not throw errors when sound is disabled
        val jab = Action.Strike.Jab(Position.HEAD)
        SoundManager.speakAction(jab)
        
        // Reset for other tests
        SoundManager.setEnabled(true)
    }
    
    @Test
    fun setRateDoesNotThrowErrors() {
        SoundManager.setRate(0.8f)
        SoundManager.setRate(1.5f)
        SoundManager.setRate(1.0f) // Reset
    }
    
    @Test
    fun setPitchDoesNotThrowErrors() {
        SoundManager.setPitch(0.8f)
        SoundManager.setPitch(1.2f)
        SoundManager.setPitch(1.0f) // Reset
    }
}
