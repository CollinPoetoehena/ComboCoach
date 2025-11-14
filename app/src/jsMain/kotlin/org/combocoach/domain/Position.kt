package org.combocoach.domain

/**
 * Represents the fighter's body position during a combination.
 * Position determines what actions can flow naturally from the current state.
 * 
 * In boxing, certain punches and movements flow naturally based on weight distribution
 * and body rotation. This class tracks these transitions.
 * 
 * Reference: Boxing combination flow principles
 * @see <a href="https://www.expertboxing.com/boxing-techniques/punch-techniques/how-to-throw-punching-combinations">Punching Combinations</a>
 */
enum class Position {
    /**
     * Neutral/starting position - balanced stance with weight centered.
     * From here, any action can be performed.
     */
    NEUTRAL,
    
    /**
     * Position after throwing a lead hand action (e.g., jab, lead hook).
     * Weight is shifted forward, body is rotated toward lead side.
     * Natural follow-ups: rear hand strikes, slips to rear side.
     */
    LEAD_EXTENDED,
    
    /**
     * Position after throwing a rear hand action (e.g., cross, rear hook).
     * Weight is shifted back, body is rotated toward rear side.
     * Natural follow-ups: lead hand strikes, slips to lead side.
     */
    REAR_EXTENDED;
    
    /**
     * Returns true if the action can be performed from this position
     */
    fun canPerformAction(action: Action, stance: Stance): Boolean {
        return when (action) {
            // Jab can be thrown multiple times in succession
            is Action.Jab -> true
            
            // Defense can be performed from any position, returns to neutral
            is Action.DefendOpponentAttack -> true
            
            // Other actions depend on position and stance
            is Action.Cross -> this == NEUTRAL || this == LEAD_EXTENDED
            is Action.LeadHook -> this == NEUTRAL || this == REAR_EXTENDED
            is Action.RearHook -> this == NEUTRAL || this == LEAD_EXTENDED
            is Action.LeadUppercut -> this == NEUTRAL || this == REAR_EXTENDED
            is Action.RearUppercut -> this == NEUTRAL || this == LEAD_EXTENDED
        }
    }
    
    /**
     * Returns the position after performing the given action
     */
    fun after(action: Action, stance: Stance): Position {
        return when (action) {
            // Defense always returns to neutral
            is Action.DefendOpponentAttack -> NEUTRAL
            
            // Jab keeps you in lead extended or transitions to it
            is Action.Jab -> LEAD_EXTENDED
            
            // Lead hand actions
            is Action.LeadHook, is Action.LeadUppercut -> LEAD_EXTENDED
            
            // Rear hand actions
            is Action.Cross, is Action.RearHook, is Action.RearUppercut -> REAR_EXTENDED
        }
    }
}
