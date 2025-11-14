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
    private var isConfigExpanded = false
    private var isLegendExpanded = false
    
    init {
        setupIntervalTrainer()
    }
    
    fun render() {
        rootElement.innerHTML = ""
        rootElement.appendChild(createMainContainer())
        
        // Attach event listeners after DOM is created
        attachEventListeners()
    }
    
    private fun attachEventListeners() {
        // Apply configuration button
        document.getElementById("apply-config-btn")?.addEventListener("click", {
            console.log("Apply Configuration button clicked!")
            applyConfiguration()
        })
        
        // Show/hide offense ratio based on mode selection
        document.getElementById("mode-select")?.addEventListener("change", { event ->
            val select = event.target as? HTMLSelectElement
            val offenseContainer = document.getElementById("offense-ratio-container") as? HTMLElement
            if (select?.value == "BOTH") {
                offenseContainer?.setAttribute("style", "display: flex;")
            } else {
                offenseContainer?.setAttribute("style", "display: none;")
            }
        })
    }
    
    private fun setupIntervalTrainer() {
        val intervalTrainer = trainer.getIntervalTrainer()
        
        intervalTrainer.onActionDisplay = { action, index, total, isComplete ->
            displayAction(action, index, total)
        }
        
        intervalTrainer.onCombinationComplete = { comboNumber ->
            showCombinationComplete(comboNumber)
        }
        
        intervalTrainer.onWaitingBetweenCombinations = { secondsRemaining ->
            showWaitingMessage(secondsRemaining)
        }
    }
    
    private fun createMainContainer(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "container")
            
            appendChild(createHeader())
            appendChild(createDisplayArea())
            appendChild(createControlPanel())
            appendChild(createConfigPanel())
            appendChild(createStrikeLegend())
        } as HTMLElement
    }
    
    private fun createHeader(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "header")
            innerHTML = """
                <h1>🥊 ComboCoach – Flow-Based Interval Training</h1>
            """
        } as HTMLElement
    }
    
    private fun createDisplayArea(): HTMLElement {
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
    
    private fun createControlPanel(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "controls")
            
            appendChild(createButton("▶️ Start Training", "btn-primary", "start-btn") {
                startIntervalTraining()
            })
            
            appendChild(createButton("⏹️ Stop", "btn-danger", "stop-btn") {
                stopTraining()
            })
            
            appendChild(createButton("👁️ Preview Combo", "btn-secondary") {
                generatePreview()
            })
        } as HTMLElement
    }
    
    private fun createConfigPanel(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "config-panel")
            
            // Header with toggle
            appendChild(document.createElement("div").apply {
                setAttribute("class", "config-header")
                innerHTML = """
                    <h2>⚙️ Configuration</h2>
                    <button id="config-toggle-btn" class="btn-icon">▼</button>
                """
                querySelector("#config-toggle-btn")?.addEventListener("click", {
                    isConfigExpanded = !isConfigExpanded
                    render()
                })
            })
            
            // Collapsible content
            if (isConfigExpanded) {
                appendChild(createConfigContent())
            }
        } as HTMLElement
    }
    
    private fun createConfigContent(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "config-content")
            
            innerHTML = """
                <div class="config-grid">
                    <div class="config-item">
                        <label for="action-interval-input">Action Interval (seconds):</label>
                        <input type="number" id="action-interval-input" value="${config.actionIntervalMs / 1000.0}" min="0.5" max="5" step="0.5" />
                    </div>
                    
                    <div class="config-item">
                        <label for="combo-interval-input">Combination Interval (seconds):</label>
                        <input type="number" id="combo-interval-input" value="${config.combinationIntervalMs / 1000.0}" min="1" max="10" step="0.5" />
                    </div>
                    
                    <div class="config-item">
                        <label for="mode-select">Training Mode:</label>
                        <select id="mode-select">
                            <option value="BOTH" ${if (config.trainingMode == TrainingMode.BOTH) "selected" else ""}>Both (Attack + Defense)</option>
                            <option value="ATTACK_ONLY" ${if (config.trainingMode == TrainingMode.ATTACK_ONLY) "selected" else ""}>Attack Only</option>
                            <option value="DEFENSE_ONLY" ${if (config.trainingMode == TrainingMode.DEFENSE_ONLY) "selected" else ""}>Defense Only</option>
                        </select>
                    </div>
                    
                    <div class="config-item" id="offense-ratio-container" style="display: ${if (config.trainingMode == TrainingMode.BOTH) "flex" else "none"};">
                        <label for="offense-ratio-input">Offense % (when using Both):</label>
                        <input type="number" id="offense-ratio-input" value="${(config.offenseRatio * 100).toInt()}" min="0" max="100" step="5" />
                    </div>
                    
                    <div class="config-item">
                        <label for="stance-select">Stance:</label>
                        <select id="stance-select">
                            <option value="ORTHODOX" ${if (config.stance == Stance.ORTHODOX) "selected" else ""}>Orthodox (Left Forward)</option>
                            <option value="SOUTHPAW" ${if (config.stance == Stance.SOUTHPAW) "selected" else ""}>Southpaw (Right Forward)</option>
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
                    
                    <div class="config-item config-item-checkbox">
                        <label>
                            <input type="checkbox" id="number-notation-check" ${if (config.useNumberNotation) "checked" else ""} />
                            Use Number Notation (e.g., "1 2 3")
                        </label>
                    </div>
                </div>
                <button id="apply-config-btn" class="btn btn-primary">✓ Apply Configuration</button>
            """
        } as HTMLElement
    }
    
    private fun createStrikeLegend(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "legend-container")
            
            // Header with toggle
            appendChild(document.createElement("div").apply {
                setAttribute("class", "legend-header")
                innerHTML = """
                    <h2>📖 Strike Notation Reference</h2>
                    <button id="legend-toggle-btn" class="btn-icon">▼</button>
                """
                querySelector("#legend-toggle-btn")?.addEventListener("click", {
                    isLegendExpanded = !isLegendExpanded
                    render()
                })
            })
            
            // Collapsible content
            if (isLegendExpanded) {
                appendChild(document.createElement("div").apply {
                    setAttribute("class", "legend-content")
                    innerHTML = """
                        <p class="legend-description">
                            Standard boxing numbering system 
                            (<a href="https://expertboxing.com/the-beginners-guide-to-boxing" target="_blank">Learn More</a>)
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
                })
            }
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
        // Stop any running training before updating configuration
        val wasRunning = trainer.getIntervalTrainer().isActive()
        if (wasRunning) {
            trainer.getIntervalTrainer().stop()
        }
        
        val actionIntervalInput = document.getElementById("action-interval-input") as? HTMLInputElement
        val comboIntervalInput = document.getElementById("combo-interval-input") as? HTMLInputElement
        val modeSelect = document.getElementById("mode-select") as? HTMLSelectElement
        val stanceSelect = document.getElementById("stance-select") as? HTMLSelectElement
        val minActionsInput = document.getElementById("min-actions-input") as? HTMLInputElement
        val maxActionsInput = document.getElementById("max-actions-input") as? HTMLInputElement
        val offenseRatioInput = document.getElementById("offense-ratio-input") as? HTMLInputElement
        val numberNotationCheck = document.getElementById("number-notation-check") as? HTMLInputElement
        
        val mode = TrainingMode.valueOf(modeSelect?.value ?: "BOTH")
        
        config = TrainingConfiguration(
            actionIntervalMs = ((actionIntervalInput?.value?.toFloatOrNull() ?: 1.0f) * 1000).toInt(),
            combinationIntervalMs = ((comboIntervalInput?.value?.toFloatOrNull() ?: 3.0f) * 1000).toInt(),
            trainingMode = mode,
            stance = Stance.valueOf(stanceSelect?.value ?: "ORTHODOX"),
            minActions = minActionsInput?.value?.toIntOrNull() ?: 3,
            maxActions = maxActionsInput?.value?.toIntOrNull() ?: 8,
            offenseRatio = (offenseRatioInput?.value?.toIntOrNull() ?: 50) / 100f,
            useNumberNotation = numberNotationCheck?.checked ?: true
        )
        
        trainer.updateConfiguration(config)
        
        // Re-setup interval trainer callbacks after configuration update
        setupIntervalTrainer()
        
        // If training was running, restart it with new config (but don't call applyConfiguration again!)
        if (wasRunning) {
            doStartTraining()
        }
        
        // Collapse config panel after applying
        isConfigExpanded = false
        render()
        
        showNotification("Configuration applied! ✓")
    }
    
    private fun startIntervalTraining() {
        // Read configuration from inputs before starting
        readConfigurationFromInputs()
        doStartTraining()
    }
    
    private fun readConfigurationFromInputs() {
        val actionIntervalInput = document.getElementById("action-interval-input") as? HTMLInputElement
        val comboIntervalInput = document.getElementById("combo-interval-input") as? HTMLInputElement
        val modeSelect = document.getElementById("mode-select") as? HTMLSelectElement
        val stanceSelect = document.getElementById("stance-select") as? HTMLSelectElement
        val minActionsInput = document.getElementById("min-actions-input") as? HTMLInputElement
        val maxActionsInput = document.getElementById("max-actions-input") as? HTMLInputElement
        val offenseRatioInput = document.getElementById("offense-ratio-input") as? HTMLInputElement
        val numberNotationCheck = document.getElementById("number-notation-check") as? HTMLInputElement
        
        val mode = TrainingMode.valueOf(modeSelect?.value ?: "BOTH")
        
        val actionInterval = (actionIntervalInput?.value?.toFloatOrNull() ?: 1.0f) * 1000
        val comboInterval = (comboIntervalInput?.value?.toFloatOrNull() ?: 3.0f) * 1000
        
        console.log("Reading config - Action Interval: ${actionInterval}ms, Combo Interval: ${comboInterval}ms")
        
        config = TrainingConfiguration(
            actionIntervalMs = actionInterval.toInt(),
            combinationIntervalMs = comboInterval.toInt(),
            trainingMode = mode,
            stance = Stance.valueOf(stanceSelect?.value ?: "ORTHODOX"),
            minActions = minActionsInput?.value?.toIntOrNull() ?: 3,
            maxActions = maxActionsInput?.value?.toIntOrNull() ?: 8,
            offenseRatio = (offenseRatioInput?.value?.toIntOrNull() ?: 50) / 100f,
            useNumberNotation = numberNotationCheck?.checked ?: true
        )
        
        console.log("New config created: actionIntervalMs=${config.actionIntervalMs}, combinationIntervalMs=${config.combinationIntervalMs}")
        
        trainer.updateConfiguration(config)
        setupIntervalTrainer()
        
        console.log("Configuration updated and callbacks re-setup")
    }
    
    private fun doStartTraining() {
        currentDisplayedActions.clear()
        
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
        
        disableStartButton()
        trainer.startIntervalTraining()
    }
    
    private fun stopTraining() {
        trainer.getIntervalTrainer().stop()
        enableStartButton()
        
        val displayArea = document.getElementById("display-area")
        displayArea?.innerHTML = """
            <div class="welcome-message">
                <p>⏹️ Training stopped</p>
                <p class="hint">Click "Start Training" to begin again</p>
            </div>
        """
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
        preview?.innerHTML = """<div class="preview-text">Combination: $formattedSoFar</div>"""
    }
    
    private fun showCombinationComplete(comboNumber: Int) {
        // Update stats
        val statsValue = document.getElementById("combos-completed")
        statsValue?.textContent = comboNumber.toString()
        
        // Clear for next combination
        currentDisplayedActions.clear()
    }
    
    private fun showWaitingMessage(secondsRemaining: Int) {
        val currentActionDisplay = document.getElementById("current-action-display")
        currentActionDisplay?.innerHTML = """
            <div class="waiting-card">
                <div class="waiting-timer">${secondsRemaining}</div>
                <div class="waiting-text">Next combination starting...</div>
            </div>
        """
        
        // Reset progress bar
        val progressBar = document.getElementById("progress-bar")
        progressBar?.setAttribute("style", "width: 0%")
    }
    
    private fun disableStartButton() {
        val btn = document.getElementById("start-btn") as? HTMLElement
        btn?.setAttribute("disabled", "true")
        btn?.setAttribute("style", "opacity: 0.6; cursor: not-allowed;")
    }
    
    private fun enableStartButton() {
        val btn = document.getElementById("start-btn") as? HTMLElement
        btn?.removeAttribute("disabled")
        btn?.setAttribute("style", "opacity: 1; cursor: pointer;")
    }
    
    private fun showNotification(message: String) {
        console.log("Notification: $message")
    }
}
