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
    /**
     * Creates the configuration form UI for training settings.
     * All UI elements are generated here, including sliders, selects, and checkboxes.
     * The form is returned as an HTMLElement for insertion into the DOM.
     */
    fun create(config: TrainingConfiguration): HTMLElement {
        // Build the form HTML with all configuration controls
        // Get current sound settings from SoundManager (default to enabled if not set)
        val currentVolume = (org.combocoach.ui.SoundManager.getVolume() * 100).toInt()
        val currentPitch = (org.combocoach.ui.SoundManager.getPitch() * 10).toInt()
        val currentVoiceIndex = org.combocoach.ui.SoundManager.getVoiceIndex()
        val currentSoundEnabled = org.combocoach.ui.SoundManager.isEnabled()
        // Hide sound controls if sound is disabled
        val soundControlsDisplay = if (currentSoundEnabled) "flex" else "none"
        
        // NOTE: Using sliders for most settings or pre-configured selects to ensure valid input and correct configs, do NOT change these! 
        // For example, min/max actions have ranges to avoid having to use a 3rd party library for a custom slider, etc.!
        return document.createElement("div").apply {
            setAttribute("class", "config-form")
            innerHTML = """
                <div class='config-grid'>
                    <div class='config-item'>
                        <label for='action-interval-slider'>Action Interval (seconds):</label>
                        <div style='display:flex;align-items:center;gap:8px;'>
                            <input type='range' id='action-interval-slider' min='0.5' max='5' step='0.5' value='${config.actionIntervalMs / 1000.0}' style='flex:1;' />
                            <span id='action-interval-display' style='min-width:48px;display:inline-block;text-align:center;'>${config.actionIntervalMs / 1000.0}s</span>
                        </div>
                    </div>
                    <div class='config-item'>
                        <label for='combo-interval-slider'>Combination Interval (seconds):</label>
                        <div style='display:flex;align-items:center;gap:8px;'>
                            <input type='range' id='combo-interval-slider' min='1' max='10' step='0.5' value='${config.combinationIntervalMs / 1000.0}' style='flex:1;' />
                            <span id='combo-interval-display' style='min-width:48px;display:inline-block;text-align:center;'>${config.combinationIntervalMs / 1000.0}s</span>
                        </div>
                    </div>
                    <div class='config-item'>
                        <label for='actions-range-select'>Actions per Combination:</label>
                        <select id='actions-range-select'>
                            <option value='3-5' ${if (config.minActions == 3 && config.maxActions == 5) "selected" else ""}>3 to 5</option>
                            <option value='5-7' ${if (config.minActions == 5 && config.maxActions == 7) "selected" else ""}>5 to 7</option>
                            <option value='7-10' ${if (config.minActions == 7 && config.maxActions == 10) "selected" else ""}>7 to 10</option>
                            <option value='10-15' ${if (config.minActions == 10 && config.maxActions == 15) "selected" else ""}>10 to 15</option>
                            <option value='15-20' ${if (config.minActions == 15 && config.maxActions == 20) "selected" else ""}>15 to 20</option>
                        </select>
                        <small style='color:#888;'>Select a range for actions per combination.</small>
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
                            <span id='pitch-display' style='min-width:48px;display:inline-block;text-align:center;'>${currentPitch / 10.0}</span>
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
        /**
         * Reads the current values from the configuration form inputs and returns a TrainingConfiguration.
         * This is used to apply the user's selected settings.
         */
        val actionIntervalSlider = document.getElementById("action-interval-slider") as? HTMLInputElement
        val comboIntervalSlider = document.getElementById("combo-interval-slider") as? HTMLInputElement
        val modeSelect = document.getElementById("mode-select") as? HTMLSelectElement
        val stanceSelect = document.getElementById("stance-select") as? HTMLSelectElement
        val actionsRangeSelect = document.getElementById("actions-range-select") as? HTMLSelectElement
        val offenseRatioSlider = document.getElementById("offense-ratio-slider") as? HTMLInputElement
        val numberNotationCheck = document.getElementById("number-notation-check") as? HTMLInputElement

        val mode = TrainingMode.valueOf(modeSelect?.value ?: "BOTH")
        val range = actionsRangeSelect?.value?.split("-") ?: listOf("3", "5")
        val minActions = range.getOrNull(0)?.toIntOrNull() ?: 3
        val maxActions = range.getOrNull(1)?.toIntOrNull() ?: 5
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
        /**
         * Sets up the event listener for the training mode select dropdown.
         * Shows or hides the offense ratio slider depending on the selected mode.
         */
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
    /**
     * Sets up all event listeners and display logic for sound controls and sliders.
     * This includes volume, pitch, voice selection, and the min/max actions sliders.
     */
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


        // Volume slider (minimum 5%)
        // Ensures volume never goes below 5%
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
        // Converts pitch slider value (0-20) to float (0.0-2.0)
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
        // Sets up the voice select dropdown and updates when voices change
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
        /**
         * Populates the voice select dropdown with available voices from the browser's speech synthesis API.
         * Keeps the previously selected voice if possible.
         */
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
