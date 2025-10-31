package org.combocoach.domain

/**
 * Configuration for combination generation and display.
 * Controls timing, output modes, and training focus.
 */
data class TrainingConfiguration(
    /**
     * Interval between actions in milliseconds (e.g., 1000ms = 1 second)
     * Default: 1000ms (1 second per action)
     */
    val actionIntervalMs: Int = 1000,
    
    /**
     * Training mode - determines what types of actions to include
     */
    val trainingMode: TrainingMode = TrainingMode.BOTH,
    
    /**
     * Output mode - visual display or audio cues
     */
    val outputMode: OutputMode = OutputMode.VISUAL,
    
    /**
     * Use number notation (e.g., "1 2 3") instead of full names
     * Recommended: true for easier reading and audio
     */
    val useNumberNotation: Boolean = true,
    
    /**
     * Fighter's stance (affects which hand is lead/rear)
     */
    val stance: Stance = Stance.ORTHODOX,
    
    /**
     * Minimum number of actions in a combination
     */
    val minActions: Int = 3,
    
    /**
     * Maximum number of actions in a combination
     */
    val maxActions: Int = 8,
    
    /**
     * Ratio of offense to defense (0.0 = all defense, 1.0 = all offense)
     * For BOTH mode, 0.5 means 50/50 split
     * Ignored for ATTACK_ONLY and DEFENSE_ONLY modes
     */
    val offenseRatio: Float = 0.5f
) {
    init {
        require(actionIntervalMs > 0) { "Action interval must be positive" }
        require(minActions > 0) { "Min actions must be positive" }
        require(maxActions >= minActions) { "Max actions must be >= min actions" }
        require(offenseRatio in 0f..1f) { "Offense ratio must be between 0 and 1" }
    }
}

/**
 * Training mode determines focus of practice
 */
enum class TrainingMode {
    /**
     * Attack only - practice offensive combinations
     * Focuses on punch flow and offensive techniques
     */
    ATTACK_ONLY,
    
    /**
     * Defense only - practice defensive movements
     * Focuses on slips, rolls, blocks, and parries
     */
    DEFENSE_ONLY,
    
    /**
     * Both attack and defense - realistic fight combinations
     * Mix of offensive and defensive actions
     */
    BOTH;
    
    fun includeOffense(): Boolean = this == ATTACK_ONLY || this == BOTH
    fun includeDefense(): Boolean = this == DEFENSE_ONLY || this == BOTH
}

/**
 * Output mode for displaying combinations
 */
enum class OutputMode {
    /**
     * Visual display - show combinations on screen
     */
    VISUAL,
    
    /**
     * Audio cues - speak or beep combinations
     * (Implementation depends on text-to-speech or audio library)
     */
    AUDIO,
    
    /**
     * Both visual and audio output
     */
    BOTH
}
