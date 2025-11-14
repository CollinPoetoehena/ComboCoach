package org.combocoach.ui.components.molecules

import kotlinx.browser.document
import org.combocoach.domain.Action
import org.w3c.dom.HTMLElement

/**
 * Action display card molecule
 */
object ActionCardMolecule {
    
    fun create(action: Action, index: Int, total: Int): HTMLElement {
        val type = if (action.isOffensive()) "offensive" else "defensive"
        
        return document.createElement("div").apply {
            setAttribute("class", "action-card action-$type")
            innerHTML = """
                <div class="action-number-large">${action.toNumber()}</div>
                <div class="action-name">${action.displayName()}</div>
                <div class="action-counter">Action $index of $total</div>
            """
        } as HTMLElement
    }
    
    fun showWaiting(secondsRemaining: Int): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "waiting-card")
            innerHTML = """
                <div class="waiting-timer">$secondsRemaining</div>
                <div class="waiting-text">Next combination starting...</div>
            """
        } as HTMLElement
    }
}
