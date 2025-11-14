package org.combocoach

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
// TODO: test things like the ratio functionality with multiple tries if it is actually that ratio, etc.
// (I did test it manually and it did seem to work)