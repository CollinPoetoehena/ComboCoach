package org.combocoach.ui

import kotlinx.browser.document
import org.combocoach.domain.*
import org.combocoach.service.ComboTrainer
import org.combocoach.ui.components.*
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

/**
 * Main application class that orchestrates all UI components and manages application state
 */
class ComboCoachApp(private val rootElement: Element) {
    private var config = TrainingConfiguration()
    private val trainer = ComboTrainer(config)
    private var currentDisplayedActions = mutableListOf<Action>()
    private var isLegendExpanded = false
    
    init {
        // Setup interval trainer on initialization
        setupIntervalTrainer()
    }
    
    /**
     * Renders the entire application UI inside the root element
     */
    fun render() {
        // Clear root and build main container (this contains all UI components)
        rootElement.innerHTML = ""
        rootElement.appendChild(createMainContainer())
        
        // Attach event listeners after DOM is created
        attachEventListeners()
    }
    
    /**
     * Attach event listeners to various UI elements in this app
     * 
     * Event listeners for dynamically created elements are attached in their respective 
     * creation methods (otherwise they would not exist yet (cannot attach to non-existing elements))
     */
    private fun attachEventListeners() {
        // Currently no global event listeners needed, however, function is here for future use (already called in render)
    }
    
    /**
     * Setup callbacks for interval trainer to update UI during training
     * These callbacks will be called during interval training sessions
     * and should update the display area accordingly
     */
    private fun setupIntervalTrainer() {
        val intervalTrainer = trainer.getIntervalTrainer()
        
        intervalTrainer.onActionDisplay = { action, index, total, _ ->
            currentDisplayedActions.add(action)
            DisplayAreaComponent.displayAction(action, index, total)
            DisplayAreaComponent.updateCombinationPreview(currentDisplayedActions, config)
        }
        
        intervalTrainer.onCombinationComplete = { comboNumber ->
            DisplayAreaComponent.updateCompletedCombos(comboNumber)
            currentDisplayedActions.clear()
        }
        
        intervalTrainer.onWaitingBetweenCombinations = { secondsRemaining ->
            DisplayAreaComponent.showWaitingMessage(secondsRemaining)
        }
    }
    
    /**
     * Creates the main container with all UI components
     */
    private fun createMainContainer(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "container")
            // Build UI components in the order they are displayed
            appendChild(HeaderComponent.create())
            appendChild(DisplayAreaComponent.create(
                config = config,
                onConfiguration = { showConfiguration() },
                onApplyConfig = { applyConfiguration() },
                onStart = { startIntervalTraining() },
                onStop = { stopTraining() },
                onPreview = { generatePreview() }
            ))
            appendChild(StrikeLegendComponent.create(isLegendExpanded) {
                isLegendExpanded = !isLegendExpanded
                render()
            })
        } as HTMLElement
    }
    
    /**
     * Apply configuration changes from the UI to the trainer instance
     * 
     * The configuration is managed in this central app class to ensure
     * consistency across components and services. So, this is the single 
     * source of truth for the configuration.
     */
    private fun applyConfiguration() {
        // Always stop training when applying new configuration
        trainer.getIntervalTrainer().stop()
        
        // Read new configuration from UI and update the trainer instance
        config = DisplayAreaComponent.readConfiguration()
        console.log("Applying configuration: $config")
        trainer.updateConfiguration(config)
        // Re-setup interval trainer callbacks after configuration update
        setupIntervalTrainer()
        
        // TODO: maybe make a feature later to auto-restart training if it was running
        // can then at the start add trainer.getIntervalTrainer().isActive() and then only stop then
        // if (wasRunning) {
        //     doStartTraining()
        // }
        
        NotificationManager.success("Configuration applied!")
    }

    private fun showConfiguration() {
        // Stop the interval trainer when showing configuration to make changes
        trainer.getIntervalTrainer().stop()
        DisplayAreaComponent.showConfiguration(config) { applyConfiguration() }
    }
    
    private fun startIntervalTraining() {
        config = DisplayAreaComponent.readConfiguration()
        trainer.updateConfiguration(config)
        setupIntervalTrainer()
        doStartTraining()
    }
    
    private fun doStartTraining() {
        currentDisplayedActions.clear()
        DisplayAreaComponent.showTrainingSession()
        trainer.startIntervalTraining()
    }
    
    private fun stopTraining() {
        trainer.getIntervalTrainer().stop()
        DisplayAreaComponent.showStopped()
    }
    
    private fun generatePreview() {
        val combo = trainer.generateFlowingCombo()
        val formatted = trainer.formatCombination(combo)
        DisplayAreaComponent.showPreview(combo, formatted)
    }
}
