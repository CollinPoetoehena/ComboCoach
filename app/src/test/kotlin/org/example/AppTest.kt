package org.example

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AppTest {
    @Test
    fun appHasAGreeting() {
        val classUnderTest = App()
        assertNotNull(classUnderTest.greeting, "app should have a greeting")
        assertTrue(classUnderTest.greeting.contains("ComboCoach"), "greeting should mention ComboCoach")
    }
}
