package org.combocoach.ui

/**
 * Centralized training state management.
 * Single source of truth for training state, eliminating duplication across components.
 */
class TrainingStateManager {
    
    enum class State {
        IDLE,      // Not training
        RUNNING,   // Training in progress
        PAUSED     // Training paused
    }
    
    enum class DisplayMode {
        WELCOME,
        CONFIGURATION,
        TRAINING,
        PREVIEW,
        STRIKE_LEGEND
    }
    
    private var state = State.IDLE
    private var displayMode = DisplayMode.WELCOME
    private var modeBeforeInfo: DisplayMode? = null // Store mode before showing info views
    private var restoreCallback: (() -> Unit)? = null // Callback to restore training state
     
    fun getState(): State = state
    fun setState(newState: State) {
        state = newState
    }
    
    fun getDisplayMode(): DisplayMode = displayMode
    fun setDisplayMode(mode: DisplayMode) {
        displayMode = mode
    }
    
    fun isIdle(): Boolean = state == State.IDLE
    fun isRunning(): Boolean = state == State.RUNNING
    fun isPaused(): Boolean = state == State.PAUSED
    
    fun storeModeBeforeInfo() {
        if (modeBeforeInfo == null) {
            modeBeforeInfo = displayMode
        }
    }
    
    fun getModeBeforeInfo(): DisplayMode? = modeBeforeInfo
    
    fun clearModeBeforeInfo() {
        modeBeforeInfo = null
    }
    
    fun setRestoreCallback(callback: () -> Unit) {
        restoreCallback = callback
    }
    
    fun getRestoreCallback(): (() -> Unit)? = restoreCallback
    
    fun clearRestoreCallback() {
        restoreCallback = null
    }
    
    fun reset() {
        state = State.IDLE
        displayMode = DisplayMode.WELCOME
        modeBeforeInfo = null
        restoreCallback = null
    }
}
