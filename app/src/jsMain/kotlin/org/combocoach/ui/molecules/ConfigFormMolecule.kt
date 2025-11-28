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
        // Get current sound settings from SoundManager (default to enabled if not set)
        val currentVolume = (org.combocoach.ui.SoundManager.getVolume() * 100).toInt()
        val currentPitch = (org.combocoach.ui.SoundManager.getPitch() * 10).toInt()
        val currentVoiceIndex = org.combocoach.ui.SoundManager.getVoiceIndex()
        val currentSoundEnabled = org.combocoach.ui.SoundManager.isEnabled()
        // Hide sound controls if sound is disabled
        val soundControlsDisplay = if (currentSoundEnabled) "flex" else "none"
        
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
                        <input type="number" id="combo-interval-input" value="${config.combinationIntervalMs / 1000.0}" min="1.0" max="10" step="0.5" />
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
                    
                    <div class="config-item config-item-checkbox">
                        <label>
                            <input type="checkbox" id="sound-enabled-check" ${if (currentSoundEnabled) "checked" else ""} />
                            Enable Sound Effects
                        </label>
                    </div>
                    
                    <div class="config-item" id="sound-volume-container" style="display: $soundControlsDisplay;">
                        <label for="volume-slider">Sound Volume:</label>
                        <input type="range" id="volume-slider" min="5" max="100" value="$currentVolume" step="5" style="flex: 1;" />
                        <span id="volume-display" style="min-width: 40px; text-align: right;">$currentVolume%</span>
                    </div>
                    
                    <div class="config-item" id="sound-pitch-container" style="display: $soundControlsDisplay;">
                        <label for="pitch-slider">Speech Pitch:</label>
                        <input type="range" id="pitch-slider" min="0" max="20" value="$currentPitch" step="1" style="flex: 1;" />
                        <span id="pitch-display" style="min-width: 40px; text-align: right;">${currentPitch / 10.0}</span>
                    </div>
                    
                    <div class="config-item" id="sound-voice-container" style="display: $soundControlsDisplay;">
                        <label for="voice-select">Voice:</label>
                        <select id="voice-select">
                            <option value="">Default</option>
                        </select>
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
    
    /**
     * Setup sound control listeners
     */
    fun setupSoundControls() {
        // Sound enabled checkbox - toggle visibility of sound controls
        document.getElementById("sound-enabled-check")?.addEventListener("change", { event ->
            val checkbox = event.target as? HTMLInputElement
            val isEnabled = checkbox?.checked ?: true
            org.combocoach.ui.SoundManager.setEnabled(isEnabled)
            
            // Show/hide sound controls based on checkbox state
            val display = if (isEnabled) "flex" else "none"
            (document.getElementById("sound-volume-container") as? HTMLElement)?.setAttribute("style", "display: $display;")
            (document.getElementById("sound-pitch-container") as? HTMLElement)?.setAttribute("style", "display: $display;")
            (document.getElementById("sound-voice-container") as? HTMLElement)?.setAttribute("style", "display: $display;")
        })
        
        // Volume slider (minimum 5%)
        val volumeSlider = document.getElementById("volume-slider") as? HTMLInputElement
        val volumeDisplay = document.getElementById("volume-display")
        
        volumeSlider?.addEventListener("input", { event ->
            val slider = event.target as? HTMLInputElement
            val volume = (slider?.value?.toIntOrNull() ?: 30).coerceAtLeast(5)
            volumeDisplay?.textContent = "$volume%"
            org.combocoach.ui.SoundManager.setVolume(volume / 100f)
        })
        
        // Pitch slider (0 to 2.0)
        val pitchSlider = document.getElementById("pitch-slider") as? HTMLInputElement
        val pitchDisplay = document.getElementById("pitch-display")
        
        pitchSlider?.addEventListener("input", { event ->
            val slider = event.target as? HTMLInputElement
            val pitchValue = (slider?.value?.toIntOrNull() ?: 10) / 10f
            pitchDisplay?.textContent = "$pitchValue"
            org.combocoach.ui.SoundManager.setPitch(pitchValue)
        })
        
        // Populate voice dropdown - voices may not be ready immediately
        populateVoices()
        
        // In Chrome, voices are loaded asynchronously, so we need to handle onvoiceschanged
        val synth = js("window.speechSynthesis")
        if (js("'onvoiceschanged' in synth") as Boolean) {
            synth.onvoiceschanged = { populateVoices() }
        }
        
        // Voice selection
        document.getElementById("voice-select")?.addEventListener("change", { event ->
            val select = event.target as? HTMLSelectElement
            val selectedIndex = (select?.selectedIndex ?: 0) - 1 // -1 because of "Default" option
            org.combocoach.ui.SoundManager.setVoice(if (selectedIndex >= 0) selectedIndex else null)
        })
    }
    
    /**
     * Populate voice dropdown with available voices
     * Following MDN documentation pattern for SpeechSynthesisVoice
     */
    private fun populateVoices() {
        val voiceSelect = document.getElementById("voice-select") as? HTMLSelectElement ?: return
        val voices = org.combocoach.ui.SoundManager.getVoices()
        val currentVoiceIndex = org.combocoach.ui.SoundManager.getVoiceIndex()
        
        // Clear existing options except the first (Default)
        val optionsLength = voiceSelect.options.length as Int
        while (optionsLength > 1) {
            voiceSelect.remove(1)
        }
        
        // Add voice options following MDN pattern
        voices.forEachIndexed { index, voice ->
            val option = document.createElement("option") as org.w3c.dom.HTMLOptionElement
            val name = voice.name.toString()
            val lang = voice.lang.toString()
            val isDefault = voice.default as? Boolean ?: false
            
            option.value = index.toString()
            option.text = "$name ($lang)" + if (isDefault) " — DEFAULT" else ""
            option.setAttribute("data-name", name)
            option.setAttribute("data-lang", lang)
            
            voiceSelect.add(option)
        }
        
        // Restore previously selected voice
        if (currentVoiceIndex != null) {
            voiceSelect.selectedIndex = currentVoiceIndex + 1 // +1 because of "Default" option
        }
    }
}
