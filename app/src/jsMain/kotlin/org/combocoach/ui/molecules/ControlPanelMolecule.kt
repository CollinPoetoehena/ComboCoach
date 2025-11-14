package org.combocoach.ui.molecules

import kotlinx.browser.document
import org.combocoach.ui.atoms.ButtonAtom
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

/**
 * Control panel with two sections:
 * 1. Training Controls - Start, Pause, Stop buttons
 * 2. Information Controls - Configuration, Preview, Strike Notation
 */
object ControlPanelMolecule {
    
    fun create(
        onConfiguration: (Event) -> Unit,
        onStart: (Event) -> Unit,
        onPause: (Event) -> Unit,
        onStop: (Event) -> Unit,
        onPreview: (Event) -> Unit,
        onStrikeLegend: (Event) -> Unit
    ): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "control-panel")
            
            // Training Controls Section
            appendChild(createTrainingControls(onStart, onPause, onStop))
            
            // Divider
            appendChild(document.createElement("div").apply {
                setAttribute("class", "control-divider")
            })
            
            // Information Controls Section
            appendChild(createInfoControls(onConfiguration, onPreview, onStrikeLegend))
        } as HTMLElement
    }
    
    private fun createTrainingControls(
        onStart: (Event) -> Unit,
        onPause: (Event) -> Unit,
        onStop: (Event) -> Unit
    ): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "training-controls")
            
            appendChild(ButtonAtom.create("▶️ Start", "btn btn-success", "start-btn", onStart))
            
            // Create pause button but hide it initially
            appendChild(ButtonAtom.create("⏸️ Pause", "btn btn-warning", "pause-btn", onPause).apply {
                setAttribute("style", "display: none;")
            })
            
            // Create stop button but hide it initially
            appendChild(ButtonAtom.create("⏹️ Stop", "btn btn-danger", "stop-btn", onStop).apply {
                setAttribute("style", "display: none;")
            })
        } as HTMLElement
    }
    
    private fun createInfoControls(
        onConfiguration: (Event) -> Unit,
        onPreview: (Event) -> Unit,
        onStrikeLegend: (Event) -> Unit
    ): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "info-controls")
            
            appendChild(ButtonAtom.create("⚙️ Config", "btn btn-secondary", "config-btn", onConfiguration))
            appendChild(ButtonAtom.create("👁️ Preview", "btn btn-secondary", "preview-btn", onPreview))
            appendChild(ButtonAtom.create("🥊 Strikes", "btn btn-secondary", "strike-legend-btn", onStrikeLegend))
        } as HTMLElement
    }
}
