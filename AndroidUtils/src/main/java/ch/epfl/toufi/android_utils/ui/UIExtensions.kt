package ch.epfl.toufi.android_utils.ui

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.IntentCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.os.BundleCompat
import ch.epfl.toufi.android_utils.LogicExtensions.reduceAll

/**
 * Extension functions that need an Android runtime
 */
object UIExtensions {

    /**
     * Sets the whole [Editable]'s text to the given [string].
     * @param string the [String] to write into the [Editable]
     */
    fun Editable.set(string: String?) {
        replace(0, length, string ?: "")
    }

    /**
     * Returns the [Parcelable] extra corresponding to [key] in the [Intent].
     * @param key [String] the parcel key
     * @return [Parcelable] the parcel corresponding to [key]
     */
    inline fun <reified T> Intent.parcelable(key: String): T? =
        IntentCompat.getParcelableExtra(this, key, T::class.java)

    /**
     * Returns the [Parcelable] extra corresponding to [key] in the [Bundle].
     * @param key [String] the parcel key
     * @return [Parcelable] the parcel corresponding to [key]
     */
    inline fun <reified T> Bundle.parcelable(key: String): T? =
        BundleCompat.getParcelable(this, key, T::class.java)

    /**
     * Checks all the passed permissions and returns an array with the same length,
     * containing true if the corresponding permission is granted.
     *
     * @param permissions [String] one or several permissions to check for
     * @return [BooleanArray] of the same size as permission,
     * with each boolean set to true iff the corresponding permission is granted
     *
     * @see [reduceAll] to check all permissions at once
     */
    fun AppCompatActivity.checkHasPermissions(vararg permissions: String): BooleanArray {
        val results = BooleanArray(permissions.size)
        permissions.forEachIndexed { i, key ->
            results[i] = checkSelfPermission(this, key) == PERMISSION_GRANTED
        }
        return results
    }
}
