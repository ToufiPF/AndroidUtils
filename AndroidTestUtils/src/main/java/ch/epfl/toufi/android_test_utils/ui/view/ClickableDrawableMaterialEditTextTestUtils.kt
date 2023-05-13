package ch.epfl.toufi.android_test_utils.ui.view

import android.view.View
import android.widget.EditText
import androidx.core.view.ViewCompat.LAYOUT_DIRECTION_RTL
import androidx.core.view.ViewCompat.getLayoutDirection
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.MotionEvents
import androidx.test.espresso.matcher.ViewMatchers
import ch.epfl.toufi.android_test_utils.ui.view.ClickableDrawableMaterialEditTextTestUtils.CompoundDrawablePlace.BOTTOM
import ch.epfl.toufi.android_test_utils.ui.view.ClickableDrawableMaterialEditTextTestUtils.CompoundDrawablePlace.END
import ch.epfl.toufi.android_test_utils.ui.view.ClickableDrawableMaterialEditTextTestUtils.CompoundDrawablePlace.LEFT
import ch.epfl.toufi.android_test_utils.ui.view.ClickableDrawableMaterialEditTextTestUtils.CompoundDrawablePlace.RIGHT
import ch.epfl.toufi.android_test_utils.ui.view.ClickableDrawableMaterialEditTextTestUtils.CompoundDrawablePlace.START
import ch.epfl.toufi.android_test_utils.ui.view.ClickableDrawableMaterialEditTextTestUtils.CompoundDrawablePlace.TOP
import com.google.android.material.textfield.TextInputEditText
import org.hamcrest.Matcher
import org.hamcrest.Matchers

object ClickableDrawableMaterialEditTextTestUtils {

    enum class CompoundDrawablePlace {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        START,
        END;
    }

    /**
     * Returns a [ViewAction] that clicks on the desired compound drawable in an [EditText]
     */
    fun clickOnCompoundDrawable(where: CompoundDrawablePlace): ViewAction = object : ViewAction {
        override fun perform(uiController: UiController?, view: View?) {
            uiController!!.loopMainThreadUntilIdle()
            val v = view as EditText

            val halfWidth = v.width / 2
            val halfHeight = v.height / 2

            val location = IntArray(2)
            v.getLocationOnScreen(location)
            var x = location[0].toFloat()
            var y = location[1].toFloat()
            when (where) {
                LEFT -> {
                    x += (v.paddingLeft + v.totalPaddingLeft) / 2
                    y += halfHeight
                }

                TOP -> {
                    x += halfWidth
                    y += (v.paddingTop + v.totalPaddingTop) / 2
                }

                RIGHT -> {
                    x += v.width - (v.paddingRight + v.totalPaddingRight) / 2
                    y += halfHeight
                }

                BOTTOM -> {
                    x += halfWidth
                    y += v.height - (v.paddingBottom + v.totalPaddingBottom) / 2
                }

                // for start/end, determine the correct location and recurse
                START -> {
                    val place =
                        if (getLayoutDirection(view) == LAYOUT_DIRECTION_RTL) RIGHT else LEFT
                    clickOnCompoundDrawable(place).perform(uiController, view)
                    return
                }

                END -> {
                    val place =
                        if (getLayoutDirection(view) == LAYOUT_DIRECTION_RTL) LEFT else RIGHT
                    clickOnCompoundDrawable(place).perform(uiController, view)
                    return
                }
            }

            val coordinates = FloatArray(2)
            coordinates[0] = x
            coordinates[1] = y
            val precision = FloatArray(2) { 1.0f }

            uiController.loopMainThreadUntilIdle()
            val down = MotionEvents.sendDown(uiController, coordinates, precision).down
            uiController.loopMainThreadForAtLeast(200)
            MotionEvents.sendUp(uiController, down)
        }

        override fun getConstraints(): Matcher<View> = Matchers.allOf(
            Matchers.instanceOf(TextInputEditText::class.java),
            ViewMatchers.isDisplayed(),
        )

        override fun getDescription(): String =
            "Click on the ${where.name.lowercase()} compound drawable"
    }
}