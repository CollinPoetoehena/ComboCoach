package org.combocoach.ui

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement

/**
 * Manages toast-style notifications in the application.
 * Provides methods to display success, error, info, and warning messages.
 */
object NotificationManager {
    
    enum class NotificationType(val backgroundColor: String, val icon: String) {
        SUCCESS("#4CAF50", "✓"),
        ERROR("#f44336", "✗"),
        INFO("#2196F3", "ℹ"),
        WARNING("#ff9800", "⚠")
    }
    
    /**
     * Show a notification toast with the specified message and type.
     * The notification will automatically disappear after the specified duration.
     * 
     * @param message The message to display
     * @param type The type of notification (SUCCESS, ERROR, INFO, WARNING)
     * @param durationMs Duration in milliseconds before the notification fades out (default: 3000ms)
     */
    fun show(
        message: String, 
        type: NotificationType = NotificationType.SUCCESS,
        durationMs: Int = 3000
    ) {
        console.log("Notification [${type.name}]: $message")
        
        // Create notification element
        val notification = document.createElement("div").apply {
            setAttribute("class", "notification-toast")
            innerHTML = """
                <span class="notification-icon">${type.icon}</span>
                <span class="notification-message">$message</span>
            """.trimIndent()
            setAttribute("style", """
                position: fixed;
                top: 20px;
                right: 20px;
                background: ${type.backgroundColor};
                color: white;
                padding: 16px 24px;
                border-radius: 8px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
                z-index: 1000;
                font-size: 16px;
                font-weight: 500;
                max-width: 350px;
                display: flex;
                align-items: center;
                gap: 12px;
                animation: slideInRight 0.3s ease-out;
            """.trimIndent())
        } as HTMLElement
        
        // Add to body
        document.body?.appendChild(notification)
        
        // Remove after specified duration with fade out
        window.setTimeout({
            notification.setAttribute("style", notification.getAttribute("style") + "; opacity: 0; transition: opacity 0.3s ease-out;")
            window.setTimeout({
                document.body?.removeChild(notification)
            }, 300)
        }, durationMs)
    }
    
    /**
     * Show a success notification (green)
     */
    fun success(message: String, durationMs: Int = 3000) {
        show(message, NotificationType.SUCCESS, durationMs)
    }
    
    /**
     * Show an error notification (red)
     */
    fun error(message: String, durationMs: Int = 4000) {
        show(message, NotificationType.ERROR, durationMs)
    }
    
    /**
     * Show an info notification (blue)
     */
    fun info(message: String, durationMs: Int = 3000) {
        show(message, NotificationType.INFO, durationMs)
    }
    
    /**
     * Show a warning notification (orange)
     */
    fun warning(message: String, durationMs: Int = 3500) {
        show(message, NotificationType.WARNING, durationMs)
    }
}
