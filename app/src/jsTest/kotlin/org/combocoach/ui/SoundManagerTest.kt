package org.combocoach.ui

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
    fun canSetVolume() {
        SoundManager.setVolume(0.5f)
        assertEquals(0.5f, SoundManager.getVolume(), 0.01f, "Volume should be set to 0.5")
        
        // Reset for other tests
        SoundManager.setVolume(0.3f)
    }
}
