package org.combocoach.ui.molecules

import kotlinx.browser.document
import org.combocoach.domain.Stance
import org.combocoach.domain.TrainingConfiguration
import org.combocoach.domain.TrainingMode
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement

/**
 * Configuration form molecule
 */
object ConfigFormMolecule {
    
    fun create(config: TrainingConfiguration): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "config-form")
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
                <button id="apply-config-btn" class="btn btn-primary">Apply Configuration</button>
            """
        } as HTMLElement
    }
    
    /**
     * Reads configuration from form inputs
     */
    fun readConfiguration(): TrainingConfiguration {
        val actionIntervalInput = document.getElementById("action-interval-input") as? HTMLInputElement
        val comboIntervalInput = document.getElementById("combo-interval-input") as? HTMLInputElement
        val modeSelect = document.getElementById("mode-select") as? HTMLSelectElement
        val stanceSelect = document.getElementById("stance-select") as? HTMLSelectElement
        val minActionsInput = document.getElementById("min-actions-input") as? HTMLInputElement
        val maxActionsInput = document.getElementById("max-actions-input") as? HTMLInputElement
        val offenseRatioInput = document.getElementById("offense-ratio-input") as? HTMLInputElement
        val numberNotationCheck = document.getElementById("number-notation-check") as? HTMLInputElement
        
        val mode = TrainingMode.valueOf(modeSelect?.value ?: "BOTH")
        
        return TrainingConfiguration(
            actionIntervalMs = ((actionIntervalInput?.value?.toFloatOrNull() ?: 1.0f) * 1000).toInt(),
            combinationIntervalMs = ((comboIntervalInput?.value?.toFloatOrNull() ?: 3.0f) * 1000).toInt(),
            trainingMode = mode,
            stance = Stance.valueOf(stanceSelect?.value ?: "ORTHODOX"),
            minActions = minActionsInput?.value?.toIntOrNull() ?: 3,
            maxActions = maxActionsInput?.value?.toIntOrNull() ?: 8,
            offenseRatio = (offenseRatioInput?.value?.toIntOrNull() ?: 50) / 100f,
            useNumberNotation = numberNotationCheck?.checked ?: true
        )
    }
    
    /**
     * Setup event listener for mode select to show/hide offense ratio
     */
    fun setupModeSelectListener() {
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
}
