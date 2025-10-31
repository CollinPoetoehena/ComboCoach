package org.combocoach.domain

/**
 * Sealed class representing elements that can appear in a combination
 */
sealed class ComboElement {
    data class Attack(val strike: Strike) : ComboElement() {
        override fun toString(): String = strike.toString()
    }
    
    data class Defense(val move: DefensiveMove) : ComboElement() {
        override fun toString(): String = move.toString()
    }
    
    data class OpponentAttack(val strike: OpponentStrike) : ComboElement() {
        override fun toString(): String = "⚠️ ${strike.displayName}"
    }
}
