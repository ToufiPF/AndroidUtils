package ch.epfl.toufi.android_utils.ui.preference

import android.os.Bundle
import android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.EditorInfo.TYPE_CLASS_NUMBER
import android.view.inputmethod.EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
import android.widget.EditText
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.get
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withInputType
import androidx.test.espresso.matcher.ViewMatchers.withText
import ch.epfl.toufi.android_test_utils.scenario.SafeFragmentScenario
import ch.epfl.toufi.android_utils.R
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Test

class InputTypeEditTextPreferenceInstrumentedTest {
    companion object {
        private const val PREF_NAME = "InputTypeEditTextPreferenceInstrumentedTest_preferences"
    }

    class Fragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            preferenceManager.sharedPreferencesName = PREF_NAME
            setPreferencesFromResource(R.xml.preference_test_fragment, rootKey)
        }
    }

    private fun runTest(testFun: (SafeFragmentScenario<Fragment>) -> Unit) {
        SafeFragmentScenario.launchInRegularContainer { scenario ->
            testFun(scenario)
        }
    }

    @Test
    fun inputTypeEditTextPreferenceGetsInputTypeFromXML() = runTest { scenario ->
        scenario.onFragment { fragment ->
            val editText =
                fragment.preferenceScreen.get<InputTypeEditTextPreference>("InputTypeEditTextPreference_key")!!

            assertEquals(TYPE_CLASS_NUMBER, editText.inputType)
        }

        onView(withText("InputTypeEditTextPreference_summary"))
            .perform(click())

        onView(allOf(instanceOf(EditText::class.java), withInputType(TYPE_CLASS_NUMBER)))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(typeTextIntoFocusedView("6000"))
    }

    @Test
    fun inputTypeEditTextPreferenceModifiesInputType() = runTest { scenario ->
        scenario.onFragment { fragment ->
            val editText =
                fragment.preferenceScreen.get<InputTypeEditTextPreference>("InputTypeEditTextPreference_key")!!
            editText.inputType = TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }

        onView(withText("InputTypeEditTextPreference_summary"))
            .perform(click())

        onView(allOf(instanceOf(EditText::class.java), withInputType(TYPE_TEXT_VARIATION_EMAIL_ADDRESS)))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(typeTextIntoFocusedView("abc@test.com"))
    }
}
