package org.example.domain

/**
 * Visual color cues for combinations
 */
enum class ComboColor(val displayName: String, val ansiCode: String) {
    RED("Red", "\u001B[31m"),
    GREEN("Green", "\u001B[32m"),
    YELLOW("Yellow", "\u001B[33m"),
    BLUE("Blue", "\u001B[34m"),
    MAGENTA("Magenta", "\u001B[35m"),
    CYAN("Cyan", "\u001B[36m");

    override fun toString(): String = displayName
    
    fun colorize(text: String): String = "$ansiCode$text\u001B[0m"
}
