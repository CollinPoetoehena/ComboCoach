package org.combocoach.domain

/**
 * Represents opponent strikes that need to be defended against
 */
enum class OpponentStrike(val displayName: String, val suggestedDefense: List<DefensiveMove>) {
    OPPONENT_JAB("Opponent Jab", listOf(DefensiveMove.SLIP_LEFT, DefensiveMove.SLIP_RIGHT, DefensiveMove.PARRY)),
    OPPONENT_CROSS("Opponent Cross", listOf(DefensiveMove.SLIP_LEFT, DefensiveMove.SLIP_RIGHT, DefensiveMove.BLOCK)),
    OPPONENT_HOOK("Opponent Hook", listOf(DefensiveMove.DUCK, DefensiveMove.BLOCK, DefensiveMove.ROLL_LEFT)),
    OPPONENT_UPPERCUT("Opponent Uppercut", listOf(DefensiveMove.BLOCK, DefensiveMove.DUCK));

    override fun toString(): String = displayName
}
