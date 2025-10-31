package org.combocoach.domain

import kotlin.random.Random

/**
 * Represents a boxing combination
 */
data class Combination(
    val id: String = generateId(),
    val elements: List<ComboElement>,
    val color: ComboColor
) {
    override fun toString(): String {
        val elementsStr = elements.joinToString(" → ") { it.toString() }
        return "[$color] $elementsStr"
    }
}

// Simple ID generator for web (replaces UUID)
private fun generateId(): String {
    val timestamp = js("Date.now()") as Double
    val random = Random.nextInt(10000, 99999)
    return "${timestamp.toLong()}-$random"
}
