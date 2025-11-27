package org.combocoach.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class StanceTest {
    
    @Test
    fun `orthodox stance has left lead hand`() {
        assertEquals(Hand.LEFT, Stance.ORTHODOX.leadHand())
    }
    
    @Test
    fun `orthodox stance has right rear hand`() {
        assertEquals(Hand.RIGHT, Stance.ORTHODOX.rearHand())
    }
    
    @Test
    fun `southpaw stance has right lead hand`() {
        assertEquals(Hand.RIGHT, Stance.SOUTHPAW.leadHand())
    }
    
    @Test
    fun `southpaw stance has left rear hand`() {
        assertEquals(Hand.LEFT, Stance.SOUTHPAW.rearHand())
    }
    
    @Test
    fun `orthodox and southpaw have opposite hands`() {
        assertEquals(Stance.ORTHODOX.leadHand(), Stance.SOUTHPAW.rearHand())
        assertEquals(Stance.ORTHODOX.rearHand(), Stance.SOUTHPAW.leadHand())
    }
}
