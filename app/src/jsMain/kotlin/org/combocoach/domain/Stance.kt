package org.combocoach.domain

/**
 * Represents the fighter's stance position in boxing.
 * The stance determines which hand/foot is forward.
 * 
 * Reference: Standard boxing stances
 * @see <a href="https://www.expertboxing.com/boxing-basics/boxing-stance">Boxing Stance Guide</a>
 */
enum class Stance {
    /** Orthodox stance - left foot forward, right hand is power hand */
    ORTHODOX,
    
    /** Southpaw stance - right foot forward, left hand is power hand */
    SOUTHPAW;
    
    /**
     * Returns the lead hand for this stance
     */
    fun leadHand(): Hand = when (this) {
        ORTHODOX -> Hand.LEFT
        SOUTHPAW -> Hand.RIGHT
    }
    
    /**
     * Returns the rear hand for this stance
     */
    fun rearHand(): Hand = when (this) {
        ORTHODOX -> Hand.RIGHT
        SOUTHPAW -> Hand.LEFT
    }
}

enum class Hand {
    LEFT, RIGHT
}
