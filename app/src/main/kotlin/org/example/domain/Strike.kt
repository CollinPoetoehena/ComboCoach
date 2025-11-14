package org.example.domain

/**
 * Represents different types of strikes in boxing
 */
enum class Strike(val code: Int, val displayName: String) {
    JAB(1, "Jab"),
    CROSS(2, "Cross"),
    LEAD_HOOK(3, "Lead Hook"),
    REAR_HOOK(4, "Rear Hook"),
    LEAD_UPPERCUT(5, "Lead Uppercut"),
    REAR_UPPERCUT(6, "Rear Uppercut");

    override fun toString(): String = "$code - $displayName"
}
