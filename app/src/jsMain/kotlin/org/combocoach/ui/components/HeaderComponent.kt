package org.combocoach.ui.components

import kotlinx.browser.document
import org.w3c.dom.HTMLElement

/**
 * Creates the header section of the application
 */
object HeaderComponent {
    
    fun create(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "header")
            innerHTML = """
                <h1>🥊 ComboCoach – Flow-Based Interval Training</h1>
            """
        } as HTMLElement
    }
}
