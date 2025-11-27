package org.combocoach.ui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertNotNull

class TrainingStateManagerTest {
    
    @Test
    fun startsInIDLEState() {
        val manager = TrainingStateManager()
        
        assertEquals(TrainingStateManager.State.IDLE, manager.getState())
        assertTrue(manager.isIdle())
        assertFalse(manager.isRunning())
        assertFalse(manager.isPaused())
    }
    
    @Test
    fun startsInWELCOMEDisplayMode() {
        val manager = TrainingStateManager()
        
        assertEquals(TrainingStateManager.DisplayMode.WELCOME, manager.getDisplayMode())
    }
    
    @Test
    fun setStateChangesState() {
        val manager = TrainingStateManager()
        
        manager.setState(TrainingStateManager.State.RUNNING)
        assertEquals(TrainingStateManager.State.RUNNING, manager.getState())
        assertTrue(manager.isRunning())
        
        manager.setState(TrainingStateManager.State.PAUSED)
        assertEquals(TrainingStateManager.State.PAUSED, manager.getState())
        assertTrue(manager.isPaused())
        
        manager.setState(TrainingStateManager.State.IDLE)
        assertEquals(TrainingStateManager.State.IDLE, manager.getState())
        assertTrue(manager.isIdle())
    }
    
    @Test
    fun setDisplayModeChangesDisplayMode() {
        val manager = TrainingStateManager()
        
        manager.setDisplayMode(TrainingStateManager.DisplayMode.CONFIGURATION)
        assertEquals(TrainingStateManager.DisplayMode.CONFIGURATION, manager.getDisplayMode())
        
        manager.setDisplayMode(TrainingStateManager.DisplayMode.TRAINING)
        assertEquals(TrainingStateManager.DisplayMode.TRAINING, manager.getDisplayMode())
        
        manager.setDisplayMode(TrainingStateManager.DisplayMode.PREVIEW)
        assertEquals(TrainingStateManager.DisplayMode.PREVIEW, manager.getDisplayMode())
        
        manager.setDisplayMode(TrainingStateManager.DisplayMode.STRIKE_LEGEND)
        assertEquals(TrainingStateManager.DisplayMode.STRIKE_LEGEND, manager.getDisplayMode())
    }
    
    @Test
    fun isIdleReturnsTrueOnlyWhenStateIsIDLE() {
        val manager = TrainingStateManager()
        
        manager.setState(TrainingStateManager.State.IDLE)
        assertTrue(manager.isIdle())
        
        manager.setState(TrainingStateManager.State.RUNNING)
        assertFalse(manager.isIdle())
        
        manager.setState(TrainingStateManager.State.PAUSED)
        assertFalse(manager.isIdle())
    }
    
    @Test
    fun isRunningReturnsTrueOnlyWhenStateIsRUNNING() {
        val manager = TrainingStateManager()
        
        manager.setState(TrainingStateManager.State.RUNNING)
        assertTrue(manager.isRunning())
        
        manager.setState(TrainingStateManager.State.IDLE)
        assertFalse(manager.isRunning())
        
        manager.setState(TrainingStateManager.State.PAUSED)
        assertFalse(manager.isRunning())
    }
    
    @Test
    fun isPausedReturnsTrueOnlyWhenStateIsPAUSED() {
        val manager = TrainingStateManager()
        
        manager.setState(TrainingStateManager.State.PAUSED)
        assertTrue(manager.isPaused())
        
        manager.setState(TrainingStateManager.State.IDLE)
        assertFalse(manager.isPaused())
        
        manager.setState(TrainingStateManager.State.RUNNING)
        assertFalse(manager.isPaused())
    }
    
    @Test
    fun storeModeBeforeInfoStoresCurrentDisplayMode() {
        val manager = TrainingStateManager()
        
        manager.setDisplayMode(TrainingStateManager.DisplayMode.TRAINING)
        manager.storeModeBeforeInfo()
        
        assertEquals(TrainingStateManager.DisplayMode.TRAINING, manager.getModeBeforeInfo())
    }
    
    @Test
    fun storeModeBeforeInfoOnlyStoresFirstCall() {
        val manager = TrainingStateManager()
        
        manager.setDisplayMode(TrainingStateManager.DisplayMode.TRAINING)
        manager.storeModeBeforeInfo()
        
        manager.setDisplayMode(TrainingStateManager.DisplayMode.CONFIGURATION)
        manager.storeModeBeforeInfo() // Should not overwrite
        
        assertEquals(TrainingStateManager.DisplayMode.TRAINING, manager.getModeBeforeInfo())
    }
    
    @Test
    fun clearModeBeforeInfoClearsStoredMode() {
        val manager = TrainingStateManager()
        
        manager.setDisplayMode(TrainingStateManager.DisplayMode.TRAINING)
        manager.storeModeBeforeInfo()
        assertNotNull(manager.getModeBeforeInfo())
        
        manager.clearModeBeforeInfo()
        assertNull(manager.getModeBeforeInfo())
    }
    
    @Test
    fun getModeBeforeInfoReturnsNullInitially() {
        val manager = TrainingStateManager()
        
        assertNull(manager.getModeBeforeInfo())
    }
    
    @Test
    fun setRestoreCallbackStoresCallback() {
        val manager = TrainingStateManager()
        var callbackCalled = false
        val callback = { callbackCalled = true }
        
        manager.setRestoreCallback(callback)
        
        assertNotNull(manager.getRestoreCallback())
        manager.getRestoreCallback()?.invoke()
        assertTrue(callbackCalled)
    }
    
    @Test
    fun clearRestoreCallbackClearsCallback() {
        val manager = TrainingStateManager()
        val callback = { }
        
        manager.setRestoreCallback(callback)
        assertNotNull(manager.getRestoreCallback())
        
        manager.clearRestoreCallback()
        assertNull(manager.getRestoreCallback())
    }
    
    @Test
    fun getRestoreCallbackReturnsNullInitially() {
        val manager = TrainingStateManager()
        
        assertNull(manager.getRestoreCallback())
    }
    
    @Test
    fun resetRestoresInitialState() {
        val manager = TrainingStateManager()
        
        // Change everything
        manager.setState(TrainingStateManager.State.RUNNING)
        manager.setDisplayMode(TrainingStateManager.DisplayMode.TRAINING)
        manager.storeModeBeforeInfo()
        manager.setRestoreCallback { }
        
        // Reset
        manager.reset()
        
        // Verify everything is reset
        assertEquals(TrainingStateManager.State.IDLE, manager.getState())
        assertEquals(TrainingStateManager.DisplayMode.WELCOME, manager.getDisplayMode())
        assertNull(manager.getModeBeforeInfo())
        assertNull(manager.getRestoreCallback())
    }
    
    @Test
    fun multipleStateTransitionsWorkCorrectly() {
        val manager = TrainingStateManager()
        
        manager.setState(TrainingStateManager.State.RUNNING)
        assertTrue(manager.isRunning())
        
        manager.setState(TrainingStateManager.State.PAUSED)
        assertTrue(manager.isPaused())
        
        manager.setState(TrainingStateManager.State.RUNNING)
        assertTrue(manager.isRunning())
        
        manager.setState(TrainingStateManager.State.IDLE)
        assertTrue(manager.isIdle())
    }
    
    @Test
    fun allDisplayModesAreAccessible() {
        val manager = TrainingStateManager()
        
        val modes = listOf(
            TrainingStateManager.DisplayMode.WELCOME,
            TrainingStateManager.DisplayMode.CONFIGURATION,
            TrainingStateManager.DisplayMode.TRAINING,
            TrainingStateManager.DisplayMode.PREVIEW,
            TrainingStateManager.DisplayMode.STRIKE_LEGEND
        )
        
        modes.forEach { mode ->
            manager.setDisplayMode(mode)
            assertEquals(mode, manager.getDisplayMode())
        }
    }
}
