package org.combocoach.service

import org.combocoach.domain.*
import kotlin.random.Random

/**
 * Advanced combination generator that respects boxing flow and position transitions.
 * 
 * This generator ensures combinations flow naturally by:
 * - Tracking the fighter's position after each action
 * - Only allowing actions that can be performed from the current position
 * - Maintaining proper weight distribution and body mechanics
 * - Respecting stance (orthodox vs southpaw)
 * 
 * Special rules:
 * - Jab can be thrown multiple times in succession
 * - Defensive actions can be performed from any position and reset to neutral
 * - Each action naturally flows into valid follow-up actions
 * 
 * References:
 * @see <a href="https://www.expertboxing.com/boxing-techniques/punch-techniques/how-to-throw-punching-combinations">Natural Punch Flow</a>
 */
class FlowCombinationGenerator(
    private val config: TrainingConfiguration = TrainingConfiguration()
) {
    
    /**
     * Generates a single action for the combination based on current position
     */
    fun generateNextAction(currentPosition: Position): Action {
        val availableActions = getAvailableActions(currentPosition)
        return availableActions.random()
    }
    
    /**
     * Generates a complete flowing combination
     */
    fun generateFlowingCombination(): List<Action> {
        val numActions = Random.nextInt(config.minActions, config.maxActions + 1)
        val actions = mutableListOf<Action>()
        var currentPosition = Position.NEUTRAL
        
        repeat(numActions) {
            val action = generateNextAction(currentPosition)
            actions.add(action)
            currentPosition = currentPosition.after(action, config.stance)
        }
        
        return actions
    }
    
    /**
     * Returns all actions that can be performed from the given position
     */
    private fun getAvailableActions(position: Position): List<Action> {
        return when (config.trainingMode) {
            TrainingMode.ATTACK_ONLY -> {
                // Only offensive actions that can be performed from current position
                Action.allOffensive().filter { 
                    position.canPerformAction(it, config.stance) 
                }
            }
            
            TrainingMode.DEFENSE_ONLY -> {
                // Only defensive actions (always available from any position)
                Action.allDefensive()
            }
            
            TrainingMode.BOTH -> {
                // Mix based on offense ratio
                val shouldBeOffensive = Random.nextFloat() < config.offenseRatio
                
                if (shouldBeOffensive) {
                    val offensiveActions = Action.allOffensive().filter { 
                        position.canPerformAction(it, config.stance) 
                    }
                    // If no offensive actions available from this position, use defensive
                    if (offensiveActions.isNotEmpty()) {
                        offensiveActions
                    } else {
                        Action.allDefensive()
                    }
                } else {
                    Action.allDefensive()
                }
            }
        }
    }
    
    /**
     * Validates that a combination flows properly
     */
    fun validateFlow(actions: List<Action>): Boolean {
        var currentPosition = Position.NEUTRAL
        
        for (action in actions) {
            if (!currentPosition.canPerformAction(action, config.stance)) {
                return false
            }
            currentPosition = currentPosition.after(action, config.stance)
        }
        
        return true
    }
    
    /**
     * Formats actions for display based on configuration
     */
    fun formatActions(actions: List<Action>): String {
        return if (config.useNumberNotation) {
            actions.joinToString(" ") { it.toNumber() }
        } else {
            actions.joinToString(" → ") { it.displayName() }
        }
    }
}
