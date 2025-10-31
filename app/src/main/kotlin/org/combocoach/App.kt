package org.combocoach

import org.combocoach.service.ComboTrainer
import org.combocoach.ui.ConsoleUI

class App {
    val greeting: String
        get() = "Welcome to ComboCoach - Your Boxing Training Companion!"
    
    fun run() {
        val trainer = ComboTrainer()
        val ui = ConsoleUI(trainer)
        ui.start()
    }
}

fun main() {
    println(App().greeting)
    println()
    App().run()
}

