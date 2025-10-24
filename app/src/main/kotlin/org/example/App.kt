package org.example

import org.example.service.ComboTrainer
import org.example.ui.ConsoleUI

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

