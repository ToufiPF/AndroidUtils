package ch.epfl.toufi.android_utils

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import androidx.core.content.IntentCompat
import androidx.core.os.BundleCompat

object Extensions {

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
}