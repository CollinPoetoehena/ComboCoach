package org.combocoach.ui

import kotlinx.browser.document
import org.combocoach.ui.components.*
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

/**
 * Main application class that orchestrates all UI components and manages application state.
 * Delegates training logic to TrainerManager for better separation of concerns.
 */
class ComboCoachApp(private val rootElement: Element) {
    private val trainerManager = TrainerManager()
    private var isLegendExpanded = false
    
    init {
        setupTrainerCallbacks()
    }
    
    /**
     * Renders the entire application UI inside the root element
     */
    fun render() {
        // Clear root and build main container (this contains all UI components)
        rootElement.innerHTML = ""
        rootElement.appendChild(createMainContainer())
    }
    
    /**
     * Setup callbacks from trainer manager to update UI
     */
    private fun setupTrainerCallbacks() {
        trainerManager.onActionDisplay = { action, index, total ->
            DisplayAreaComponent.displayAction(action, index, total)
            DisplayAreaComponent.updateCombinationPreview(
                trainerManager.getCurrentDisplayedActions(),
                trainerManager.getConfig()
            )
        }
        
        trainerManager.onCombinationComplete = { comboNumber ->
            DisplayAreaComponent.updateCompletedCombos(comboNumber)
        }
        
        trainerManager.onWaitingBetweenCombinations = { secondsRemaining ->
            DisplayAreaComponent.showWaitingMessage(secondsRemaining)
        }
    }
    
    /**
     * Creates the main container with all UI components
     */
    private fun createMainContainer(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "container")
            appendChild(HeaderComponent.create())
            appendChild(DisplayAreaComponent.create(
                config = trainerManager.getConfig(),
                onConfiguration = { showConfiguration() },
                onApplyConfig = { applyConfiguration() },
                onStart = { startTraining() },
                onResume = { resumeTraining() },
                onPause = { pauseTraining() },
                onStop = { stopTraining() },
                onPreview = { generatePreview() },
                onStrikeLegend = { showStrikeLegend() }
            ))
        } as HTMLElement
    }
    
    /**
     * Apply configuration changes from the UI
     */
    private fun applyConfiguration() {
        trainerManager.stopTraining()
        val newConfig = DisplayAreaComponent.readConfiguration()
        trainerManager.updateConfiguration(newConfig)
    }

    /**
     * Show configuration panel
     */
    private fun showConfiguration() {
        DisplayAreaComponent.handleInfoButton(
            targetMode = DisplayAreaComponent.DisplayMode.CONFIGURATION,
            isTrainingActive = trainerManager.isTrainingActive(),
            onPauseTraining = { trainerManager.pauseTraining() },
            onRestoreTraining = { restoreTrainingWithState() },
            showView = {
                DisplayAreaComponent.showConfiguration(trainerManager.getConfig()) {
                    applyConfiguration()
                }
            }
        )
    }
    
    /**
     * Start interval training
     */
    private fun startTraining() {
        // Configuration is already stored in trainerManager (applied via applyConfiguration)
        // Do NOT read from DOM again as the config form may not be visible (resulting in default config)
        DisplayAreaComponent.showTrainingSession()
        trainerManager.startTraining()
    }
    
    /**
     * Pause training
     */
    private fun pauseTraining() {
        trainerManager.pauseTraining()
        DisplayAreaComponent.showPaused()
    }
    
    /**
     * Resume paused training
     */
    private fun resumeTraining() {
        trainerManager.resumeTraining()
        DisplayAreaComponent.showResumed()
    }
    
    /**
     * Stop training
     */
    private fun stopTraining() {
        trainerManager.stopTraining()
        DisplayAreaComponent.showStopped()
    }
    
    /**
     * Generate and display preview
     */
    private fun generatePreview() {
        DisplayAreaComponent.handleInfoButton(
            targetMode = DisplayAreaComponent.DisplayMode.PREVIEW,
            isTrainingActive = trainerManager.isTrainingActive(),
            onPauseTraining = { trainerManager.pauseTraining() },
            onRestoreTraining = { restoreTrainingWithState() },
            showView = {
                val (combo, formatted) = trainerManager.generatePreview()
                DisplayAreaComponent.showPreview(combo, formatted)
            }
        )
    }
    
    /**
     * Show strike notation legend
     */
    private fun showStrikeLegend() {
        DisplayAreaComponent.handleInfoButton(
            targetMode = DisplayAreaComponent.DisplayMode.STRIKE_LEGEND,
            isTrainingActive = trainerManager.isTrainingActive(),
            onPauseTraining = { trainerManager.pauseTraining() },
            onRestoreTraining = { restoreTrainingWithState() },
            showView = { DisplayAreaComponent.showStrikeLegend() }
        )
    }
    
    /**
     * Restore training view with current state (actions, combo, stats)
     */
    private fun restoreTrainingWithState() {
        // Recreate the training container
        DisplayAreaComponent.showTrainingSession()
        
        // Restore completed combos count
        val completedCombos = trainerManager.getCompletedCombos()
        DisplayAreaComponent.updateCompletedCombos(completedCombos)
        
        // Restore the combination preview (show only actions displayed so far, not full combo)
        val displayedActions = trainerManager.getCurrentDisplayedActions()
        if (displayedActions.isNotEmpty()) {
            DisplayAreaComponent.updateCombinationPreview(
                displayedActions,
                trainerManager.getConfig()
            )
        }
        
        // Restore progress bar
        val progress = trainerManager.getProgress()
        DisplayAreaComponent.setProgressBar(progress)
        
        // Show paused message
        DisplayAreaComponent.showPaused()
    }
}
