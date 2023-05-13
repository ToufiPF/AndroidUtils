package ch.epfl.toufi.android_utils

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.os.BundleCompat
import ch.epfl.toufi.android_utils.databinding.DialogDoNotAskAgainBinding

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

    /**
     * Shows a default confirmation dialog with a custom title and message,
     * and "confirm" and "cancel" buttons.
     * @param title Int, R.string of the title to print
     * @param text Int, R.string of the message to print
     * @param onConfirm will be run only if the user presses "confirm".
     */
    fun AppCompatActivity.showConfirmationDialog(
        @StringRes title: Int,
        @StringRes text: Int,
        onConfirm: () -> Unit
    ) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(text)
            setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                onConfirm.invoke()
            }
            setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
        }.show()
    }

    /**
     * Shows a default confirmation dialog with a custom title and message,
     * "do not ask again" checkbox, and "confirm" and "cancel" buttons.
     * @param title [Int], R.string of the title to print
     * @param text [Int], R.string of the message to print
     * @param preferences [SharedPreferences] instance that will store whether to
     * show the confirmation dialog the next time
     * @param onConfirm will be run only if the user presses "confirm",
     * or if "do not ask again" was previously checked
     */
    fun AppCompatActivity.showConfirmationDialogWithDoNotAskAgain(
        @StringRes title: Int,
        @StringRes text: Int,
        preferences: SharedPreferences,
        showConfirmationFlag: String,
        onConfirm: () -> Unit
    ) {
        val showConfirmation = preferences.getBoolean(showConfirmationFlag, true)
        if (showConfirmation) {
            val binding = DialogDoNotAskAgainBinding.inflate(layoutInflater)
            binding.messageTextView.text = getString(text)

            AlertDialog.Builder(this).apply {
                setTitle(title)
                setView(binding.root)

                setPositiveButton(R.string.confirm) { dialog, _ ->
                    dialog.dismiss()
                    preferences.edit()
                        .putBoolean(showConfirmationFlag, !binding.checkboxDoNotAskAgain.isChecked)
                        .apply()
                    onConfirm.invoke()
                }
                setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }
            }.show()
        } else {
            onConfirm.invoke()
        }
    }
}