package ch.epfl.toufi.android_utils.ui.activity

import android.content.Intent
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import ch.epfl.toufi.android_utils.PreferencesTestActivity
import ch.epfl.toufi.android_utils.PreferencesTestActivity.Companion.VALID_PREFIX
import ch.epfl.toufi.android_utils.ui.activity.PreferencesActivity.Companion.EXTRA_PREFERENCES_ID
import ch.epfl.toufi.android_utils.ui.activity.PreferencesActivity.Companion.EXTRA_PREFERENCES_IDS
import ch.epfl.toufi.android_utils.ui.activity.PreferencesActivity.Companion.EXTRA_TITLE
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.Test


class PreferencesActivityInstrumentedTest {

    private fun launchActivity(
        intent: Intent,
        testFun: (ActivityScenario<PreferencesTestActivity>) -> Unit,
    ) {
        ActivityScenario.launch<PreferencesTestActivity>(intent).use(testFun)
    }

    @Test
    fun singlePreferenceIDIsDisplayed() {
        val title = "Custom test title"
        val text = "${VALID_PREFIX}_1"
        val intent = Intent(getApplicationContext(), PreferencesTestActivity::class.java).apply {
            putExtra(EXTRA_PREFERENCES_ID, text)
            putExtra(EXTRA_TITLE, title)
        }

        launchActivity(intent) {
            onView(withText(text)).check(matches(isDisplayed()))
            onView(
                allOf(
                    instanceOf(TextView::class.java),
                    isDescendantOfA(withId(androidx.appcompat.R.id.action_bar)),
                )
            ).check(matches(withText(title)))
        }
    }

    @Test
    fun severalPreferencesIDsAreAllDisplayed() {
        val text1 = "${VALID_PREFIX}_1"
        val text2 = "not_valid_${VALID_PREFIX}_2"
        val text3 = "${VALID_PREFIX}_3"
        val intent = Intent(getApplicationContext(), PreferencesTestActivity::class.java).apply {
            putExtra(EXTRA_PREFERENCES_IDS, arrayListOf(text1, text2, text3))
        }

        launchActivity(intent) {
            onView(withText(text1)).check(matches(isDisplayed()))
            onView(withText(text2)).check(doesNotExist())
            onView(withText(text3)).check(matches(isDisplayed()))
        }
    }
}