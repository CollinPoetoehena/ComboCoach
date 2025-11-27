package org.combocoach.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OpponentStrikeTest {
    
    @Test
    fun opponentStrikesHaveCorrectDisplayNames() {
        assertEquals("Opponent Jab", OpponentStrike.OPPONENT_JAB.displayName)
        assertEquals("Opponent Cross", OpponentStrike.OPPONENT_CROSS.displayName)
        assertEquals("Opponent Hook", OpponentStrike.OPPONENT_HOOK.displayName)
        assertEquals("Opponent Uppercut", OpponentStrike.OPPONENT_UPPERCUT.displayName)
    }
    
    @Test
    fun opponentStrikeToStringReturnsDisplayName() {
        assertEquals("Opponent Jab", OpponentStrike.OPPONENT_JAB.toString())
        assertEquals("Opponent Hook", OpponentStrike.OPPONENT_HOOK.toString())
    }
    
    @Test
    fun opponentJabHasAppropriateDefensiveSuggestions() {
        val defenses = OpponentStrike.OPPONENT_JAB.suggestedDefense
        assertTrue(defenses.contains(DefensiveMove.SLIP_LEFT))
        assertTrue(defenses.contains(DefensiveMove.SLIP_RIGHT))
        assertTrue(defenses.contains(DefensiveMove.PARRY))
    }
    
    @Test
    fun opponentCrossHasAppropriateDefensiveSuggestions() {
        val defenses = OpponentStrike.OPPONENT_CROSS.suggestedDefense
        assertTrue(defenses.contains(DefensiveMove.SLIP_LEFT))
        assertTrue(defenses.contains(DefensiveMove.SLIP_RIGHT))
        assertTrue(defenses.contains(DefensiveMove.BLOCK))
    }
    
    @Test
    fun opponentHookHasAppropriateDefensiveSuggestions() {
        val defenses = OpponentStrike.OPPONENT_HOOK.suggestedDefense
        assertTrue(defenses.contains(DefensiveMove.DUCK))
        assertTrue(defenses.contains(DefensiveMove.BLOCK))
        assertTrue(defenses.contains(DefensiveMove.ROLL_LEFT))
    }
    
    @Test
    fun opponentUppercutHasAppropriateDefensiveSuggestions() {
        val defenses = OpponentStrike.OPPONENT_UPPERCUT.suggestedDefense
        assertTrue(defenses.contains(DefensiveMove.BLOCK))
        assertTrue(defenses.contains(DefensiveMove.DUCK))
    }
    
    @Test
    fun allOpponentStrikesHaveAtLeastOneSuggestedDefense() {
        OpponentStrike.entries.forEach { strike ->
            assertTrue(strike.suggestedDefense.isNotEmpty(), 
                "${strike.displayName} should have at least one suggested defense")
        }
    }
    
    @Test
    fun allOpponentStrikesAreAccessible() {
        val allStrikes = OpponentStrike.entries
        assertEquals(4, allStrikes.size)
    }
}
