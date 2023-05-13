package ch.epfl.toufi.android_utils.ui.view

import android.view.View
import androidx.test.espresso.Espresso.onIdle
import ch.epfl.toufi.android_test_utils.scenario.SafeViewScenario
import ch.epfl.toufi.android_test_utils.ui.view.ClickableDrawableMaterialEditTextTestUtils.CompoundDrawablePlace.BOTTOM
import ch.epfl.toufi.android_test_utils.ui.view.ClickableDrawableMaterialEditTextTestUtils.CompoundDrawablePlace.END
import ch.epfl.toufi.android_test_utils.ui.view.ClickableDrawableMaterialEditTextTestUtils.CompoundDrawablePlace.LEFT
import ch.epfl.toufi.android_test_utils.ui.view.ClickableDrawableMaterialEditTextTestUtils.CompoundDrawablePlace.RIGHT
import ch.epfl.toufi.android_test_utils.ui.view.ClickableDrawableMaterialEditTextTestUtils.CompoundDrawablePlace.START
import ch.epfl.toufi.android_test_utils.ui.view.ClickableDrawableMaterialEditTextTestUtils.CompoundDrawablePlace.TOP
import ch.epfl.toufi.android_test_utils.ui.view.ClickableDrawableMaterialEditTextTestUtils.clickOnCompoundDrawable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger


class ClickableDrawableMaterialEditTextInstrumentedTest {
    private val clicked = AtomicInteger(0)

    companion object {
        private val FOUR_PLACES = listOf(LEFT, TOP, RIGHT, BOTTOM)
        private val START_END_PLACES = listOf(START, END)
        private val ALL_PLACES = FOUR_PLACES + START_END_PLACES
    }

    @Before
    fun init() {
        clicked.set(0)
    }

    private fun runTest(testFunction: (SafeViewScenario<ClickableDrawableMaterialEditText>) -> Unit) {
        SafeViewScenario.launchInRegularContainer({ context ->
            ClickableDrawableMaterialEditText(context)
        }) { scenario ->
            scenario.onView { editText ->
                editText.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        android.R.drawable.ic_media_previous,
                        android.R.drawable.arrow_up_float,
                        android.R.drawable.ic_media_next,
                        android.R.drawable.arrow_down_float,
                    )
                    hint = "Test text"
                    text = null
                }
            }

            testFunction.invoke(scenario)
        }
    }

    private val listener = View.OnClickListener {
        clicked.incrementAndGet()
    }

    @Test
    fun topRightBottomActionsArePerformed() = runTest { scenario ->
        scenario.onView { editText ->
            editText.apply {
                setLeftDrawableClickListener(listener)
                setRightDrawableClickListener(listener)
                setTopDrawableClickListener(listener)
                setBottomDrawableClickListener(listener)
            }
        }

        FOUR_PLACES.forEach {
            clicked.set(0)
            scenario.onTestedView().perform(clickOnCompoundDrawable(it))
            onIdle()
            assertEquals(1, clicked.get())
        }
    }

    @Test
    fun startActionIsPerformedWhenClickingOnLeftDrawable() = runTest { scenario ->
        scenario.onView { editText ->
            editText.setStartDrawableClickListener(listener)
        }

        scenario.onTestedView().perform(clickOnCompoundDrawable(LEFT))
        onIdle()
        assertEquals(1, clicked.get())
    }

    @Test
    fun endActionIsPerformedWhenClickingOnRightDrawable() = runTest { scenario ->
        scenario.onView { editText ->
            editText.setEndDrawableClickListener(listener)
        }

        scenario.onTestedView().perform(clickOnCompoundDrawable(RIGHT))
        onIdle()
        assertEquals(1, clicked.get())
    }

    @Test
    fun noActionPerformedWhenNoDrawable() = runTest { scenario ->
        scenario.onView { editText ->
            editText.apply {
                setLeftDrawableClickListener(listener)
                setTopDrawableClickListener(listener)
                setRightDrawableClickListener(listener)
                setBottomDrawableClickListener(listener)

                setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
        }

        ALL_PLACES.forEach {
            scenario.onTestedView().perform(clickOnCompoundDrawable(it))
            onIdle()
            assertEquals(0, clicked.get())
        }
    }

    @Test
    fun noActionPerformedWhenNoListener() = runTest { scenario ->
        ALL_PLACES.forEach {
            scenario.onTestedView().perform(clickOnCompoundDrawable(it))
            onIdle()
            assertEquals(0, clicked.get())
        }
    }
}
