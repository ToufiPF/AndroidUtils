package ch.epfl.toufi.android_test_utils

import androidx.fragment.app.Fragment
import ch.epfl.toufi.android_test_utils.scenario.SafeFragmentScenario
import org.junit.Test

class SafeFragmentScenarioInstrumentedTest {
    class TestFragment : Fragment()

    @Test
    fun test() {
        SafeFragmentScenario.launchInRegularContainer<TestFragment> {
            print("ok")
        }
    }
}
