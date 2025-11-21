package org.combocoach.ui.components

import kotlinx.browser.document
import org.combocoach.domain.Action
import org.combocoach.domain.TrainingConfiguration
import org.combocoach.ui.TrainingStateManager
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
    
    private val stateManager = TrainingStateManager()
    private var onApplyCallback: (() -> Unit)? = null
    
    fun getStateManager(): TrainingStateManager = stateManager
    
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
                onConfiguration = onConfiguration,
                onStart = onStart,
                onResume = onResume,
                onPause = onPause,
                onStop = onStop,
                onPreview = onPreview,
                onStrikeLegend = onStrikeLegend
            ))

            // Content Area Section
            appendChild(createContentArea())
        } as HTMLElement
    }
    
    private fun createContentArea(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "content-area")
            setAttribute("id", "content-area")
            innerHTML = """
                <div class="welcome-message">
                    <p>👊 Click "Config" to adjust settings, or "Start" to begin training!</p>
                </div>
            """
        } as HTMLElement
    }
    
    fun showStrikeLegend() {
        stateManager.setDisplayMode(TrainingStateManager.DisplayMode.STRIKE_LEGEND)
        val contentArea = document.getElementById("content-area")
        contentArea?.innerHTML = ""
        contentArea?.appendChild(StrikeLegendMolecule.create())
    }
    
    fun showWelcome() {
        stateManager.setDisplayMode(TrainingStateManager.DisplayMode.WELCOME)
        updateContentArea("""
            <div class="welcome-message">
                <p>👊 Click "Configuration" to adjust settings, or "Start Training" to begin!</p>
            </div>
        """)
    }
    
    fun resetTrainingState() {
        stateManager.reset()
        showWelcome()
        updateTrainingButtons()
    }
    
    /**
     * Centralized handler for all info button clicks
     * Handles toggle logic, pause, mode storage, and delegates to specific show method
     */
    fun handleInfoButton(
        targetMode: TrainingStateManager.DisplayMode,
        isTrainingActive: Boolean,
        onPauseTraining: () -> Unit,
        onRestoreTraining: (() -> Unit)?,
        showView: () -> Unit
    ) {
        if (stateManager.getDisplayMode() == targetMode) {
            restorePreviousMode()
            return
        }
        
        stateManager.storeModeBeforeInfo()
        
        if (isTrainingActive) {
            onPauseTraining()
            stateManager.setState(TrainingStateManager.State.PAUSED)
            updateTrainingButtons()
            // Set restore callback 
            onRestoreTraining?.let { stateManager.setRestoreCallback(it) }
        } else if (stateManager.isPaused() && stateManager.getRestoreCallback() == null) {
            // Already paused but callback was consumed (now it is null) - restore it
            onRestoreTraining?.let { stateManager.setRestoreCallback(it) }
        }
        
        showView()
    }
    
    fun restorePreviousMode() {
        when (stateManager.getModeBeforeInfo()) {
            TrainingStateManager.DisplayMode.TRAINING -> {
                if (stateManager.isPaused()) {
                    showPausedAfterInfo()
                } else {
                    restoreTrainingView()
                }
            }
            else -> showWelcome()
        }
        stateManager.clearModeBeforeInfo()
    }
    
    fun showConfiguration(config: TrainingConfiguration, onApply: () -> Unit) {
        stateManager.setDisplayMode(TrainingStateManager.DisplayMode.CONFIGURATION)
        val contentArea = document.getElementById("content-area")
        contentArea?.innerHTML = ""
        contentArea?.appendChild(ConfigFormMolecule.create(config))
        
        ConfigFormMolecule.setupModeSelectListener()
        
        document.getElementById("apply-config-btn")?.addEventListener("click", {
            onApply()
            showWelcome()
        })
    }
    
    fun showTrainingSession() {
        stateManager.setDisplayMode(TrainingStateManager.DisplayMode.TRAINING)
        stateManager.setState(TrainingStateManager.State.RUNNING)
        restoreTrainingView()
    }
    
    fun showPaused() {
        stateManager.setState(TrainingStateManager.State.PAUSED)
        val currentActionDisplay = document.getElementById("current-action-display")
        currentActionDisplay?.innerHTML = """
            <div class="paused-message">
                <h2>⏸️ Training Paused</h2>
                <p>Click "Resume" to continue</p>
            </div>
        """
        updateTrainingButtons()
    }
    
    fun showResumed() {
        stateManager.setDisplayMode(TrainingStateManager.DisplayMode.TRAINING)
        stateManager.setState(TrainingStateManager.State.RUNNING)
        restoreTrainingView()
    }
    
    fun showStopped() {
        stateManager.reset()
        updateContentArea("""
            <div class="welcome-message">
                <p>⏹️ Training stopped</p>
                <p class="hint">Click "Start" to begin again</p>
            </div>
        """)
        updateTrainingButtons()
    }
    
    fun showPreview(combo: List<Action>, formatted: String) {
        stateManager.setDisplayMode(TrainingStateManager.DisplayMode.PREVIEW)
        val contentArea = document.getElementById("content-area")
        contentArea?.innerHTML = ""
        contentArea?.appendChild(PreviewDisplayMolecule.create(combo, formatted))
    }
    
    fun displayAction(action: Action, index: Int, total: Int) {
        val currentActionDisplay = document.getElementById("current-action-display")
        currentActionDisplay?.innerHTML = ""
        currentActionDisplay?.appendChild(ActionCardMolecule.create(action, index, total))
        updateProgressBar(index, total)
    }
    
    fun updateCombinationPreview(actions: List<Action>, config: TrainingConfiguration) {
        val preview = document.getElementById("combination-preview")
        val formatted = if (config.useNumberNotation) {
            actions.joinToString(" ") { it.toNumber() }
        } else {
            actions.joinToString(" → ") { it.displayName() }
        }
        preview?.innerHTML = """<div class="preview-text">Combination: $formatted</div>"""
    }
    
    fun setProgressBar(progress: Float) {
        val percentage = (progress * 100).toInt()
        document.getElementById("progress-bar")?.setAttribute("style", "width: $percentage%")
    }
    
    fun updateCompletedCombos(comboNumber: Int) {
        TrainingStatsMolecule.updateCompletedCombos(comboNumber)
    }
    
    fun showWaitingMessage(secondsRemaining: Int) {
        val currentActionDisplay = document.getElementById("current-action-display")
        currentActionDisplay?.innerHTML = ""
        currentActionDisplay?.appendChild(ActionCardMolecule.showWaiting(secondsRemaining))
        resetProgressBar()
    }
    
    fun readConfiguration(): TrainingConfiguration {
        return ConfigFormMolecule.readConfiguration()
    }
    
    private fun showPausedAfterInfo() {
        stateManager.setDisplayMode(TrainingStateManager.DisplayMode.TRAINING)
        
        val restoreCallback = stateManager.getRestoreCallback()
        if (restoreCallback != null) {
            restoreCallback()
            stateManager.clearRestoreCallback()
        } else {
            restoreTrainingView()
            showPaused()
        }
    }
    
    private fun restoreTrainingView() {
        val contentArea = document.getElementById("content-area")
        
        if (contentArea?.querySelector(".training-container") != null) {
            updateTrainingButtons()
            return
        }
        
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
    
    private fun updateContentArea(html: String) {
        document.getElementById("content-area")?.innerHTML = html
    }
    
    private fun updateProgressBar(index: Int, total: Int) {
        val progress = (index.toFloat() / total.toFloat() * 100).toInt()
        document.getElementById("progress-bar")?.setAttribute("style", "width: $progress%")
    }
    
    private fun resetProgressBar() {
        document.getElementById("progress-bar")?.setAttribute("style", "width: 0%")
    }
    
    private fun updateTrainingButtons() {
        val buttons = mapOf(
            "start-btn" to document.getElementById("start-btn") as? HTMLElement,
            "resume-btn" to document.getElementById("resume-btn") as? HTMLElement,
            "pause-btn" to document.getElementById("pause-btn") as? HTMLElement,
            "stop-btn" to document.getElementById("stop-btn") as? HTMLElement
        )
        
        when (stateManager.getState()) {
            TrainingStateManager.State.IDLE -> {
                buttons["start-btn"]?.setAttribute("style", "display: inline-block;")
                buttons["resume-btn"]?.setAttribute("style", "display: none;")
                buttons["pause-btn"]?.setAttribute("style", "display: none;")
                buttons["stop-btn"]?.setAttribute("style", "display: none;")
            }
            TrainingStateManager.State.RUNNING -> {
                buttons["start-btn"]?.setAttribute("style", "display: none;")
                buttons["resume-btn"]?.setAttribute("style", "display: none;")
                buttons["pause-btn"]?.setAttribute("style", "display: inline-block;")
                buttons["stop-btn"]?.setAttribute("style", "display: inline-block;")
            }
            TrainingStateManager.State.PAUSED -> {
                buttons["start-btn"]?.setAttribute("style", "display: none;")
                buttons["resume-btn"]?.setAttribute("style", "display: inline-block;")
                buttons["pause-btn"]?.setAttribute("style", "display: none;")
                buttons["stop-btn"]?.setAttribute("style", "display: inline-block;")
            }
        }
    }
}
