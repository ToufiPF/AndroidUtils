package ch.epfl.toufi.android_utils.ui.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.toufi.android_utils.BackArrowTestActivity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BackArrowActivityInstrumentedTest {

    @get:Rule
    val rule = ActivityScenarioRule(BackArrowTestActivity::class.java)

    @Test
    fun homeButtonIsDisplayedAndCallsPressedCallbacks() {
        onView(withContentDescription(androidx.appcompat.R.string.abc_action_bar_up_description))
            .check(matches(isDisplayed()))
            .perform(click())
        rule.scenario.onActivity { activity ->
            assertEquals(activity.backPressedCounter.get(), 1)
        }
    }

    @Test
    fun backButtonCallsBackPressedCallbacks() {
        pressBack()
        rule.scenario.onActivity { activity ->
            assertEquals(activity.backPressedCounter.get(), 1)
        }
    }
}