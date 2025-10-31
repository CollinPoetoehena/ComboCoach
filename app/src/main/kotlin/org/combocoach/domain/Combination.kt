package org.combocoach.domain

import java.util.UUID

/**
 * Represents a boxing combination
 */
data class Combination(
    val id: String = UUID.randomUUID().toString(),
    val elements: List<ComboElement>,
    val color: ComboColor
) {
    override fun toString(): String {
        val elementsStr = elements.joinToString(" → ") { it.toString() }
        return "[$color] $elementsStr"
    }
}
