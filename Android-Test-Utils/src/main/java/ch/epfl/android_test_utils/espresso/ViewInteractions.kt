package ch.epfl.android_test_utils.espresso

import android.content.Context
import android.view.View
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import org.hamcrest.Matcher

object ViewInteractions {

    /**
     * Returns a [ViewInteraction] for a menu item matching [matcher].
     * Overflows the menu/action bar if needed
     * and then uses [Espresso.onView] with the given matcher.
     *
     * @param matcher [Matcher] menu item matcher
     * @return [ViewInteraction] for the given item.
     */
    fun onMenuItem(matcher: Matcher<View>): ViewInteraction {
        try {
            val context: Context = getApplicationContext()
            Espresso.openActionBarOverflowOrOptionsMenu(context)
        } catch (ignored: Exception) {
            // there may be no menu overflow, ignore
        }

        return Espresso.onView(matcher)
    }
}
