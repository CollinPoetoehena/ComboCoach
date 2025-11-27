package org.combocoach.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class DefensiveMoveTest {
    
    @Test
    fun `defensive moves have correct display names`() {
        assertEquals("Slip Left", DefensiveMove.SLIP_LEFT.displayName)
        assertEquals("Slip Right", DefensiveMove.SLIP_RIGHT.displayName)
        assertEquals("Roll Left", DefensiveMove.ROLL_LEFT.displayName)
        assertEquals("Roll Right", DefensiveMove.ROLL_RIGHT.displayName)
        assertEquals("Duck", DefensiveMove.DUCK.displayName)
        assertEquals("Block", DefensiveMove.BLOCK.displayName)
        assertEquals("Parry", DefensiveMove.PARRY.displayName)
    }
    
    @Test
    fun `defensive move toString returns display name`() {
        assertEquals("Slip Left", DefensiveMove.SLIP_LEFT.toString())
        assertEquals("Duck", DefensiveMove.DUCK.toString())
        assertEquals("Block", DefensiveMove.BLOCK.toString())
    }
    
    @Test
    fun `all defensive moves are accessible`() {
        val allMoves = DefensiveMove.entries
        assertEquals(7, allMoves.size)
    }
}
