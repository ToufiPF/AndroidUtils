package ch.epfl.toufi.android_utils.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.ID_NULL
import ch.epfl.toufi.android_utils.permissions.MockPermissionsActivity

/**
 * An activity that shows a back arrow in the action bar.
 * It executes the same action as pressing the physical back arrow of the phone
 * (ie. it calls [onBackPressed]).
 */
abstract class BackArrowActivity @JvmOverloads constructor(
    @LayoutRes layoutRes: Int = ID_NULL
) : MockPermissionsActivity(layoutRes) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        init()
    }

    private fun init() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home, androidx.appcompat.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}