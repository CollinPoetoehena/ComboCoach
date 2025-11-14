package org.combocoach.ui.components

import kotlinx.browser.document
import org.combocoach.domain.Action
import org.combocoach.domain.TrainingConfiguration
import org.combocoach.ui.components.atoms.ButtonAtom
import org.combocoach.ui.components.molecules.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

/**
 * Main display area component following atomic design principles.
 * Contains two main sections:
 * 1. Control Panel - with configuration, start, stop, and preview buttons
 * 2. Content Area - displays active content (config form, training session, or preview)
 */
object DisplayAreaComponent {
    
    enum class DisplayMode {
        WELCOME,
        CONFIGURATION,
        TRAINING,
        PREVIEW
    }
    
    private var currentMode = DisplayMode.WELCOME
    
    fun create(
        config: TrainingConfiguration,
        onConfiguration: (Event) -> Unit,
        onStart: (Event) -> Unit,
        onStop: (Event) -> Unit,
        onPreview: (Event) -> Unit
    ): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "display-area")
            setAttribute("id", "display-area")
            
            // Control Panel Section
            appendChild(ControlPanelMolecule.create(
                onConfiguration = {
                    showConfiguration(config)
                    onConfiguration(it)
                },
                onStart = onStart,
                onStop = onStop,
                onPreview = onPreview
            ))
            
            // Content Area Section
            appendChild(createContentArea())
        } as HTMLElement
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
    fun showConfiguration(config: TrainingConfiguration) {
        currentMode = DisplayMode.CONFIGURATION
        val contentArea = document.getElementById("content-area")
        contentArea?.innerHTML = ""
        contentArea?.appendChild(ConfigFormMolecule.create(config))
        
        // Setup listeners after DOM is created
        ConfigFormMolecule.setupModeSelectListener()
    }
    
    /**
     * Show training session with stats and action display
     */
    fun showTrainingSession() {
        currentMode = DisplayMode.TRAINING
        val contentArea = document.getElementById("content-area")
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
        ButtonAtom.setEnabled("start-btn", false)
    }
    
    /**
     * Show stopped message
     */
    fun showStopped() {
        currentMode = DisplayMode.WELCOME
        val contentArea = document.getElementById("content-area")
        contentArea?.innerHTML = """
            <div class="welcome-message">
                <p>⏹️ Training stopped</p>
                <p class="hint">Click "Start Training" to begin again</p>
            </div>
        """
        ButtonAtom.setEnabled("start-btn", true)
    }
    
    /**
     * Show combo preview
     */
    fun showPreview(combo: List<Action>, formatted: String) {
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
}
