package org.combocoach.ui.molecules

import kotlinx.browser.document
import org.w3c.dom.HTMLElement

/**
 * Strike notation legend/reference molecule
 * Displays the standard boxing numbering system
 */
object StrikeLegendMolecule {
    
    fun create(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "strike-legend")
            innerHTML = """
                <h2>📖 Strike Notation Reference</h2>
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
