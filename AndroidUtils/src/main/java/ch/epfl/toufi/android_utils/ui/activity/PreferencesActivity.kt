package ch.epfl.toufi.android_utils.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import ch.epfl.toufi.android_utils.R

/**
 * Base Activity that can inflate [PreferenceFragmentCompat]s.
 *
 * Subclasses should override [loadFragment]
 */
abstract class PreferencesActivity : BackArrowActivity(R.layout.activity_preferences) {
    companion object {
        private const val PREFERENCES_FRAGMENT_TAG = "preferences_fragment"

        /**
         * Key for the Intent extra to pass a single [String],
         * the name of the [PreferenceFragmentCompat] to create and add.
         */
        const val EXTRA_PREFERENCES_ID: String = "preferences_activity_fragment_id"

        /**
         * Key for the Intent extra to pass an [ArrayList] of [String]s,
         * the names of all [PreferenceFragmentCompat] to create and add, in order.
         */
        const val EXTRA_PREFERENCES_IDS: String = "preferences_activity_fragment_ids"

        /**
         * Key for the Intent extra containing an eventual title for the [PreferencesActivity]
         */
        const val EXTRA_TITLE: String = "preferences_activity_title"
    }

    private val fragments = ArrayList<Fragment>()

    /**
     * Returns the PreferenceFragment associated to the given id.
     *
     * Typical [PreferenceFragmentCompat] subclass should look like this:
     * ```kotlin
     *  class TestFragment : PreferenceFragmentCompat() {
     *      override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
     *          preferenceManager.sharedPreferencesName = PREFERENCE_NAME
     *          setPreferencesFromResource(R.xml.preference_test_fragment, rootKey)
     *      }
     *  }
     * ```
     * @param fragmentId String, name of the Fragment that should be loaded
     * @return [PreferenceFragmentCompat], or null if the name is not recognized
     */
    abstract fun loadFragment(fragmentId: String): PreferenceFragmentCompat?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        populateFragmentContainer()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        populateFragmentContainer()
    }

    private fun populateFragmentContainer() {
        intent.getStringExtra(EXTRA_TITLE)?.let { title = it }

        val preferences = intent.getStringArrayListExtra(EXTRA_PREFERENCES_IDS) ?: listOfNotNull(
            intent.getStringExtra(EXTRA_PREFERENCES_ID)
        )

        if (preferences.isEmpty()) {
            Log.e(this::class.simpleName, "EXTRA_PREFERENCES_ID(S) not specified")
            return
        }

        supportFragmentManager.beginTransaction().apply {
            preferences.forEachIndexed { i, pref ->
                val fragment = loadFragment(pref)
                if (fragment != null) {
                    fragments.add(fragment)
                    val tag = "${PREFERENCES_FRAGMENT_TAG}_$i"
                    add(R.id.fragment_container, fragment, tag)
                } else {
                    Log.e(
                        this::class.simpleName,
                        "Name ${preferences[i]} not recognized. Fragment skipped."
                    )
                }
            }
        }.commit()
    }
    /*
    TODO check whether the fragments are added twice upon e.g. screen rotation

        override fun onDestroy() {
            // Remove fragments added in onCreate,
            // otherwise there are duplicated on e.g. screen rotation
            supportFragmentManager.beginTransaction().apply {
                fragments.forEach { remove(it) }
            }.commit()
            fragments.clear()

            super.onDestroy()
        }
    */
}
