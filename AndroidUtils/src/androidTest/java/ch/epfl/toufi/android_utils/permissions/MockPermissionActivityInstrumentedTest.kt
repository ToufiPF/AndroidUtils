package ch.epfl.toufi.android_utils.permissions

import android.Manifest.permission.BATTERY_STATS
import android.Manifest.permission.INTERNET
import android.Manifest.permission.RECEIVE_MMS
import android.Manifest.permission.RECEIVE_SMS
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.PermissionChecker.PERMISSION_DENIED
import androidx.core.content.PermissionChecker.PERMISSION_DENIED_APP_OP
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.test.core.app.ActivityScenario
import ch.epfl.toufi.android_utils.BackArrowTestActivity
import ch.epfl.toufi.android_utils.permissions.MockPermissionsActivity.Companion.configuredSelfPermissions
import ch.epfl.toufi.android_utils.permissions.MockPermissionsActivity.Companion.configuredShouldShowRationale
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class MockPermissionActivityInstrumentedTest {

    @Before
    fun init() {
        configuredSelfPermissions.clear()
        configuredShouldShowRationale.clear()
    }

    private fun runTest(testFun: (ActivityScenario<out MockPermissionsActivity>) -> Unit) {
        ActivityScenario.launch(BackArrowTestActivity::class.java).use(testFun)
    }

    @Test
    fun checkSelfPermissionsReturnsConfigured() {
        configuredSelfPermissions[RECEIVE_SMS] = PERMISSION_DENIED
        configuredSelfPermissions[INTERNET] = PERMISSION_DENIED_APP_OP
        configuredSelfPermissions[BATTERY_STATS] = PERMISSION_GRANTED

        runTest { scenario ->
            scenario.onActivity { activity ->
                assertEquals(PERMISSION_DENIED, checkSelfPermission(activity, RECEIVE_SMS))
                assertEquals(PERMISSION_DENIED, activity.checkSelfPermission(RECEIVE_SMS))
                assertEquals(PERMISSION_DENIED_APP_OP, checkSelfPermission(activity, INTERNET))
                assertEquals(PERMISSION_DENIED_APP_OP, activity.checkSelfPermission(INTERNET))
                assertEquals(PERMISSION_GRANTED, checkSelfPermission(activity, BATTERY_STATS))
                assertEquals(PERMISSION_GRANTED, activity.checkSelfPermission(BATTERY_STATS))

                // don't care about result, just check it doesn't crash
                checkSelfPermission(activity, RECEIVE_MMS)
                activity.checkSelfPermission(RECEIVE_MMS)
            }
        }
    }

    @Test
    fun shouldShowRationaleReturnsConfigured() {
        configuredShouldShowRationale[RECEIVE_SMS] = false
        configuredShouldShowRationale[INTERNET] = true

        runTest { scenario ->
            scenario.onActivity { activity ->
                assertFalse(activity.shouldShowRequestPermissionRationale(RECEIVE_SMS))
                assertTrue(activity.shouldShowRequestPermissionRationale(INTERNET))

                // don't care about result, just check it doesn't crash
                activity.shouldShowRequestPermissionRationale(RECEIVE_MMS)
            }
        }
    }
}
