package ch.epfl.android_test_utils.ui.notifications

import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until

object NotificationsActions {

    /**
     * Shortcut to get the [UiDevice].
     * @see UiDevice.getInstance
     */
    fun getUiDevice(): UiDevice = UiDevice.getInstance(getInstrumentation())

    /**
     * Clears all notifications and closes the Notification Panel if open
     */
    fun clearAllNotifications() {
        NotificationManagerCompat.from(getApplicationContext()).cancelAll()
        closeNotificationPanel()
    }

    /**
     * Closes the notification panel if open
     */
    fun closeNotificationPanel() {
        // Causes SecurityException in normal uses, but it's authorized in android tests
        @Suppress("DEPRECATION") val intent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        getApplicationContext<Context>().sendBroadcast(intent)
    }

    /**
     * Waits for a [UiObject2] matching [matcher] to appear on the screen,
     * and returns it.
     * @param matcher the [BySelector] matcher to satisfy
     * @param timeout (Long) timeout in ms
     * @param throwIfNotFound (Boolean) whether to throw if timeout is exceeded,
     * or to simply return null
     */
    fun waitAndFind(
        matcher: BySelector, timeout: Long = 2000L, throwIfNotFound: Boolean = true
    ): UiObject2? {
        val device = getUiDevice()

        val found = device.wait(Until.hasObject(matcher), timeout) ?: false
        if (throwIfNotFound && !found) throw RuntimeException(
            "Waited ${timeout}ms for an object matching $matcher to appear, in vain."
        )
        return device.findObject(matcher)
    }
}