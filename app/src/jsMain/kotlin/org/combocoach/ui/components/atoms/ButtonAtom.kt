package org.combocoach.ui.components.atoms

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

/**
 * Atomic component for creating buttons
 */
object ButtonAtom {
    
    fun create(
        text: String,
        className: String = "btn",
        id: String = "",
        onClick: (Event) -> Unit
    ): HTMLElement {
        return document.createElement("button").apply {
            this.textContent = text
            setAttribute("class", className)
            if (id.isNotEmpty()) {
                setAttribute("id", id)
            }
            addEventListener("click", onClick)
        } as HTMLElement
    }
    
    fun setEnabled(buttonId: String, enabled: Boolean) {
        val btn = document.getElementById(buttonId) as? HTMLElement
        if (enabled) {
            btn?.removeAttribute("disabled")
            btn?.setAttribute("style", "opacity: 1; cursor: pointer;")
        } else {
            btn?.setAttribute("disabled", "true")
            btn?.setAttribute("style", "opacity: 0.6; cursor: not-allowed;")
        }
    }
}
