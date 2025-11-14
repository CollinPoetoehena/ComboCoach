package org.combocoach.ui.components

import kotlinx.browser.document
import org.w3c.dom.HTMLElement

/**
 * Creates the strike notation legend/reference panel
 */
object StrikeLegendComponent {
    
    fun create(isExpanded: Boolean, onToggle: () -> Unit): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "legend-container")
            
            // Header with toggle
            appendChild(createHeader(onToggle))
            
            // Collapsible content
            if (isExpanded) {
                appendChild(createContent())
            }
        } as HTMLElement
    }
    
    private fun createHeader(onToggle: () -> Unit): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "legend-header")
            innerHTML = """
                <h2>📖 Strike Notation Reference</h2>
                <button id="legend-toggle-btn" class="btn-icon">▼</button>
            """
            querySelector("#legend-toggle-btn")?.addEventListener("click", { onToggle() })
        } as HTMLElement
    }
    
    private fun createContent(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "legend-content")
            innerHTML = """
                <p class="legend-description">
                    Standard boxing numbering system 
                    (<a href="https://expertboxing.com/the-beginners-guide-to-boxing" target="_blank">Learn More</a>)
                </p>
                <div class="legend-grid">
                    <div class="legend-item"><span class="strike-number">1</span> - Jab</div>
                    <div class="legend-item"><span class="strike-number">2</span> - Cross</div>
                    <div class="legend-item"><span class="strike-number">3</span> - Lead Hook</div>
                    <div class="legend-item"><span class="strike-number">4</span> - Rear Hook</div>
                    <div class="legend-item"><span class="strike-number">5</span> - Lead Uppercut</div>
                    <div class="legend-item"><span class="strike-number">6</span> - Rear Uppercut</div>
                    <div class="legend-item defense"><span class="strike-number">D1</span> - Defend Jab</div>
                    <div class="legend-item defense"><span class="strike-number">D2</span> - Defend Cross</div>
                    <div class="legend-item defense"><span class="strike-number">D3</span> - Defend Hook</div>
                    <div class="legend-item defense"><span class="strike-number">D4</span> - Defend Uppercut</div>
                </div>
            """
        } as HTMLElement
    }
}
