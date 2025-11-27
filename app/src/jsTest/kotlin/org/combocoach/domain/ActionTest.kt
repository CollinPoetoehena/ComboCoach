package org.combocoach.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class ActionTest {
    
    @Test
    fun `offensive actions return correct number notation`() {
        assertEquals("1", Action.Jab.toNumber())
        assertEquals("2", Action.Cross.toNumber())
        assertEquals("3", Action.LeadHook.toNumber())
        assertEquals("4", Action.RearHook.toNumber())
        assertEquals("5", Action.LeadUppercut.toNumber())
        assertEquals("6", Action.RearUppercut.toNumber())
    }
    
    @Test
    fun `offensive actions return correct display names`() {
        assertEquals("Jab", Action.Jab.displayName())
        assertEquals("Cross", Action.Cross.displayName())
        assertEquals("Lead Hook", Action.LeadHook.displayName())
        assertEquals("Rear Hook", Action.RearHook.displayName())
        assertEquals("Lead Uppercut", Action.LeadUppercut.displayName())
        assertEquals("Rear Uppercut", Action.RearUppercut.displayName())
    }
    
    @Test
    fun `offensive actions are marked as offensive`() {
        assertTrue(Action.Jab.isOffensive())
        assertTrue(Action.Cross.isOffensive())
        assertTrue(Action.LeadHook.isOffensive())
        assertTrue(Action.RearHook.isOffensive())
        assertTrue(Action.LeadUppercut.isOffensive())
        assertTrue(Action.RearUppercut.isOffensive())
    }
    
    @Test
    fun `offensive actions are not defensive`() {
        assertFalse(Action.Jab.isDefensive())
        assertFalse(Action.Cross.isDefensive())
        assertFalse(Action.LeadHook.isDefensive())
    }
    
    @Test
    fun `defensive actions return correct number notation`() {
        assertEquals("D1", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_JAB).toNumber())
        assertEquals("D2", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_CROSS).toNumber())
        assertEquals("D3", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_HOOK).toNumber())
        assertEquals("D4", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_UPPERCUT).toNumber())
    }
    
    @Test
    fun `defensive actions return correct display names`() {
        assertEquals("Defend Opponent Jab", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_JAB).displayName())
        assertEquals("Defend Opponent Cross", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_CROSS).displayName())
        assertEquals("Defend Opponent Hook", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_HOOK).displayName())
        assertEquals("Defend Opponent Uppercut", Action.DefendOpponentAttack(OpponentStrike.OPPONENT_UPPERCUT).displayName())
    }
    
    @Test
    fun `defensive actions are not offensive`() {
        assertFalse(Action.DefendOpponentAttack(OpponentStrike.OPPONENT_JAB).isOffensive())
        assertFalse(Action.DefendOpponentAttack(OpponentStrike.OPPONENT_CROSS).isOffensive())
    }
    
    @Test
    fun `defensive actions are marked as defensive`() {
        assertTrue(Action.DefendOpponentAttack(OpponentStrike.OPPONENT_JAB).isDefensive())
        assertTrue(Action.DefendOpponentAttack(OpponentStrike.OPPONENT_CROSS).isDefensive())
    }
    
    @Test
    fun `allOffensive returns all 6 offensive actions`() {
        val offensive = Action.allOffensive()
        assertEquals(6, offensive.size)
        assertTrue(offensive.all { it.isOffensive() })
    }
    
    @Test
    fun `allDefensive returns all 4 defensive actions`() {
        val defensive = Action.allDefensive()
        assertEquals(4, defensive.size)
        assertTrue(defensive.all { it.isDefensive() })
    }
    
    @Test
    fun `all returns all 10 actions`() {
        val all = Action.all()
        assertEquals(10, all.size)
        assertEquals(6, all.count { it.isOffensive() })
        assertEquals(4, all.count { it.isDefensive() })
    }
}
