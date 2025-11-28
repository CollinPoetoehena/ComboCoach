package org.combocoach.ui

import kotlinx.browser.window
import org.combocoach.domain.Action

/**
 * Manages audio feedback for the application.
 * Uses Web Speech API (browser-native speech synthesis) via Kotlin/JS interop.
 * Note: This does not rely on a Kotlin audio library — sound is provided
 * directly by the browser’s built-in `speechSynthesis`: https://developer.mozilla.org/en-US/docs/Web/API/SpeechSynthesis
 */
object SoundManager {
    
    private var isEnabled = true
    private var volume = 0.3f // Default volume (0.0 to 1.0)
    private var rate = 1.0 // Speech rate (0.1 to 10)
    private var pitch = 1.0 // Speech pitch (0 to 2)
    private var selectedVoiceIndex: Int? = null // Index of selected voice
    
    // Speech synthesis for text-to-speech
    private val speechSynthesis by lazy {
        try {
            js("window.speechSynthesis")
        } catch (e: Throwable) {
            console.error("Speech Synthesis not supported: $e")
            null
        }
    }
    
    /**
     * Enable or disable all sounds
     */
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
        console.log("Sound ${if (enabled) "enabled" else "disabled"}")
    }
    
    /**
     * Check if sound is enabled
     */
    fun isEnabled(): Boolean = isEnabled
    
    /**
     * Set volume level (0.0 to 1.0)
     */
    fun setVolume(level: Float) {
        volume = level.coerceIn(0f, 1f)
    }
    
    /**
     * Get current volume level
     */
    fun getVolume(): Float = volume.toFloat()
    
    /**
     * Get available voices from the browser's speech synthesis
     */
    fun getVoices(): Array<dynamic> {
        val synth = speechSynthesis ?: return emptyArray()
        return try {
            synth.getVoices() as Array<dynamic>
        } catch (e: Throwable) {
            console.error("Error getting voices: $e")
            emptyArray()
        }
    }
    
    /**
     * Set the voice by index from getVoices() array
     */
    fun setVoice(voiceIndex: Int?) {
        selectedVoiceIndex = voiceIndex
    }
    
    /**
     * Get current voice index
     */
    fun getVoiceIndex(): Int? = selectedVoiceIndex
    
    /**
     * Get current rate
     */
    fun getRate(): Float = rate.toFloat()
    
    /**
     * Auto-calculate and set speech rate based on action interval.
     * See details: https://developer.mozilla.org/en-US/docs/Web/API/SpeechSynthesisUtterance/rate
     * 
     * CRITICAL: Speech rate is automatically adjusted to ensure spoken numbers
     * complete within the action interval time. This prevents sound from overlapping
     * between consecutive actions.
     * 
     * Rate calculation logic:
     * - actionIntervalMs = 500ms (0.5s) -> rate = 2.0 (fast, to fit in short time)
     * - actionIntervalMs = 1000ms (1.0s) -> rate = 1.5 (moderate-fast)
     * - actionIntervalMs = 2000ms (2.0s) -> rate = 1.0 (normal speed)
     * - actionIntervalMs >= 3000ms (3.0s+) -> rate = 0.8 (slightly slower for clarity)
     * 
     * This ensures the speech synthesis completes before the next action is displayed.
     * 
     * @param actionIntervalMs The interval between actions in milliseconds
     */
    fun autoAdjustRateForInterval(actionIntervalMs: Int) {
        rate = when {
            actionIntervalMs <= 500 -> 5.0      // Very fast for 0.5s intervals
            actionIntervalMs <= 750 -> 1.8      // Fast for 0.75s intervals
            actionIntervalMs <= 1000 -> 1.5     // Moderate-fast for 1s intervals
            actionIntervalMs <= 1500 -> 1.2     // Slightly faster for 1.5s intervals
            actionIntervalMs <= 2000 -> 1.0     // Normal for 2s intervals
            else -> 0.8                          // Slightly slower for 3s+ intervals
        }
        console.log("Auto-adjusted speech rate to $rate for ${actionIntervalMs}ms action interval")
    }
    
    /**
     * Set speech rate (0.1 to 10, default 1.0).
     * - The Web Speech API allows controlling how fast the voice speaks.
     * - `speed` is clamped between 0.1 (very slow) and 10 (very fast).
     * - Stored as a Double because the API expects numeric values in that type.
     */
    fun setRate(speed: Float) {
        rate = speed.coerceIn(0.1f, 10f).toDouble()
    }
    
    /**
     * Set speech pitch (0 to 2, default 1.0).
     * - Controls how high or low the synthesized voice sounds.
     * - `pitchLevel` is clamped between 0 (lowest pitch) and 2 (highest pitch).
     * - Stored as a Double for compatibility with the Web Speech API.
     */
    fun setPitch(pitchLevel: Float) {
        pitch = pitchLevel.coerceIn(0f, 2f).toDouble()
    }
    
    /**
     * Get current pitch
     */
    fun getPitch(): Float = pitch.toFloat()
    
    /**
     * Speak the action text (e.g., "1", "2", "D1", "D4").
     * - This is the entry point for audio feedback when an Action occurs.
     * - First checks if sound is enabled; if disabled, it does nothing.
     * - Converts the Action object into its numeric/string representation
     *   using `action.toNumber()`.
     * - Passes the resulting text to the private `speak()` function,
     *   which handles the actual Web Speech API call.
     */
    fun speakAction(action: Action) {
        if (!isEnabled) return
        
        val text = action.toNumber().toString()
        speak(text)
    }

    /**
     * Use Text-to-Speech to speak the given text.
     *
     * @param text The text to speak (e.g., "1", "2", "D1", "D4").
     * This function wraps the browser's Web Speech API (SpeechSynthesis)
     * to convert text into audible speech. It applies the configured
     * volume, rate, and pitch settings before playback.
     * 
     * NOTE: See above documentation link for the Speech API details.
     */
    private fun speak(text: String) {
        // Retrieve the speech synthesis engine from the browser.
        // If it's not available (e.g., unsupported browser), exit early.
        val synth = speechSynthesis ?: return

        try {
            // Cancel any ongoing speech before starting a new one.
            // This ensures that repeated calls don't overlap or queue up.
            synth.cancel()

            // Create a new speech utterance object with the given text.
            // SpeechSynthesisUtterance is the Web Speech API class that
            // represents a single spoken phrase.
            val utterance = js("new SpeechSynthesisUtterance(text)")

            // Apply audio settings: https://developer.mozilla.org/en-US/docs/Web/API/SpeechSynthesisUtterance#instance_properties
            // - volume: loudness (0.0 = mute, 1.0 = max)
            // - rate: speed of speech (0.1 = very slow, 10 = very fast)
            // - pitch: voice pitch (0 = lowest, 2 = highest)
            // - voice: selected voice from available options
            utterance.volume = volume.toDouble()
            utterance.rate = rate
            utterance.pitch = pitch
            // Set voice if one is selected
            if (selectedVoiceIndex != null) {
                val voices = synth.getVoices()
                val voicesLength = voices.length as Int
                if (selectedVoiceIndex!! < voicesLength) {
                    utterance.voice = voices[selectedVoiceIndex!!]
                }
            }

            // Send the utterance to the speech synthesis engine.
            // The browser will now speak the text aloud using the
            // configured voice and settings.
            synth.speak(utterance)

        } catch (e: Throwable) {
            // Log any errors that occur during speech synthesis.
            // This helps diagnose issues like unsupported features
            // or runtime failures in the browser.
            console.error("Error speaking text '$text': $e")
        }
    }
}
