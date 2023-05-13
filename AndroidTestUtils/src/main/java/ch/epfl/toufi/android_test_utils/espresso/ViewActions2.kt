package ch.epfl.toufi.android_test_utils.espresso

import android.util.Log
import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.MotionEvents
import org.hamcrest.Matcher
import org.hamcrest.Matchers

object ViewActions2 {

    /**
     * Creates a action that clicks at the given position, relative to the matched [View].
     * @param x [Int] pixel position in window coordinates (left = 0, increases towards right)
     * @param y [Int] pixel position in window coordinates (top = 0, increases towards bottom)
     * @return [ViewAction] that can perform a click at the given relative position
     */
    fun clickRelative(x: Int, y: Int): ViewAction = object : ViewAction {
        override fun getDescription(): String = "Click at relative position ($x, $y)"
        override fun getConstraints(): Matcher<View> = Matchers.any(View::class.java)

        override fun perform(uiController: UiController?, view: View?) {
            uiController!!.loopMainThreadUntilIdle()

            val location = IntArray(2)
            view!!.getLocationOnScreen(location)

            val coordinates = FloatArray(2)
            coordinates[0] = (location[0] + x).toFloat()
            coordinates[1] = (location[1] + y).toFloat()
            val precision = FloatArray(2) { 1.0f }

            Log.i(
                this@ViewActions2::class.simpleName + "_clickAt",
                "Clicking at (${coordinates[0]}, ${coordinates[1]})",
            )

            uiController.loopMainThreadUntilIdle()
            val down = MotionEvents.sendDown(uiController, coordinates, precision).down
            uiController.loopMainThreadForAtLeast(200)
            MotionEvents.sendUp(uiController, down)
        }
    }
}
