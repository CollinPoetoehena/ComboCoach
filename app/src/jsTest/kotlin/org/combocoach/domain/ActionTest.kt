package org.combocoach.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class ActionTest {
    
    @Test
    fun offensiveActionsReturnCorrectNumberNotation() {
        assertEquals("1", Action.Jab.toNumber())
        assertEquals("2", Action.Cross.toNumber())
        assertEquals("3", Action.LeadHook.toNumber())
        assertEquals("4", Action.RearHook.toNumber())
        assertEquals("5", Action.LeadUppercut.toNumber())
        assertEquals("6", Action.RearUppercut.toNumber())
    }
    
    @Test
    fun offensiveActionsReturnCorrectDisplayNames() {
        assertEquals("Jab", Action.Jab.displayName())
        assertEquals("Cross", Action.Cross.displayName())
        assertEquals("Lead Hook", Action.LeadHook.displayName())
        assertEquals("Rear Hook", Action.RearHook.displayName())
        assertEquals("Lead Uppercut", Action.LeadUppercut.displayName())
        assertEquals("Rear Uppercut", Action.RearUppercut.displayName())
    }
    
    @Test
    fun offensiveActionsAreMarkedAsOffensive() {
        assertTrue(Action.Jab.isOffensive())
        assertTrue(Action.Cross.isOffensive())
        assertTrue(Action.LeadHook.isOffensive())
        assertTrue(Action.RearHook.isOffensive())
        assertTrue(Action.LeadUppercut.isOffensive())
        assertTrue(Action.RearUppercut.isOffensive())
    }
    
    @Test
    fun offensiveActionsAreNotDefensive() {
        assertFalse(Action.Jab.isDefensive())
        assertFalse(Action.Cross.isDefensive())
        assertFalse(Action.LeadHook.isDefensive())
    }
    
    @Test
    fun defensiveActionsReturnCorrectNumberNotation() {
        assertEquals("D1", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_JAB).toNumber())
        assertEquals("D2", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_CROSS).toNumber())
        assertEquals("D3", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_HOOK).toNumber())
        assertEquals("D4", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_UPPERCUT).toNumber())
    }
    
    @Test
    fun defensiveActionsReturnCorrectDisplayNames() {
        assertEquals("Defend Opponent Jab", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_JAB).displayName())
        assertEquals("Defend Opponent Cross", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_CROSS).displayName())
        assertEquals("Defend Opponent Hook", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_HOOK).displayName())
        assertEquals("Defend Opponent Uppercut", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_UPPERCUT).displayName())
    }
    
    @Test
    fun defensiveActionsAreNotOffensive() {
        assertFalse(Action.DefendOpponentAttack(OpponentStrike.OPPONENT_JAB).isOffensive())
        assertFalse(Action.DefendOpponentAttack(OpponentStrike.OPPONENT_CROSS).isOffensive())
    }
    
    @Test
    fun defensiveActionsAreMarkedAsDefensive() {
        assertTrue(Action.DefendOpponentAttack(OpponentStrike.OPPONENT_JAB).isDefensive())
        assertTrue(Action.DefendOpponentAttack(OpponentStrike.OPPONENT_CROSS).isDefensive())
    }
    
    @Test
    fun allOffensiveReturnsAll6OffensiveActions() {
        val offensive = Action.allOffensive()
        assertEquals(6, offensive.size)
        assertTrue(offensive.all { it.isOffensive() })
    }
    
    @Test
    fun allDefensiveReturnsAll4DefensiveActions() {
        val defensive = Action.allDefensive()
        assertEquals(4, defensive.size)
        assertTrue(defensive.all { it.isDefensive() })
    }
    
    @Test
    fun allReturnsAll10Actions() {
        val all = Action.all()
        assertEquals(10, all.size)
        assertEquals(6, all.count { it.isOffensive() })
        assertEquals(4, all.count { it.isDefensive() })
    }
}
