package ch.epfl.toufi.android_utils.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import ch.epfl.toufi.android_utils.R

abstract class PreferencesActivity : BackArrowActivity(R.layout.activity_preferences) {
    companion object {
        /**
         * Key for the Intent extra to pass a single [String],
         * the name of the [PreferenceFragmentCompat] to create and add.
         */
        const val PREFERENCES_ID: String = "preferences_activity_fragment_id"

        /**
         * Key for the Intent extra to pass an [ArrayList] of [String]s,
         * the names of all [PreferenceFragmentCompat] to create and add, in order.
         */
        const val PREFERENCES_IDS: String = "preferences_activity_fragment_ids"

        private const val PREFERENCES_FRAGMENT_TAG = "preferences_fragment"
    }

    private val fragments = ArrayList<Fragment>()

    /**
     * Returns the [PreferenceFragmentCompat] associated to the given id.
     * @param preferenceFragmentId String,
     * @return [PreferenceFragmentCompat], or null if the name is not recognized
     */
    abstract fun loadFragment(preferenceFragmentId: String): PreferenceFragmentCompat?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = intent.getStringArrayListExtra(PREFERENCES_IDS) ?: listOfNotNull(
            intent.getStringExtra(PREFERENCES_ID)
        )
        populateFragmentContainer(preferences)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        val preferences = intent.getStringArrayListExtra(PREFERENCES_IDS) ?: listOfNotNull(
            intent.getStringExtra(PREFERENCES_ID)
        )
        populateFragmentContainer(preferences)
    }

    private fun populateFragmentContainer(preferenceNames: List<String>) {
        supportFragmentManager.beginTransaction().apply {
            preferenceNames.forEachIndexed { i, pref ->
                val fragment = loadFragment(pref)
                if (fragment != null) {
                    fragments.add(fragment)
                    val tag = "${PREFERENCES_FRAGMENT_TAG}_$i"
                    add(R.id.fragment_container, fragment, tag)
                } else {
                    Log.e(
                        this::class.simpleName,
                        "Name ${preferenceNames[i]} not recognized. Fragment skipped."
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
