package org.example.service

import org.example.domain.*
import kotlin.random.Random

/**
 * Service for generating random boxing combinations
 */
class CombinationGenerator(
    private val minElements: Int = 2,
    private val maxElements: Int = 6,
    private val includeDefense: Boolean = true,
    private val includeOpponentStrikes: Boolean = true
) {
    
    /**
     * Generates a random boxing combination
     */
    fun generateCombination(): Combination {
        val numberOfElements = Random.nextInt(minElements, maxElements + 1)
        val elements = mutableListOf<ComboElement>()
        
        repeat(numberOfElements) {
            elements.add(generateRandomElement())
        }
        
        val color = ComboColor.entries.random()
        return Combination(elements = elements, color = color)
    }
    
    /**
     * Generates multiple random combinations
     */
    fun generateCombinations(count: Int): List<Combination> {
        return List(count) { generateCombination() }
    }
    
    private fun generateRandomElement(): ComboElement {
        val elementTypes = mutableListOf<() -> ComboElement>()
        
        // Always include strikes
        elementTypes.add { ComboElement.Attack(Strike.entries.random()) }
        
        // Optionally include defensive moves
        if (includeDefense) {
            elementTypes.add { ComboElement.Defense(DefensiveMove.entries.random()) }
        }
        
        // Optionally include opponent strikes
        if (includeOpponentStrikes) {
            elementTypes.add { ComboElement.OpponentAttack(OpponentStrike.entries.random()) }
        }
        
        return elementTypes.random().invoke()
    }
}
