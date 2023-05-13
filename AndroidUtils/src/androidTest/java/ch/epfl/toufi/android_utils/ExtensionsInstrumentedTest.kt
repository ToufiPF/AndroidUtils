package ch.epfl.toufi.android_utils

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import ch.epfl.toufi.android_test_utils.scenario.SafeViewScenario
import ch.epfl.toufi.android_utils.Extensions.parcelable
import ch.epfl.toufi.android_utils.Extensions.set
import kotlinx.parcelize.Parcelize
import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionsInstrumentedTest {

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
}