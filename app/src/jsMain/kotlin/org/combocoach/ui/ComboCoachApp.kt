package org.combocoach.ui

import kotlinx.browser.document
import org.combocoach.domain.*
import org.combocoach.service.ComboTrainer
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.events.Event

class ComboCoachApp(private val rootElement: Element) {
    private var config = TrainingConfiguration()
    private val trainer = ComboTrainer(config)
    private var currentDisplayedActions = mutableListOf<Action>()
    private var currentActionIndex = 0
    
    init {
        setupIntervalTrainer()
    }
    
    fun render() {
        rootElement.innerHTML = ""
        rootElement.appendChild(createMainContainer())
    }
    
    private fun setupIntervalTrainer() {
        val intervalTrainer = trainer.getIntervalTrainer()
        
        intervalTrainer.onActionDisplay = { action, index, total, isComplete ->
            displayAction(action, index, total)
            if (isComplete) {
                showCompletionMessage()
            }
        }
        
        intervalTrainer.onCombinationComplete = {
            enableStartButton()
        }
    }
    
    private fun createMainContainer(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "container")
            
            appendChild(createHeader())
            appendChild(createConfigPanel())
            appendChild(createControlPanel())
            appendChild(createDisplayArea())
            appendChild(createStrikeLegend())
        } as HTMLElement
    }
    
    private fun createHeader(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "header")
            innerHTML = """
                <h1>🥊 ComboCoach</h1>
                <p class="subtitle">Flow-Based Interval Training</p>
            """
        } as HTMLElement
    }
    
    private fun createConfigPanel(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "config-panel")
            innerHTML = """
                <h2>⚙️ Configuration</h2>
                <div class="config-grid">
                    <div class="config-item">
                        <label for="interval-input">Interval (seconds):</label>
                        <input type="number" id="interval-input" value="${config.actionIntervalMs / 1000}" min="0.5" max="5" step="0.5" />
                    </div>
                    
                    <div class="config-item">
                        <label for="mode-select">Training Mode:</label>
                        <select id="mode-select">
                            <option value="BOTH">Both (Attack + Defense)</option>
                            <option value="ATTACK_ONLY">Attack Only</option>
                            <option value="DEFENSE_ONLY">Defense Only</option>
                        </select>
                    </div>
                    
                    <div class="config-item">
                        <label for="stance-select">Stance:</label>
                        <select id="stance-select">
                            <option value="ORTHODOX">Orthodox (Left Forward)</option>
                            <option value="SOUTHPAW">Southpaw (Right Forward)</option>
                        </select>
                    </div>
                    
                    <div class="config-item">
                        <label for="min-actions-input">Min Actions:</label>
                        <input type="number" id="min-actions-input" value="${config.minActions}" min="1" max="15" />
                    </div>
                    
                    <div class="config-item">
                        <label for="max-actions-input">Max Actions:</label>
                        <input type="number" id="max-actions-input" value="${config.maxActions}" min="1" max="20" />
                    </div>
                    
                    <div class="config-item">
                        <label for="offense-ratio-input">Offense Ratio (0-100%):</label>
                        <input type="number" id="offense-ratio-input" value="${(config.offenseRatio * 100).toInt()}" min="0" max="100" />
                    </div>
                    
                    <div class="config-item config-item-full">
                        <label>
                            <input type="checkbox" id="number-notation-check" ${if (config.useNumberNotation) "checked" else ""} />
                            Use Number Notation (e.g., "1 2 3" instead of "Jab Cross Lead Hook")
                        </label>
                    </div>
                </div>
                <button id="apply-config-btn" class="btn btn-secondary">Apply Configuration</button>
            """
            
            // Attach event listeners
            querySelector("#apply-config-btn")?.addEventListener("click", {
                applyConfiguration()
            })
        } as HTMLElement
    }
    
    private fun createControlPanel(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "controls")
            
            appendChild(createButton("Start Interval Training", "btn-primary", "start-btn") {
                startIntervalTraining()
            })
            
            appendChild(createButton("Stop", "btn-danger", "stop-btn") {
                stopTraining()
            })
            
            appendChild(createButton("Generate Preview", "btn-secondary", "preview-btn") {
                generatePreview()
            })
            
            appendChild(createButton("Clear History", "btn-secondary") {
                clearHistory()
            })
        } as HTMLElement
    }
    
    private fun createDisplayArea(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "display-area")
            setAttribute("id", "display-area")
            innerHTML = """
                <div class="welcome-message">
                    <p>👊 Configure your training and click "Start Interval Training"!</p>
                    <p class="hint">Each action will be displayed one at a time with your configured interval.</p>
                </div>
            """
        } as HTMLElement
    }
    
    private fun createStrikeLegend(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "legend-container")
            innerHTML = """
                <h2>📖 Strike Notation Reference</h2>
                <p class="legend-description">
                    Standard boxing numbering system 
                    (<a href="https://www.expertboxing.com/boxing-basics/boxing-punches" target="_blank">Learn More</a>)
                </p>
                <div class="legend-grid">
                    <div class="legend-item"><span class="strike-number">1</span> - Jab</div>
                    <div class="legend-item"><span class="strike-number">2</span> - Cross</div>
                    <div class="legend-item"><span class="strike-number">3</span> - Lead Hook</div>
                    <div class="legend-item"><span class="strike-number">4</span> - Rear Hook</div>
                    <div class="legend-item"><span class="strike-number">5</span> - Lead Uppercut</div>
                    <div class="legend-item"><span class="strike-number">6</span> - Rear Uppercut</div>
                    <div class="legend-item defense"><span class="strike-number">D1</span> - Defend Jab</div>
                    <div class="legend-item defense"><span class="strike-number">D2</span> - Defend Cross</div>
                    <div class="legend-item defense"><span class="strike-number">D3</span> - Defend Hook</div>
                    <div class="legend-item defense"><span class="strike-number">D4</span> - Defend Uppercut</div>
                </div>
            """
        } as HTMLElement
    }
    
    private fun createButton(text: String, className: String, id: String = "", onClick: (Event) -> Unit): HTMLElement {
        return document.createElement("button").apply {
            this.textContent = text
            setAttribute("class", "btn $className")
            if (id.isNotEmpty()) {
                setAttribute("id", id)
            }
            addEventListener("click", onClick)
        } as HTMLElement
    }
    
    private fun applyConfiguration() {
        val intervalInput = document.getElementById("interval-input") as? HTMLInputElement
        val modeSelect = document.getElementById("mode-select") as? HTMLSelectElement
        val stanceSelect = document.getElementById("stance-select") as? HTMLSelectElement
        val minActionsInput = document.getElementById("min-actions-input") as? HTMLInputElement
        val maxActionsInput = document.getElementById("max-actions-input") as? HTMLInputElement
        val offenseRatioInput = document.getElementById("offense-ratio-input") as? HTMLInputElement
        val numberNotationCheck = document.getElementById("number-notation-check") as? HTMLInputElement
        
        config = TrainingConfiguration(
            actionIntervalMs = ((intervalInput?.value?.toFloatOrNull() ?: 1.0f) * 1000).toInt(),
            trainingMode = TrainingMode.valueOf(modeSelect?.value ?: "BOTH"),
            stance = Stance.valueOf(stanceSelect?.value ?: "ORTHODOX"),
            minActions = minActionsInput?.value?.toIntOrNull() ?: 3,
            maxActions = maxActionsInput?.value?.toIntOrNull() ?: 8,
            offenseRatio = (offenseRatioInput?.value?.toIntOrNull() ?: 50) / 100f,
            useNumberNotation = numberNotationCheck?.checked ?: true
        )
        
        trainer.updateConfiguration(config)
        
        showNotification("Configuration applied!")
    }
    
    private fun startIntervalTraining() {
        applyConfiguration()
        currentDisplayedActions.clear()
        currentActionIndex = 0
        
        val displayArea = document.getElementById("display-area")
        displayArea?.innerHTML = """
            <div class="training-in-progress">
                <h3>Training in Progress...</h3>
                <div id="current-action-display" class="current-action-display"></div>
                <div id="progress-bar-container" class="progress-bar-container">
                    <div id="progress-bar" class="progress-bar"></div>
                </div>
                <div id="combination-preview" class="combination-preview"></div>
            </div>
        """
        
        disableStartButton()
        trainer.startIntervalTraining()
    }
    
    private fun stopTraining() {
        trainer.getIntervalTrainer().stop()
        enableStartButton()
        showNotification("Training stopped")
    }
    
    private fun generatePreview() {
        applyConfiguration()
        val combo = trainer.generateFlowingCombo()
        val formatted = trainer.formatCombination(combo)
        
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
    
    private fun displayAction(action: Action, index: Int, total: Int) {
        currentDisplayedActions.add(action)
        currentActionIndex = index
        
        val currentActionDisplay = document.getElementById("current-action-display")
        val type = if (action.isOffensive()) "offensive" else "defensive"
        
        currentActionDisplay?.innerHTML = """
            <div class="action-card action-${type}">
                <div class="action-number-large">${action.toNumber()}</div>
                <div class="action-name">${action.displayName()}</div>
                <div class="action-counter">Action $index of $total</div>
            </div>
        """
        
        // Update progress bar
        val progress = (index.toFloat() / total.toFloat() * 100).toInt()
        val progressBar = document.getElementById("progress-bar")
        progressBar?.setAttribute("style", "width: $progress%")
        
        // Update combination preview
        val preview = document.getElementById("combination-preview")
        val formattedSoFar = if (config.useNumberNotation) {
            currentDisplayedActions.joinToString(" ") { it.toNumber() }
        } else {
            currentDisplayedActions.joinToString(" → ") { it.displayName() }
        }
        preview?.innerHTML = """<div class="preview-text">Combination so far: $formattedSoFar</div>"""
    }
    
    private fun showCompletionMessage() {
        val currentActionDisplay = document.getElementById("current-action-display")
        currentActionDisplay?.innerHTML += """
            <div class="completion-message">
                ✅ Combination Complete!
            </div>
        """
    }
    
    private fun clearHistory() {
        trainer.clearHistory()
        showNotification("History cleared")
    }
    
    private fun disableStartButton() {
        val btn = document.getElementById("start-btn") as? HTMLElement
        btn?.setAttribute("disabled", "true")
        btn?.style?.opacity = "0.5"
    }
    
    private fun enableStartButton() {
        val btn = document.getElementById("start-btn") as? HTMLElement
        btn?.removeAttribute("disabled")
        btn?.style?.opacity = "1"
    }
    
    private fun showNotification(message: String) {
        // Simple notification - could be enhanced with a toast library
        console.log("Notification: $message")
        // TODO: Implement visual notification
    }
}
