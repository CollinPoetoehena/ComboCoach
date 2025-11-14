package org.combocoach.ui.molecules

import kotlinx.browser.document
import org.combocoach.ui.atoms.ButtonAtom
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

/**
 * Control panel with action buttons
 */
object ControlPanelMolecule {
    
    fun create(
        onConfiguration: (Event) -> Unit,
        onStart: (Event) -> Unit,
        onStop: (Event) -> Unit,
        onPreview: (Event) -> Unit
    ): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "control-panel")
            
            appendChild(ButtonAtom.create("⚙️ Configuration", "btn btn-secondary", "config-btn", onConfiguration))
            appendChild(ButtonAtom.create("▶️ Start Training", "btn btn-primary", "start-btn", onStart))
            appendChild(ButtonAtom.create("⏹️ Stop", "btn btn-danger", "stop-btn", onStop))
            appendChild(ButtonAtom.create("👁️ Preview Combo", "btn btn-secondary", "preview-btn", onPreview))
        } as HTMLElement
    }
}
