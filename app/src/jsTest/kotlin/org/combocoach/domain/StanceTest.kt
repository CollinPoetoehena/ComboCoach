package org.combocoach.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class StanceTest {
    
    @Test
    fun orthodoxStanceHasLeftLeadHand() {
        assertEquals(Hand.LEFT, Stance.ORTHODOX.leadHand())
    }
    
    @Test
    fun orthodoxStanceHasRightRearHand() {
        assertEquals(Hand.RIGHT, Stance.ORTHODOX.rearHand())
    }
    
    @Test
    fun southpawStanceHasRightLeadHand() {
        assertEquals(Hand.RIGHT, Stance.SOUTHPAW.leadHand())
    }
    
    @Test
    fun southpawStanceHasLeftRearHand() {
        assertEquals(Hand.LEFT, Stance.SOUTHPAW.rearHand())
    }
    
    @Test
    fun orthodoxAndSouthpawHaveOppositeHands() {
        assertEquals(Stance.ORTHODOX.leadHand(), Stance.SOUTHPAW.rearHand())
        assertEquals(Stance.ORTHODOX.rearHand(), Stance.SOUTHPAW.leadHand())
    }
}
