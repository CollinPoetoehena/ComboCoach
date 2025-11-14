package org.combocoach.ui.components.molecules

import kotlinx.browser.document
import org.combocoach.domain.Action
import org.w3c.dom.HTMLElement

/**
 * Preview display molecule for combo previews
 */
object PreviewDisplayMolecule {
    
    fun create(combo: List<Action>, formatted: String): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "preview-display")
            innerHTML = """
                <h3>Generated Combination Preview</h3>
                <div class="combo-preview-text">$formatted</div>
                <div class="combo-details">
                    ${combo.mapIndexed { index, action ->
                        val type = if (action.isOffensive()) "offensive" else "defensive"
                        """<span class="action-badge action-$type">${action.toNumber()} - ${action.displayName()}</span>"""
                    }.joinToString("")}
                </div>
            """
        } as HTMLElement
    }
}
