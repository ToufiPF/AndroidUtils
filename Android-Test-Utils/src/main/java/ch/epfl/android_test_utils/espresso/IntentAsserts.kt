package ch.epfl.android_test_utils.espresso

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers

object IntentAsserts {

    private const val BOOSTRAP_ACTIVITY_NAME =
        "androidx.test.core.app.InstrumentationActivityInvoker\$BootstrapActivity"

    /**
     * Asserts that there's no unverified intents, but ignores an eventual intent with component "BootstrapActivity"
     * which is the activity used to boot most tests.
     * @see Intents.assertNoUnverifiedIntents
     */
    fun assertNoUnverifiedIntentIgnoringBootstrap() {
        if (Intents.getIntents().find { it.component?.className == BOOSTRAP_ACTIVITY_NAME } != null)
            Intents.intended(IntentMatchers.hasComponent(BOOSTRAP_ACTIVITY_NAME))

        Intents.assertNoUnverifiedIntents()
    }
}