package org.combocoach.ui

import kotlinx.browser.document
import kotlinx.dom.clear
import org.combocoach.domain.Combination
import org.combocoach.domain.ComboColor
import org.combocoach.domain.ComboElement
import org.combocoach.service.ComboTrainer
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event

class ComboCoachApp(private val rootElement: Element) {
    private val trainer = ComboTrainer()
    private var currentCombos: List<Combination> = emptyList()
    private var showHistory = false
    private var rounds = 5
    
    fun render() {
        rootElement.innerHTML = ""
        rootElement.appendChild(createMainContainer())
    }
    
    private fun createMainContainer(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "container")
            
            // Header
            appendChild(createHeader())
            
            // Strike Legend
            appendChild(createStrikeLegend())
            
            // Controls
            appendChild(createControls())
            
            // Combinations Display
            appendChild(createCombosDisplay())
        } as HTMLElement
    }
    
    private fun createHeader(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "header")
            innerHTML = """
                <h1>🥊 ComboCoach</h1>
                <p class="subtitle">Your Boxing Training Companion</p>
            """
        } as HTMLElement
    }
    
    private fun createStrikeLegend(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "legend-container")
            innerHTML = """
                <h2>Strike Notation</h2>
                <div class="legend-grid">
                    <div class="legend-item"><span class="strike-number">1</span> - Jab</div>
                    <div class="legend-item"><span class="strike-number">2</span> - Cross</div>
                    <div class="legend-item"><span class="strike-number">3</span> - Lead Hook</div>
                    <div class="legend-item"><span class="strike-number">4</span> - Rear Hook</div>
                    <div class="legend-item"><span class="strike-number">5</span> - Lead Uppercut</div>
                    <div class="legend-item"><span class="strike-number">6</span> - Rear Uppercut</div>
                </div>
            """
        } as HTMLElement
    }
    
    private fun createControls(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "controls")
            
            // Rounds input
            val inputContainer = document.createElement("div").apply {
                setAttribute("class", "input-container")
                innerHTML = """
                    <label for="rounds-input">Rounds:</label>
                    <input type="number" id="rounds-input" value="$rounds" min="1" max="20" />
                """
            }
            val input = inputContainer.querySelector("#rounds-input") as HTMLInputElement
            input.oninput = { 
                rounds = input.value.toIntOrNull() ?: 5
                null
            }
            appendChild(inputContainer)
            
            // Start Training Button
            appendChild(createButton("Start Training ($rounds rounds)", "btn-primary") {
                currentCombos = trainer.startTrainingSession(rounds)
                showHistory = false
                render()
            })
            
            // Random Combo Button
            appendChild(createButton("Random Combo", "btn-danger") {
                currentCombos = listOf(trainer.getRandomCombo())
                showHistory = false
                render()
            })
            
            // Show History Button
            val historyCount = trainer.getHistory().size
            appendChild(createButton(
                if (showHistory) "Hide History" else "Show History ($historyCount)",
                "btn-warning"
            ) {
                showHistory = !showHistory
                currentCombos = if (showHistory) trainer.getHistory() else emptyList()
                render()
            })
            
            // Clear History Button
            appendChild(createButton("Clear History", "btn-secondary") {
                trainer.clearHistory()
                currentCombos = emptyList()
                showHistory = false
                render()
            })
        } as HTMLElement
    }
    
    private fun createButton(text: String, className: String, onClick: (Event) -> Unit): HTMLElement {
        return document.createElement("button").apply {
            this.textContent = text
            setAttribute("class", "btn $className")
            addEventListener("click", onClick)
        } as HTMLElement
    }
    
    private fun createCombosDisplay(): HTMLElement {
        return document.createElement("div").apply {
            setAttribute("class", "combos-display")
            
            if (currentCombos.isNotEmpty()) {
                appendChild(document.createElement("h2").apply {
                    textContent = if (showHistory) "Training History" else "Current Combinations"
                    setAttribute("class", "combos-title")
                })
                
                val grid = document.createElement("div").apply {
                    setAttribute("class", "combos-grid")
                }
                
                currentCombos.forEachIndexed { index, combo ->
                    grid.appendChild(createCombinationCard(index + 1, combo))
                }
                
                appendChild(grid)
            } else {
                appendChild(document.createElement("div").apply {
                    setAttribute("class", "welcome-message")
                    innerHTML = """
                        <p>👊 Click 'Start Training' to generate combinations or 'Random Combo' for a single combo!</p>
                    """
                })
            }
        } as HTMLElement
    }
    
    private fun createCombinationCard(index: Int, combo: Combination): HTMLElement {
        val bgColor = when (combo.color) {
            ComboColor.RED -> "#ff6b6b"
            ComboColor.GREEN -> "#51cf66"
            ComboColor.YELLOW -> "#ffd43b"
            ComboColor.BLUE -> "#4dabf7"
            ComboColor.MAGENTA -> "#cc5de8"
            ComboColor.CYAN -> "#22b8cf"
        }
        
        return document.createElement("div").apply {
            setAttribute("class", "combo-card")
            setAttribute("style", "border-color: $bgColor;")
            
            // Header
            appendChild(document.createElement("div").apply {
                setAttribute("class", "combo-card-header")
                innerHTML = """
                    <h3>Combo #$index</h3>
                    <span class="combo-color-badge" style="background-color: $bgColor;">${combo.color.displayName}</span>
                """
            })
            
            // Elements
            val elementsContainer = document.createElement("div").apply {
                setAttribute("class", "combo-elements")
            }
            
            combo.elements.forEach { element ->
                elementsContainer.appendChild(createElementBadge(element))
            }
            
            appendChild(elementsContainer)
        } as HTMLElement
    }
    
    private fun createElementBadge(element: ComboElement): HTMLElement {
        val (text, bgColor, textColor) = when (element) {
            is ComboElement.Attack -> Triple(
                element.strike.toString(),
                "#4ecdc4",
                "#1a1a1a"
            )
            is ComboElement.Defense -> Triple(
                element.move.toString(),
                "#95e1d3",
                "#1a1a1a"
            )
            is ComboElement.OpponentAttack -> Triple(
                "⚠️ ${element.strike.displayName}",
                "#ff6b6b",
                "#ffffff"
            )
        }
        
        return document.createElement("div").apply {
            setAttribute("class", "element-badge")
            setAttribute("style", "background-color: $bgColor; color: $textColor;")
            textContent = text
        } as HTMLElement
    }
}
