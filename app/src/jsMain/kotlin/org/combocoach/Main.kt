package org.combocoach

import org.combocoach.ui.ComboCoachApp
import kotlinx.browser.document
import kotlinx.browser.window

fun main() {
    window.onload = {
        val root = document.getElementById("root")
        if (root != null) {
            val app = ComboCoachApp(root)
            app.render()
        }
    }
}
