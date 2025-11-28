package org.combocoach.domain

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertEquals

class PositionTest {
    
    private val orthodoxStance = Stance.ORTHODOX
    private val southpawStance = Stance.SOUTHPAW
    
    @Test
    fun neutralPositionAllowsAllOffensiveActions() {
        val position = Position.NEUTRAL
        
        assertTrue(position.canPerformAction(Action.Jab, orthodoxStance))
        assertTrue(position.canPerformAction(Action.Cross, orthodoxStance))
        assertTrue(position.canPerformAction(Action.LeadHook, orthodoxStance))
        assertTrue(position.canPerformAction(Action.RearHook, orthodoxStance))
        assertTrue(position.canPerformAction(Action.LeadUppercut, orthodoxStance))
        assertTrue(position.canPerformAction(Action.RearUppercut, orthodoxStance))
    }
    
    @Test
    fun neutralPositionAllowsAllDefensiveActions() {
        val position = Position.NEUTRAL
        
        assertTrue(position.canPerformAction(
            Action.DefendOpponentAttack(OpponentStrike.OPPONENT_JAB), 
            orthodoxStance
        ))
        assertTrue(position.canPerformAction(
            Action.DefendOpponentAttack(OpponentStrike.OPPONENT_CROSS), 
            orthodoxStance
        ))
    }
    
    @Test
    fun jabCanBeThrownFromAnyPosition() {
        assertTrue(Position.NEUTRAL.canPerformAction(Action.Jab, orthodoxStance))
        assertTrue(Position.LEAD_EXTENDED.canPerformAction(Action.Jab, orthodoxStance))
        assertTrue(Position.REAR_EXTENDED.canPerformAction(Action.Jab, orthodoxStance))
    }
    
    @Test
    fun defenseCanBePerformedFromAnyPosition() {
        val defense = Action.DefendOpponentAttack(OpponentStrike.OPPONENT_JAB)
        
        assertTrue(Position.NEUTRAL.canPerformAction(defense, orthodoxStance))
        assertTrue(Position.LEAD_EXTENDED.canPerformAction(defense, orthodoxStance))
        assertTrue(Position.REAR_EXTENDED.canPerformAction(defense, orthodoxStance))
    }
    
    @Test
    fun crossAllowedFromNeutralAndLeadExtended() {
        assertTrue(Position.NEUTRAL.canPerformAction(Action.Cross, orthodoxStance))
        assertTrue(Position.LEAD_EXTENDED.canPerformAction(Action.Cross, orthodoxStance))
        assertFalse(Position.REAR_EXTENDED.canPerformAction(Action.Cross, orthodoxStance))
    }
    
    @Test
    fun leadHookAllowedFromNeutralAndRearExtended() {
        assertTrue(Position.NEUTRAL.canPerformAction(Action.LeadHook, orthodoxStance))
        assertTrue(Position.REAR_EXTENDED.canPerformAction(Action.LeadHook, orthodoxStance))
        assertFalse(Position.LEAD_EXTENDED.canPerformAction(Action.LeadHook, orthodoxStance))
    }
    
    @Test
    fun rearHookAllowedFromNeutralAndLeadExtended() {
        assertTrue(Position.NEUTRAL.canPerformAction(Action.RearHook, orthodoxStance))
        assertTrue(Position.LEAD_EXTENDED.canPerformAction(Action.RearHook, orthodoxStance))
        assertFalse(Position.REAR_EXTENDED.canPerformAction(Action.RearHook, orthodoxStance))
    }
    
    @Test
    fun leadUppercutAllowedFromNeutralAndRearExtended() {
        assertTrue(Position.NEUTRAL.canPerformAction(Action.LeadUppercut, orthodoxStance))
        assertTrue(Position.REAR_EXTENDED.canPerformAction(Action.LeadUppercut, orthodoxStance))
        assertFalse(Position.LEAD_EXTENDED.canPerformAction(Action.LeadUppercut, orthodoxStance))
    }
    
    @Test
    fun rearUppercutAllowedFromNeutralAndLeadExtended() {
        assertTrue(Position.NEUTRAL.canPerformAction(Action.RearUppercut, orthodoxStance))
        assertTrue(Position.LEAD_EXTENDED.canPerformAction(Action.RearUppercut, orthodoxStance))
        assertFalse(Position.REAR_EXTENDED.canPerformAction(Action.RearUppercut, orthodoxStance))
    }
    
    @Test
    fun jabTransitionsToLeadExtended() {
        assertEquals(Position.LEAD_EXTENDED, Position.NEUTRAL.after(Action.Jab, orthodoxStance))
        assertEquals(Position.LEAD_EXTENDED, Position.LEAD_EXTENDED.after(Action.Jab, orthodoxStance))
        assertEquals(Position.LEAD_EXTENDED, Position.REAR_EXTENDED.after(Action.Jab, orthodoxStance))
    }
    
    @Test
    fun crossTransitionsToRearExtended() {
        assertEquals(Position.REAR_EXTENDED, Position.NEUTRAL.after(Action.Cross, orthodoxStance))
        assertEquals(Position.REAR_EXTENDED, Position.LEAD_EXTENDED.after(Action.Cross, orthodoxStance))
    }
    
    @Test
    fun leadHandActionsTransitionToLeadExtended() {
        assertEquals(Position.LEAD_EXTENDED, Position.NEUTRAL.after(Action.LeadHook, orthodoxStance))
        assertEquals(Position.LEAD_EXTENDED, Position.NEUTRAL.after(Action.LeadUppercut, orthodoxStance))
    }
    
    @Test
    fun rearHandActionsTransitionToRearExtended() {
        assertEquals(Position.REAR_EXTENDED, Position.NEUTRAL.after(Action.RearHook, orthodoxStance))
        assertEquals(Position.REAR_EXTENDED, Position.NEUTRAL.after(Action.RearUppercut, orthodoxStance))
    }
    
    @Test
    fun defenseTransitionsBackToNeutral() {
        val defense = Action.DefendOpponentAttack(OpponentStrike.OPPONENT_JAB)
        
        assertEquals(Position.NEUTRAL, Position.LEAD_EXTENDED.after(defense, orthodoxStance))
        assertEquals(Position.NEUTRAL, Position.REAR_EXTENDED.after(defense, orthodoxStance))
        assertEquals(Position.NEUTRAL, Position.NEUTRAL.after(defense, orthodoxStance))
    }
    
    @Test
    fun positionLogicWorksForSouthpawStance() {
        // Basic tests to ensure stance parameter is accepted
        assertTrue(Position.NEUTRAL.canPerformAction(Action.Jab, southpawStance))
        assertTrue(Position.LEAD_EXTENDED.canPerformAction(Action.Cross, southpawStance))
        assertEquals(Position.LEAD_EXTENDED, Position.NEUTRAL.after(Action.Jab, southpawStance))
    }
}
