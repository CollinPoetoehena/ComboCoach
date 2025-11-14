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
    
    fun render() {
        // Clear root and build main container (this contains all UI components)
        rootElement.innerHTML = ""
        rootElement.appendChild(createMainContainer())
        
        // Attach event listeners after DOM is created
        attachEventListeners()
    }
    
    private fun attachEventListeners() {
        // Apply configuration button
        document.getElementById("apply-config-btn")?.addEventListener("click", {
            applyConfiguration()
        })
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
    
    private fun showConfiguration() {
        DisplayAreaComponent.showConfiguration(config)
    }
    
    private fun applyConfiguration() {
        // Stop any running training before updating configuration
        val wasRunning = trainer.getIntervalTrainer().isActive()
        if (wasRunning) {
            trainer.getIntervalTrainer().stop()
        }
        
        // Read new configuration from UI and update the trainer instance
        trainer.updateConfiguration(ConfigPanelComponent.readConfiguration())

        // Re-setup interval trainer callbacks after configuration update
        setupIntervalTrainer()
        
        if (wasRunning) {
            doStartTraining()
        }
        
        NotificationManager.success("Configuration applied!")
    }
    
    private fun startIntervalTraining() {
        trainer.updateConfiguration(ConfigPanelComponent.readConfiguration())
        // Setup interval trainer callbacks after configuration update
        setupIntervalTrainer()
        // Start training
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
