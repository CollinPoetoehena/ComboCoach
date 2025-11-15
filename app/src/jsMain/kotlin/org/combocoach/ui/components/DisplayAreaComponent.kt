package org.combocoach.ui.components

import kotlinx.browser.document
import org.combocoach.domain.Action
import org.combocoach.domain.TrainingConfiguration
import org.combocoach.ui.atoms.ButtonAtom
import org.combocoach.ui.molecules.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

/**
 * Main display area component following atomic design principles.
 * Contains two main sections:
 * 1. Control Panel - with configuration, start, stop, and preview buttons
 * 2. Content Area - displays active content (config form, training session, or preview, etc.)
 */
object DisplayAreaComponent {
    
    enum class DisplayMode {
        WELCOME,
        CONFIGURATION,
        TRAINING,
        PREVIEW,
        STRIKE_LEGEND
    }
    
    enum class TrainingState {
        IDLE,      // Not training
        RUNNING,   // Training in progress
        PAUSED     // Training paused
    }
    
    private var currentMode = DisplayMode.WELCOME
    private var onApplyCallback: (() -> Unit)? = null
    private var trainingState = TrainingState.IDLE
    private var modeBeforeInfo: DisplayMode? = null  // Store mode before showing info views
    
    fun create(
        config: TrainingConfiguration,
        onConfiguration: (Event) -> Unit,
        onApplyConfig: () -> Unit,
        onStart: (Event) -> Unit,
        onResume: (Event) -> Unit,
        onPause: (Event) -> Unit,
        onStop: (Event) -> Unit,
        onPreview: (Event) -> Unit,
        onStrikeLegend: (Event) -> Unit
    ): HTMLElement {
        // Store the apply callback for later use
        onApplyCallback = onApplyConfig

        return document.createElement("div").apply {
            setAttribute("class", "display-area")
            setAttribute("id", "display-area")

            // Control Panel Section
            appendChild(ControlPanelMolecule.create(
                onConfiguration = {
                    // Toggle: if already showing config, restore previous state
                    if (currentMode == DisplayMode.CONFIGURATION) {
                        restorePreviousMode()
                    } else {
                        onConfiguration(it)  // Call the app's handler which will call showConfiguration
                    }
                },
                onStart = onStart,
                onResume = onResume,
                onPause = onPause,
                onStop = onStop,
                onPreview = {
                    // Toggle: if already showing preview, restore previous state
                    if (currentMode == DisplayMode.PREVIEW) {
                        restorePreviousMode()
                    } else {
                        onPreview(it)
                    }
                },
                onStrikeLegend = {
                    // Toggle: if already showing legend, restore previous state
                    if (currentMode == DisplayMode.STRIKE_LEGEND) {
                        restorePreviousMode()
                    } else {
                        onStrikeLegend(it)
                    }
                }
            ))

            // Content Area Section
            appendChild(createContentArea())
        } as HTMLElement
    }

    /**
     * Show the strike notation legend/reference in the content area
     */
    fun showStrikeLegend() {
        modeBeforeInfo = currentMode  // Store previous mode
        currentMode = DisplayMode.STRIKE_LEGEND
        val contentArea = document.getElementById("content-area")
        contentArea?.innerHTML = ""
        contentArea?.appendChild(StrikeLegendMolecule.create())
    }
    
    /**
     * Show welcome message
     */
    fun showWelcome() {
        currentMode = DisplayMode.WELCOME
        val contentArea = document.getElementById("content-area")
        contentArea?.innerHTML = """
            <div class="welcome-message">
                <p>👊 Click "Configuration" to adjust settings, or "Start Training" to begin!</p>
            </div>
        """
    }
    
    /**
     * Set training state and update button visibility
     */
    fun setTrainingState(state: TrainingState) {
        trainingState = state
        updateTrainingButtons()
    }
    
    /**
     * Get current display mode
     */
    fun getCurrentMode(): DisplayMode = currentMode
    
    /**
     * Restore the previous mode before info view was shown
     */
    private fun restorePreviousMode() {
        when (modeBeforeInfo) {
            DisplayMode.TRAINING -> {
                // If we were in training and paused, show the paused state
                if (trainingState == TrainingState.PAUSED) {
                    showPausedAfterInfo()
                } else {
                    // Should not happen, but restore training view just in case
                    restoreTrainingView()
                }
            }
            else -> showWelcome()
        }
        modeBeforeInfo = null
    }
    
    /**
     * Centralized method to pause training and show paused state
     * Used by both Pause button and info buttons during active training
     */
    fun pauseTraining() {
        trainingState = TrainingState.PAUSED
        val currentActionDisplay = document.getElementById("current-action-display")
        currentActionDisplay?.innerHTML = """
            <div class="paused-message">
                <h2>⏸️ Training Paused</h2>
                <p>Click "Resume" to continue</p>
            </div>
        """
        updateTrainingButtons()
    }
    
    /**
     * Show paused state after returning from info view
     * Different from pauseTraining() as we're already paused, just need to restore the UI
     */
    private fun showPausedAfterInfo() {
        currentMode = DisplayMode.TRAINING
        restoreTrainingView()
        // Update the current action display to show paused message
        val currentActionDisplay = document.getElementById("current-action-display")
        currentActionDisplay?.innerHTML = """
            <div class="paused-message">
                <h2>⏸️ Training Paused</h2>
                <p>Click "Resume" to continue</p>
            </div>
        """
    }
    
    private fun createContentArea(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "content-area")
            setAttribute("id", "content-area")
            
            // Initial welcome message
            innerHTML = """
                <div class="welcome-message">
                    <p>👊 Click "Configuration" to adjust settings, or "Start Training" to begin!</p>
                </div>
            """
        } as HTMLElement
    }
    
    /**
     * Show configuration form in the content area
     */
    fun showConfiguration(config: TrainingConfiguration, onApply: () -> Unit) {
        modeBeforeInfo = currentMode  // Store previous mode
        currentMode = DisplayMode.CONFIGURATION
        val contentArea = document.getElementById("content-area")
        contentArea?.innerHTML = ""
        contentArea?.appendChild(ConfigFormMolecule.create(config))
        
        // Setup listeners after DOM is created
        ConfigFormMolecule.setupModeSelectListener()
        
        // Attach apply button listener - also close configuration after applying
        document.getElementById("apply-config-btn")?.addEventListener("click", {
            onApply()
            showWelcome()  // Close configuration view after applying
        })
    }
    
    /**
     * Show training session with stats and action display
     */
    fun showTrainingSession() {
        currentMode = DisplayMode.TRAINING
        trainingState = TrainingState.RUNNING
        restoreTrainingView()
    }
    
    /**
     * Restore training view without changing state
     */
    private fun restoreTrainingView() {
        val contentArea = document.getElementById("content-area")
        
        // Check if training container already exists (e.g., when resuming from pause)
        val existingContainer = contentArea?.querySelector(".training-container")
        if (existingContainer != null) {
            // Container already exists, just update buttons
            updateTrainingButtons()
            return
        }
        
        // No existing container, create fresh one
        contentArea?.innerHTML = ""
        
        val trainingContainer = document.createElement("div").apply {
            setAttribute("class", "training-container")
            
            appendChild(TrainingStatsMolecule.create())
            appendChild(document.createElement("div").apply {
                setAttribute("id", "current-action-display")
                setAttribute("class", "current-action-display")
            })
            appendChild(document.createElement("div").apply {
                setAttribute("class", "progress-bar-container")
                setAttribute("id", "progress-bar-container")
                innerHTML = """<div id="progress-bar" class="progress-bar"></div>"""
            })
            appendChild(document.createElement("div").apply {
                setAttribute("id", "combination-preview")
                setAttribute("class", "combination-preview")
            })
        } as HTMLElement
        
        contentArea?.appendChild(trainingContainer)
        updateTrainingButtons()
    }
    
    /**
     * Show paused state (delegates to centralized pause method)
     */
    fun showPaused() {
        pauseTraining()
    }
    
    /**
     * Resume from paused state
     */
    fun showResumed() {
        currentMode = DisplayMode.TRAINING
        trainingState = TrainingState.RUNNING
        restoreTrainingView()
    }
    
    /**
     * Show stopped message
     */
    fun showStopped() {
        currentMode = DisplayMode.WELCOME
        trainingState = TrainingState.IDLE
        val contentArea = document.getElementById("content-area")
        contentArea?.innerHTML = """
            <div class="welcome-message">
                <p>⏹️ Training stopped</p>
                <p class="hint">Click "Start" to begin again</p>
            </div>
        """
        updateTrainingButtons()
    }
    
    /**
     * Show combo preview
     */
    fun showPreview(combo: List<Action>, formatted: String) {
        modeBeforeInfo = currentMode  // Store previous mode
        currentMode = DisplayMode.PREVIEW
        val contentArea = document.getElementById("content-area")
        contentArea?.innerHTML = ""
        contentArea?.appendChild(PreviewDisplayMolecule.create(combo, formatted))
    }
    
    /**
     * Display current action during training
     */
    fun displayAction(action: Action, index: Int, total: Int) {
        val currentActionDisplay = document.getElementById("current-action-display")
        currentActionDisplay?.innerHTML = ""
        currentActionDisplay?.appendChild(ActionCardMolecule.create(action, index, total))
        
        updateProgressBar(index, total)
    }
    
    /**
     * Update combination preview text during training
     */
    fun updateCombinationPreview(actions: List<Action>, config: TrainingConfiguration) {
        val preview = document.getElementById("combination-preview")
        val formattedSoFar = if (config.useNumberNotation) {
            actions.joinToString(" ") { it.toNumber() }
        } else {
            actions.joinToString(" → ") { it.displayName() }
        }
        preview?.innerHTML = """<div class="preview-text">Combination: $formattedSoFar</div>"""
    }
    
    /**
     * Update completed combos counter
     */
    fun updateCompletedCombos(comboNumber: Int) {
        TrainingStatsMolecule.updateCompletedCombos(comboNumber)
    }
    
    /**
     * Show waiting message between combinations
     */
    fun showWaitingMessage(secondsRemaining: Int) {
        val currentActionDisplay = document.getElementById("current-action-display")
        currentActionDisplay?.innerHTML = ""
        currentActionDisplay?.appendChild(ActionCardMolecule.showWaiting(secondsRemaining))
        
        resetProgressBar()
    }
    
    /**
     * Read configuration from the form
     */
    fun readConfiguration(): TrainingConfiguration {
        return ConfigFormMolecule.readConfiguration()
    }
    
    private fun updateProgressBar(index: Int, total: Int) {
        val progress = (index.toFloat() / total.toFloat() * 100).toInt()
        val progressBar = document.getElementById("progress-bar")
        progressBar?.setAttribute("style", "width: $progress%")
    }
    
    private fun resetProgressBar() {
        val progressBar = document.getElementById("progress-bar")
        progressBar?.setAttribute("style", "width: 0%")
    }
    
    /**
     * Update training buttons visibility based on training state
     * - IDLE: Show Start only
     * - RUNNING: Show Pause and Stop
     * - PAUSED: Show Start (resume) and Stop
     */
    private fun updateTrainingButtons() {
        val startBtn = document.getElementById("start-btn") as? HTMLElement
        val resumeBtn = document.getElementById("resume-btn") as? HTMLElement
        val pauseBtn = document.getElementById("pause-btn") as? HTMLElement
        val stopBtn = document.getElementById("stop-btn") as? HTMLElement
        
        when (trainingState) {
            TrainingState.IDLE -> {
                // Show only Start button
                startBtn?.setAttribute("style", "display: inline-block;")
                resumeBtn?.setAttribute("style", "display: none;")
                pauseBtn?.setAttribute("style", "display: none;")
                stopBtn?.setAttribute("style", "display: none;")
            }
            TrainingState.RUNNING -> {
                // Show Pause and Stop buttons
                startBtn?.setAttribute("style", "display: none;")
                resumeBtn?.setAttribute("style", "display: none;")
                pauseBtn?.setAttribute("style", "display: inline-block;")
                stopBtn?.setAttribute("style", "display: inline-block;")
            }
            TrainingState.PAUSED -> {
                // Show Resume and Stop buttons
                startBtn?.setAttribute("style", "display: none;")
                resumeBtn?.setAttribute("style", "display: inline-block;")
                pauseBtn?.setAttribute("style", "display: none;")
                stopBtn?.setAttribute("style", "display: inline-block;")
            }
        }
    }
}
