package ch.epfl.toufi.android_utils.ui.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.toufi.android_test_utils.espresso.ViewActions2.clickRelative
import ch.epfl.toufi.android_utils.BackArrowTestActivity
import ch.epfl.toufi.android_utils.R
import ch.epfl.toufi.android_utils.ui.activity.ConfirmationDialogExtensions.showConfirmationDialog
import ch.epfl.toufi.android_utils.ui.activity.ConfirmationDialogExtensions.showConfirmationDialogWithDoNotShowAgain
import ch.epfl.toufi.android_utils.ui.activity.ConfirmationDialogExtensions.showYesNoDialogWithDoNotAskAgain
import org.hamcrest.Matchers.any
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class ConfirmationDialogExtensionsInstrumentedTest {
    companion object {
        private const val PREF_NAME: String = "test_preferences"
        private const val PREF_KEY: String = "show_confirmation_dialog"
    }

    private val counter = AtomicInteger(0)
    private val incrementCounter: () -> Unit = { counter.getAndIncrement() }
    private val incrementOrDecrement: (Boolean) -> Unit = { yes: Boolean ->
        counter.addAndGet(if (yes) 1 else -1)
    }

    private lateinit var context: Context
    private lateinit var preferences: SharedPreferences

    private fun onDialogText(@StringRes text: Int) = onView(withText(text)).inRoot(isDialog())
    private fun onDialogText(text: String) = onView(withText(text)).inRoot(isDialog())

    @get:Rule
    val rule = ActivityScenarioRule(BackArrowTestActivity::class.java)

    @Before
    fun init() {
        counter.getAndSet(0)

        context = ApplicationProvider.getApplicationContext()
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        preferences.edit().clear().commit()
    }

    @After
    fun clean() {
        preferences.edit().clear().commit()
    }

    // ---------------------------------------------------------------------------------------------
    // Base confirmation dialog
    @Test
    fun confirmationDialogShowsTitleMessageConfirmCancel() {
        rule.scenario.onActivity { activity ->
            activity.showConfirmationDialog(
                android.R.string.untitled,
                android.R.string.unknownName,
                incrementCounter,
            )
        }
        // clicking outside the dialog should not cancel it
        onView(isRoot()).inRoot(isDialog()).perform(clickRelative(0, -100))

        onDialogText(android.R.string.untitled).check(matches(isDisplayed()))
        onDialogText(android.R.string.unknownName).check(matches(isDisplayed()))
        onDialogText(R.string.confirm).check(matches(isDisplayed()))
        onDialogText(R.string.cancel).check(matches(isDisplayed()))

        onDialogText(R.string.cancel).perform(click())

        // onDialogText would fail b/c root isDialog does not exist anymore
        onView(withText(android.R.string.untitled)).inRoot(any(Root::class.java))
            .check(doesNotExist())
        // cancelled, callback shouldn't have been called
        assertEquals(0, counter.get())
    }

    @Test
    fun confirmationDialogCallsCallbackOnConfirm() {
        rule.scenario.onActivity { activity ->
            activity.showConfirmationDialog(
                android.R.string.untitled,
                android.R.string.unknownName,
                incrementCounter,
            )
        }

        onDialogText(R.string.confirm).perform(click())
        assertEquals(1, counter.get())
    }

    // ---------------------------------------------------------------------------------------------
    // "Do not show again" confirmation dialog
    @Test
    fun doNotShowAgainShowsTitleMessageConfirmCancelCheckbox() {
        rule.scenario.onActivity { activity ->
            activity.showConfirmationDialogWithDoNotShowAgain(
                android.R.string.untitled,
                android.R.string.unknownName,
                preferences,
                PREF_KEY,
                incrementCounter,
            )
        }
        // clicking outside the dialog should not cancel it
        onView(isRoot()).inRoot(isDialog()).perform(clickRelative(0, -100))

        onDialogText(android.R.string.untitled).check(matches(isDisplayed()))
        onDialogText(android.R.string.unknownName).check(matches(isDisplayed()))
        onDialogText(R.string.cancel).check(matches(isDisplayed()))
        onDialogText(R.string.confirm).check(matches(isDisplayed()))
        onDialogText(R.string.do_not_show_again).check(matches(isDisplayed()))

        onDialogText(R.string.cancel).perform(click())

        // onDialogText would fail b/c root isDialog does not exist anymore
        onView(withText(android.R.string.untitled)).inRoot(any(Root::class.java))
            .check(doesNotExist())
        // cancelled, callback shouldn't have been called
        assertEquals(0, counter.get())
    }

    @Test
    fun doNotAskAgainDoesNotEditPreferenceOnCancel() {
        rule.scenario.onActivity { activity ->
            activity.showConfirmationDialogWithDoNotShowAgain(
                android.R.string.untitled,
                android.R.string.unknownName,
                preferences,
                PREF_KEY,
                incrementCounter,
            )
        }

        // tick the "do_not_show_again" checkbox
        onDialogText(R.string.do_not_show_again).perform(click()).check(matches(isChecked()))
        onDialogText(R.string.cancel).perform(click())
        assertEquals(0, counter.get())

        // on cancel, don't save in preferences
        assertEquals(preferences.getBoolean(PREF_KEY, true), true)
    }

    @Test
    fun doNotAskAgainSetsFlagToFalseIfCheckedOnConfirm() {
        rule.scenario.onActivity { activity ->
            activity.showConfirmationDialogWithDoNotShowAgain(
                android.R.string.untitled,
                android.R.string.unknownName,
                preferences,
                PREF_KEY,
                incrementCounter,
            )
        }

        onDialogText(R.string.do_not_show_again).perform(click()).check(matches(isChecked()))
        onDialogText(R.string.confirm).perform(click())
        assertEquals(1, counter.get())
        // on confirm, save in preferences
        assertEquals(preferences.getBoolean(PREF_KEY, true), false)
    }

    @Test
    fun doNotAskAgainSetsFlagToTrueIfNotCheckedOnConfirm() {
        rule.scenario.onActivity { activity ->
            activity.showConfirmationDialogWithDoNotShowAgain(
                android.R.string.untitled,
                android.R.string.unknownName,
                preferences,
                PREF_KEY,
                incrementCounter,
            )
        }

        onDialogText(R.string.do_not_show_again).perform(click(), click())
            .check(matches(not(isChecked())))
        onDialogText(R.string.confirm).perform(click())
        assertEquals(1, counter.get())
        // on confirm, save in preferences
        assertEquals(preferences.getBoolean(PREF_KEY, true), true)
    }

    // ---------------------------------------------------------------------------------------------
    // "Do not ask again" yes/no dialog
    @Test
    fun yesNoShowsTitleMessageYesNoCheckbox() {
        rule.scenario.onActivity { activity ->
            activity.showYesNoDialogWithDoNotAskAgain(
                android.R.string.untitled,
                android.R.string.unknownName,
                true,
                preferences,
                PREF_KEY,
                incrementOrDecrement,
            )
        }

        onDialogText(android.R.string.untitled).check(matches(isDisplayed()))
        onDialogText(android.R.string.unknownName).check(matches(isDisplayed()))
        onDialogText(R.string.yes).check(matches(isDisplayed()))
        onDialogText(R.string.no).check(matches(isDisplayed()))
        onDialogText(R.string.do_not_ask_again).check(matches(isDisplayed()))

        // clicking outside the dialog should cancel it
        onView(isRoot()).inRoot(isDialog()).perform(clickRelative(0, -100))

        // onDialogText would fail b/c root isDialog does not exist anymore
        onView(withText(android.R.string.untitled)).inRoot(any(Root::class.java))
            .check(doesNotExist())
        // cancelled, callback shouldn't have been called
        assertEquals(0, counter.get())
    }

    @Test
    fun yesNoRemembersYesIfChecked() {
        rule.scenario.onActivity { activity ->
            activity.showYesNoDialogWithDoNotAskAgain(
                android.R.string.untitled,
                android.R.string.unknownName,
                false,
                preferences,
                PREF_KEY,
                incrementOrDecrement,
            )
        }

        // clicking outside the dialog should not cancel it
        onView(isRoot()).inRoot(isDialog()).perform(clickRelative(0, -100))

        onDialogText(R.string.do_not_ask_again).perform(click()).check(matches(isChecked()))
        onDialogText(R.string.yes).perform(click())
        assertEquals(1, counter.get())

        // callback should be called again directly with the correct value
        rule.scenario.onActivity { activity ->
            activity.showYesNoDialogWithDoNotAskAgain(
                android.R.string.untitled,
                android.R.string.unknownName,
                false,
                preferences,
                PREF_KEY,
                incrementOrDecrement,
            )
        }
        assertEquals(2, counter.get())
    }

    @Test
    fun yesNoRemembersNoIfChecked() {
        rule.scenario.onActivity { activity ->
            activity.showYesNoDialogWithDoNotAskAgain(
                android.R.string.untitled,
                android.R.string.unknownName,
                false,
                preferences,
                PREF_KEY,
                incrementOrDecrement,
            )
        }

        // clicking outside the dialog should not cancel it
        onView(isRoot()).inRoot(isDialog()).perform(clickRelative(0, -100))

        onDialogText(R.string.do_not_ask_again).perform(click()).check(matches(isChecked()))
        onDialogText(R.string.no).perform(click())
        assertEquals(-1, counter.get())

        // callback should be called again directly with the correct value
        rule.scenario.onActivity { activity ->
            activity.showYesNoDialogWithDoNotAskAgain(
                android.R.string.untitled,
                android.R.string.unknownName,
                false,
                preferences,
                PREF_KEY,
                incrementOrDecrement,
            )
        }
        assertEquals(-2, counter.get())
    }
}
