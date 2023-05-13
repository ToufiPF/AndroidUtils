package ch.epfl.toufi.android_utils

import androidx.activity.OnBackPressedCallback
import ch.epfl.toufi.android_utils.ui.activity.BackArrowActivity
import java.util.concurrent.atomic.AtomicInteger

class BackArrowTestActivity : BackArrowActivity() {
    val backPressedCounter = AtomicInteger(0)

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backPressedCounter.incrementAndGet()
        }
    }

    override fun onStart() {
        super.onStart()
        onBackPressedDispatcher.addCallback(callback)
    }

    override fun onStop() {
        super.onStop()
        callback.remove()
    }
}
