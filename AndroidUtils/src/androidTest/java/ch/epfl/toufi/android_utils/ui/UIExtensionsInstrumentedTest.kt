package ch.epfl.toufi.android_utils.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.PermissionChecker.PERMISSION_DENIED
import androidx.core.content.PermissionChecker.PERMISSION_DENIED_APP_OP
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import ch.epfl.toufi.android_test_utils.scenario.SafeViewScenario
import ch.epfl.toufi.android_utils.BackArrowTestActivity
import ch.epfl.toufi.android_utils.ui.UIExtensions.checkHasPermissions
import ch.epfl.toufi.android_utils.ui.UIExtensions.parcelable
import ch.epfl.toufi.android_utils.ui.UIExtensions.set
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.parcelize.Parcelize
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class UIExtensionsInstrumentedTest {

    companion object {
        private const val EXTRA_KEY = "test_extra_key"
    }

    @Parcelize
    data class TestParcelable(
        val id: Long,
        val text: String,
    ) : Parcelable


    @Test
    fun editableSetCompletelyReplacesText() {
        SafeViewScenario.launchInRegularContainer({ context -> EditText(context) }) { scenario ->
            scenario.onView { editText ->
                editText.text?.set("New value")
            }

            onView(withText("New value")).check(matches(isDisplayed()))

            scenario.onView { editText ->
                assertEquals("New value", editText.text?.toString())
                editText.text?.set(null)
                assertEquals("", editText.text?.toString() ?: "")
            }
        }
    }

    @Test
    fun intentGetParcelableReturnsObjectEqualToOriginal() {
        val original = TestParcelable(id = 5001459L, text = "Lorem ipsum something")

        val intent = Intent()
        intent.putExtra(EXTRA_KEY, original)

        val res: TestParcelable? = intent.parcelable(EXTRA_KEY)

        assertEquals(original, res)
    }

    @Test
    fun bundleGetParcelableReturnsObjectEqualToOriginal() {
        val original = TestParcelable(id = 5001459L, text = "Lorem ipsum something")

        val bundle = Bundle()
        bundle.putParcelable(EXTRA_KEY, original)

        val res: TestParcelable? = bundle.parcelable(EXTRA_KEY)
        assertEquals(original, res)
    }

    @Test
    fun checkHasPermissionsReturnsTrueIffPermissionGranted() {
        mockkStatic(ContextCompat::class) {
            val permissions = arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.INTERNET,
            )
            every { checkSelfPermission(any(), permissions[0]) } returns PERMISSION_DENIED
            every { checkSelfPermission(any(), permissions[1]) } returns PERMISSION_GRANTED
            every { checkSelfPermission(any(), permissions[2]) } returns PERMISSION_DENIED_APP_OP
            every { checkSelfPermission(any(), permissions[3]) } returns PERMISSION_GRANTED

            val expected = arrayOf(false, true, false, true).toBooleanArray()
            ActivityScenario.launch(BackArrowTestActivity::class.java).use { scenario ->
                scenario.onActivity { activity ->
                    val result = activity.checkHasPermissions(*permissions)

                    assertEquals(permissions.size, result.size)
                    assertArrayEquals(expected, result)
                }
            }
        }
    }
}
