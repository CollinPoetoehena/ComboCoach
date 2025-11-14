package org.combocoach.ui.components

import kotlinx.browser.document
import org.combocoach.domain.Action
import org.combocoach.domain.TrainingConfiguration
import org.w3c.dom.HTMLElement

/**
 * Manages the main display area where training sessions are shown
 */
object DisplayAreaComponent {
    
    fun createInitial(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "display-area")
            setAttribute("id", "display-area")
            innerHTML = """
                <div class="welcome-message">
                    <p>👊 Configure your training and click "Start Training"!</p>
                </div>
            """
        } as HTMLElement
    }
    
    fun showTrainingInProgress() {
        val displayArea = document.getElementById("display-area")
        displayArea?.innerHTML = """
            <div class="training-in-progress">
                <div class="stats-row">
                    <div class="stat-box">
                        <div class="stat-label">Combos Completed</div>
                        <div class="stat-value" id="combos-completed">0</div>
                    </div>
                </div>
                <div id="current-action-display" class="current-action-display"></div>
                <div id="progress-bar-container" class="progress-bar-container">
                    <div id="progress-bar" class="progress-bar"></div>
                </div>
                <div id="combination-preview" class="combination-preview"></div>
            </div>
        """
    }
    
    fun showStoppedMessage() {
        val displayArea = document.getElementById("display-area")
        displayArea?.innerHTML = """
            <div class="welcome-message">
                <p>⏹️ Training stopped</p>
                <p class="hint">Click "Start Training" to begin again</p>
            </div>
        """
    }
    
    fun showPreview(combo: List<Action>, formatted: String) {
        val displayArea = document.getElementById("display-area")
        displayArea?.innerHTML = """
            <div class="preview-display">
                <h3>Generated Combination Preview</h3>
                <div class="combo-preview-text">${formatted}</div>
                <div class="combo-details">
                    ${combo.mapIndexed { index, action ->
                        val type = if (action.isOffensive()) "offensive" else "defensive"
                        """<span class="action-badge action-$type">${action.toNumber()} - ${action.displayName()}</span>"""
                    }.joinToString("")}
                </div>
            </div>
        """
    }
    
    fun displayAction(action: Action, index: Int, total: Int) {
        val currentActionDisplay = document.getElementById("current-action-display")
        val type = if (action.isOffensive()) "offensive" else "defensive"
        
        currentActionDisplay?.innerHTML = """
            <div class="action-card action-${type}">
                <div class="action-number-large">${action.toNumber()}</div>
                <div class="action-name">${action.displayName()}</div>
                <div class="action-counter">Action $index of $total</div>
            </div>
        """
        
        updateProgressBar(index, total)
    }
    
    fun updateCombinationPreview(actions: List<Action>, config: TrainingConfiguration) {
        val preview = document.getElementById("combination-preview")
        val formattedSoFar = if (config.useNumberNotation) {
            actions.joinToString(" ") { it.toNumber() }
        } else {
            actions.joinToString(" → ") { it.displayName() }
        }
        preview?.innerHTML = """<div class="preview-text">Combination: $formattedSoFar</div>"""
    }
    
    fun updateCompletedCombos(comboNumber: Int) {
        val statsValue = document.getElementById("combos-completed")
        statsValue?.textContent = comboNumber.toString()
    }
    
    fun showWaitingMessage(secondsRemaining: Int) {
        val currentActionDisplay = document.getElementById("current-action-display")
        currentActionDisplay?.innerHTML = """
            <div class="waiting-card">
                <div class="waiting-timer">${secondsRemaining}</div>
                <div class="waiting-text">Next combination starting...</div>
            </div>
        """
        
        resetProgressBar()
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
