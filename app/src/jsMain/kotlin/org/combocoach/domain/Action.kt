package org.combocoach.domain

/**
 * Represents a single action in a combination.
 * Actions follow boxing numbering system standard practice.
 * 
 * Standard Boxing Numbering System:
 * 1 = Jab, 2 = Cross, 3 = Lead Hook, 4 = Rear Hook, 5 = Lead Uppercut, 6 = Rear Uppercut
 * 
 * For defensive actions against opponent strikes, we use "D" prefix:
 * D1 = Defend Jab, D2 = Defend Cross, D3 = Defend Hook, D4 = Defend Uppercut
 * 
 */
sealed class Action {
    /**
     * Returns the standard boxing number notation for this action
     */
    abstract fun toNumber(): String
    
    /**
     * Returns the full display name of this action
     */
    abstract fun displayName(): String
    
    /**
     * Returns true if this is an offensive action
     */
    abstract fun isOffensive(): Boolean
    
    /**
     * Returns true if this is a defensive action
     */
    fun isDefensive(): Boolean = !isOffensive()
    
    // Offensive Actions
    data object Jab : Action() {
        override fun toNumber() = "1"
        override fun displayName() = "Jab"
        override fun isOffensive() = true
    }
    
    data object Cross : Action() {
        override fun toNumber() = "2"
        override fun displayName() = "Cross"
        override fun isOffensive() = true
    }
    
    data object LeadHook : Action() {
        override fun toNumber() = "3"
        override fun displayName() = "Lead Hook"
        override fun isOffensive() = true
    }
    
    data object RearHook : Action() {
        override fun toNumber() = "4"
        override fun displayName() = "Rear Hook"
        override fun isOffensive() = true
    }
    
    data object LeadUppercut : Action() {
        override fun toNumber() = "5"
        override fun displayName() = "Lead Uppercut"
        override fun isOffensive() = true
    }
    
    data object RearUppercut : Action() {
        override fun toNumber() = "6"
        override fun displayName() = "Rear Uppercut"
        override fun isOffensive() = true
    }
    
    // Defensive Actions
    data class DefendOpponentAttack(val opponentStrike: OpponentStrike) : Action() {
        override fun toNumber(): String = when (opponentStrike) {
            OpponentStrike.OPPONENT_JAB -> "D1"
            OpponentStrike.OPPONENT_CROSS -> "D2"
            OpponentStrike.OPPONENT_HOOK -> "D3"
            OpponentStrike.OPPONENT_UPPERCUT -> "D4"
        }
        
        override fun displayName(): String = "Defend ${opponentStrike.displayName}"
        override fun isOffensive() = false
    }
    
    companion object {
        /**
         * Returns all offensive actions
         */
        fun allOffensive(): List<Action> = listOf(
            Jab, Cross, LeadHook, RearHook, LeadUppercut, RearUppercut
        )
        
        /**
         * Returns all defensive actions
         */
        fun allDefensive(): List<Action> = OpponentStrike.entries.map { 
            DefendOpponentAttack(it) 
        }
        
        /**
         * Returns all actions
         */
        fun all(): List<Action> = allOffensive() + allDefensive()
    }
}
