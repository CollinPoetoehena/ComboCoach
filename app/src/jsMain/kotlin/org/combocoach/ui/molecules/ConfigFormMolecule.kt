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
        
        // Centralized slider display for min/max actions (dual-handle workaround)
        return document.createElement("div").apply {
            setAttribute("class", "config-form")
            innerHTML = """
                <div class='config-grid'>
                    <div class='config-item'>
                        <label for='action-interval-slider'>Action Interval (seconds):</label>
                        <div style='display:flex;align-items:center;gap:8px;'>
                            <input type='range' id='action-interval-slider' min='0.5' max='5' step='0.5' value='${config.actionIntervalMs / 1000.0}' style='flex:1;' />
                            <span id='action-interval-display'>${config.actionIntervalMs / 1000.0}s</span>
                        </div>
                    </div>
                    <div class='config-item'>
                        <label for='combo-interval-slider'>Combination Interval (seconds):</label>
                        <div style='display:flex;align-items:center;gap:8px;'>
                            <input type='range' id='combo-interval-slider' min='1' max='10' step='0.5' value='${config.combinationIntervalMs / 1000.0}' style='flex:1;' />
                            <span id='combo-interval-display'>${config.combinationIntervalMs / 1000.0}s</span>
                        </div>
                    </div>
                    <div class='config-item'>
                        <label for='actions-range-min'>Actions per Combination:</label>
                        <div style='display:flex;align-items:center;gap:8px;flex-wrap:wrap;'>
                            <input type='range' id='actions-range-min' min='1' max='20' step='1' value='${config.minActions}' style='flex:1;' />
                            <span id='actions-range-min-display'>${config.minActions}</span>
                            <span style='margin:0 4px;'>to</span>
                            <input type='range' id='actions-range-max' min='1' max='20' step='1' value='${config.maxActions}' style='flex:1;' />
                            <span id='actions-range-max-display'>${config.maxActions}</span>
                        </div>
                        <small style='color:#888;'>Select the minimum and maximum actions per combination.</small>
                    </div>
                    <div class='config-item'>
                        <label for='stance-select'>Stance:</label>
                        <select id='stance-select'>
                            <option value='ORTHODOX' ${if (config.stance == Stance.ORTHODOX) "selected" else ""}>Orthodox (Left Forward)</option>
                            <option value='SOUTHPAW' ${if (config.stance == Stance.SOUTHPAW) "selected" else ""}>Southpaw (Right Forward)</option>
                        </select>
                    </div>
                    <div class='config-item'>
                        <label for='mode-select'>Training Mode:</label>
                        <select id='mode-select'>
                            <option value='BOTH' ${if (config.trainingMode == TrainingMode.BOTH) "selected" else ""}>Both (Attack + Defense)</option>
                            <option value='ATTACK_ONLY' ${if (config.trainingMode == TrainingMode.ATTACK_ONLY) "selected" else ""}>Attack Only</option>
                            <option value='DEFENSE_ONLY' ${if (config.trainingMode == TrainingMode.DEFENSE_ONLY) "selected" else ""}>Defense Only</option>
                        </select>
                    </div>
                    <div class='config-item' id='offense-ratio-container' style='display: ${if (config.trainingMode == TrainingMode.BOTH) "flex" else "none"};'>
                        <label for='offense-ratio-slider'>Offense % (when using Both):</label>
                        <div style='display:flex;align-items:center;gap:8px;'>
                            <input type='range' id='offense-ratio-slider' min='0' max='100' step='1' value='${(config.offenseRatio * 100).toInt()}' style='flex:1;' />
                            <span id='offense-ratio-display'>${(config.offenseRatio * 100).toInt()}%</span>
                        </div>
                    </div>
                    <div class='config-item config-item-checkbox'>
                        <label>
                            <input type='checkbox' id='number-notation-check' ${if (config.useNumberNotation) "checked" else ""} />
                            Use Number Notation (e.g., "1 2 3")
                        </label>
                    </div>
                    <div class='config-item config-item-checkbox'>
                        <label>
                            <input type='checkbox' id='sound-enabled-check' ${if (currentSoundEnabled) "checked" else ""} />
                            Enable Sound Effects
                        </label>
                    </div>
                    <div class='config-item' id='sound-volume-container' style='display: $soundControlsDisplay;'>
                        <label for='volume-slider'>Sound Volume:</label>
                        <div style='display:flex;align-items:center;gap:8px;'>
                            <input type='range' id='volume-slider' min='5' max='100' value='$currentVolume' step='5' style='flex: 1;' />
                            <span id='volume-display'>$currentVolume%</span>
                        </div>
                    </div>
                    <div class='config-item' id='sound-pitch-container' style='display: $soundControlsDisplay;'>
                        <label for='pitch-slider'>Speech Pitch:</label>
                        <div style='display:flex;align-items:center;gap:8px;'>
                            <input type='range' id='pitch-slider' min='0' max='20' value='$currentPitch' step='1' style='flex: 1;' />
                            <span id='pitch-display'>${currentPitch / 10.0}</span>
                        </div>
                    </div>
                    <div class='config-item' id='sound-voice-container' style='display: $soundControlsDisplay;'>
                        <label for='voice-select'>Voice:</label>
                        <select id='voice-select'>
                            <option value=''>Default</option>
                        </select>
                    </div>
                </div>
                <button id='apply-config-btn' class='btn btn-primary'>Apply Configuration</button>
            """
        } as HTMLElement
    }
    
    /**
     * Reads configuration from form inputs
     */
    fun readConfiguration(): TrainingConfiguration {
        val actionIntervalSlider = document.getElementById("action-interval-slider") as? HTMLInputElement
        val comboIntervalSlider = document.getElementById("combo-interval-slider") as? HTMLInputElement
        val modeSelect = document.getElementById("mode-select") as? HTMLSelectElement
        val stanceSelect = document.getElementById("stance-select") as? HTMLSelectElement
        val minActionsSlider = document.getElementById("actions-range-min") as? HTMLInputElement
        val maxActionsSlider = document.getElementById("actions-range-max") as? HTMLInputElement
        val offenseRatioSlider = document.getElementById("offense-ratio-slider") as? HTMLInputElement
        val numberNotationCheck = document.getElementById("number-notation-check") as? HTMLInputElement

        val mode = TrainingMode.valueOf(modeSelect?.value ?: "BOTH")
        val minActions = minActionsSlider?.value?.toIntOrNull() ?: 3
        val maxActions = maxActionsSlider?.value?.toIntOrNull() ?: minActions
        return TrainingConfiguration(
            actionIntervalMs = ((actionIntervalSlider?.value?.toFloatOrNull() ?: 1.0f) * 1000).toInt(),
            combinationIntervalMs = ((comboIntervalSlider?.value?.toFloatOrNull() ?: 3.0f) * 1000).toInt(),
            trainingMode = mode,
            stance = Stance.valueOf(stanceSelect?.value ?: "ORTHODOX"),
            minActions = minActions,
            maxActions = maxActions,
            offenseRatio = (offenseRatioSlider?.value?.toIntOrNull() ?: 50) / 100f,
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
        // Helper for slider display
        fun setupSlider(id: String, displayId: String, suffix: String = "", transform: ((String) -> String)? = null) {
            val slider = document.getElementById(id) as? HTMLInputElement
            val display = document.getElementById(displayId)
            slider?.addEventListener("input", {
                val value = slider.value
                display?.textContent = (transform?.invoke(value) ?: value) + suffix
            })
        }

        // Sound enabled checkbox - toggle visibility of sound controls
        document.getElementById("sound-enabled-check")?.addEventListener("change", { event ->
            val checkbox = event.target as? HTMLInputElement
            val isEnabled = checkbox?.checked ?: true
            org.combocoach.ui.SoundManager.setEnabled(isEnabled)
            val display = if (isEnabled) "flex" else "none"
            (document.getElementById("sound-volume-container") as? HTMLElement)?.setAttribute("style", "display: $display;")
            (document.getElementById("sound-pitch-container") as? HTMLElement)?.setAttribute("style", "display: $display;")
            (document.getElementById("sound-voice-container") as? HTMLElement)?.setAttribute("style", "display: $display;")
        })

        // Sliders for intervals and offense %
        setupSlider("action-interval-slider", "action-interval-display", "s")
        setupSlider("combo-interval-slider", "combo-interval-display", "s")
        setupSlider("offense-ratio-slider", "offense-ratio-display", "%")

        // Double-ended actions slider logic
        val minActionsSlider = document.getElementById("actions-range-min") as? HTMLInputElement
        val maxActionsSlider = document.getElementById("actions-range-max") as? HTMLInputElement
        val minActionsDisplay = document.getElementById("actions-range-min-display")
        val maxActionsDisplay = document.getElementById("actions-range-max-display")
        fun updateActionsRangeDisplays() {
            val minVal = minActionsSlider?.value?.toIntOrNull() ?: 1
            val maxVal = maxActionsSlider?.value?.toIntOrNull() ?: minVal
            // Ensure min <= max
            if (maxVal < minVal) {
                maxActionsSlider?.value = minVal.toString()
                maxActionsDisplay?.textContent = minVal.toString()
            } else {
                maxActionsDisplay?.textContent = maxVal.toString()
            }
            minActionsDisplay?.textContent = minVal.toString()
            maxActionsSlider?.min = minVal.toString()
        }
        minActionsSlider?.addEventListener("input", { updateActionsRangeDisplays() })
        maxActionsSlider?.addEventListener("input", { updateActionsRangeDisplays() })
        updateActionsRangeDisplays()

        // Volume slider (minimum 5%)
        val volumeSlider = document.getElementById("volume-slider") as? HTMLInputElement
        val volumeDisplay = document.getElementById("volume-display")
        volumeSlider?.addEventListener("input", { event ->
            val slider = event.target as? HTMLInputElement
            val volume = (slider?.value?.toIntOrNull() ?: 30).coerceAtLeast(5)
            volumeDisplay?.textContent = "$volume%"
            org.combocoach.ui.SoundManager.setVolume(volume / 100f)
            slider?.value = volume.toString()
        })

        // Pitch slider (0 to 2.0)
        val pitchSlider = document.getElementById("pitch-slider") as? HTMLInputElement
        val pitchDisplay = document.getElementById("pitch-display")
        pitchSlider?.addEventListener("input", { event ->
            val slider = event.target as? HTMLInputElement
            val pitchValue = (slider?.value?.toIntOrNull() ?: 10).coerceIn(0, 20) / 10f
            pitchDisplay?.textContent = "$pitchValue"
            org.combocoach.ui.SoundManager.setPitch(pitchValue)
            slider?.value = ((pitchValue * 10).toInt()).toString()
        })

        // Populate voice dropdown - voices may not be ready immediately
        populateVoices()
        val synth = js("window.speechSynthesis")
        if (js("'onvoiceschanged' in synth") as Boolean) {
            synth.onvoiceschanged = { populateVoices() }
        }
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
