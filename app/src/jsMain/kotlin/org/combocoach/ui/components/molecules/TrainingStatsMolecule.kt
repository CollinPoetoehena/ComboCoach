package org.combocoach.ui.components.molecules

import kotlinx.browser.document
import org.combocoach.domain.Action
import org.w3c.dom.HTMLElement

/**
 * Training stats display molecule
 */
object TrainingStatsMolecule {
    
    fun create(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "stats-row")
            innerHTML = """
                <div class="stat-box">
                    <div class="stat-label">Combos Completed</div>
                    <div class="stat-value" id="combos-completed">0</div>
                </div>
            """
        } as HTMLElement
    }
    
    fun updateCompletedCombos(comboNumber: Int) {
        val statsValue = document.getElementById("combos-completed")
        statsValue?.textContent = comboNumber.toString()
    }
}
