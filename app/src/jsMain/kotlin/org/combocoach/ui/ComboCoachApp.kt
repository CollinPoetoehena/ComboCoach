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
                onStop = { stopTraining() },
                onPreview = { generatePreview() }
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
        trainerManager.stopTraining()
        DisplayAreaComponent.showConfiguration(trainerManager.getConfig()) { 
            applyConfiguration() 
        }
    }
    
    /**
     * Start interval training
     */
    private fun startTraining() {
        val newConfig = DisplayAreaComponent.readConfiguration()
        trainerManager.updateConfiguration(newConfig)
        DisplayAreaComponent.showTrainingSession()
        trainerManager.startTraining()
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
        val (combo, formatted) = trainerManager.generatePreview()
        DisplayAreaComponent.showPreview(combo, formatted)
    }
}
