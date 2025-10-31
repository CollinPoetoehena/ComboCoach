package org.combocoach.domain

/**
 * Represents defensive moves in boxing
 */
enum class DefensiveMove(val displayName: String) {
    SLIP_LEFT("Slip Left"),
    SLIP_RIGHT("Slip Right"),
    ROLL_LEFT("Roll Left"),
    ROLL_RIGHT("Roll Right"),
    DUCK("Duck"),
    BLOCK("Block"),
    PARRY("Parry");

    override fun toString(): String = displayName
}
