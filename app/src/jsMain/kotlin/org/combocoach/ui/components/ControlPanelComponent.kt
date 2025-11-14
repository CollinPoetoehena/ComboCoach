package org.combocoach.ui.components

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

/**
 * Creates and manages the control panel with training action buttons
 */
object ControlPanelComponent {
    
    fun create(
        onStart: (Event) -> Unit,
        onStop: (Event) -> Unit,
        onPreview: (Event) -> Unit
    ): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "controls")
            
            appendChild(createButton("▶️ Start Training", "btn-primary", "start-btn", onStart))
            appendChild(createButton("⏹️ Stop", "btn-danger", "stop-btn", onStop))
            appendChild(createButton("👁️ Preview Combo", "btn-secondary", onClick = onPreview))
        } as HTMLElement
    }
    
    fun disableStartButton() {
        val btn = document.getElementById("start-btn") as? HTMLElement
        btn?.setAttribute("disabled", "true")
        btn?.setAttribute("style", "opacity: 0.6; cursor: not-allowed;")
    }
    
    fun enableStartButton() {
        val btn = document.getElementById("start-btn") as? HTMLElement
        btn?.removeAttribute("disabled")
        btn?.setAttribute("style", "opacity: 1; cursor: pointer;")
    }
    
    private fun createButton(
        text: String,
        className: String,
        id: String = "",
        onClick: (Event) -> Unit
    ): HTMLElement {
        return document.createElement("button").apply {
            this.textContent = text
            setAttribute("class", "btn $className")
            if (id.isNotEmpty()) {
                setAttribute("id", id)
            }
            addEventListener("click", onClick)
        } as HTMLElement
    }
}
