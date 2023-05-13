package ch.epfl.android_test_utils.espresso

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

object EspressoMatchers {

    /**
     * Returns a [Matcher] that matches [View]s that have the background of the given color.
     */
    fun withBackgroundColor(@ColorInt color: Int): Matcher<View> = object : BaseMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("with background color 0x${color.toUInt().toString(16)}")
        }

        override fun matches(item: Any?): Boolean {
            return item is View && if (item is CardView) {
                @Suppress("RedundantNullableReturnType")
                val bg: ColorStateList? = item.cardBackgroundColor
                bg?.defaultColor == color
            } else {
                val bg = item.background as? ColorDrawable
                bg?.color == color
            }
        }
    }

    /**
     * Returns a [Matcher] that matches [View]s that have the background corresponding
     * to the given color resource id.
     */
    fun withBackgroundColorRes(@ColorRes res: Int): Matcher<View> {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val color = ResourcesCompat.getColor(context.resources, res, context.theme)
        return withBackgroundColor(color)
    }
}