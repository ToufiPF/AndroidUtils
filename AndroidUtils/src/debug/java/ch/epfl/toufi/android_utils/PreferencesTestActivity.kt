package ch.epfl.toufi.android_utils

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.get
import ch.epfl.toufi.android_utils.ui.activity.PreferencesActivity

class PreferencesTestActivity : PreferencesActivity() {
    companion object {
        const val PREFERENCE_NAME = "test_preferences"
        const val VALID_PREFIX = "test_fragment"
    }

    class TestFragment(private val text: String) : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            preferenceManager.sharedPreferencesName = PREFERENCE_NAME
            setPreferencesFromResource(R.xml.preference_test_fragment, rootKey)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            preferenceScreen[0].title = text
        }
    }

    override fun loadFragment(preferenceFragmentId: String): PreferenceFragmentCompat? =
        if (preferenceFragmentId.startsWith(VALID_PREFIX)) TestFragment(preferenceFragmentId)
        else null
}
