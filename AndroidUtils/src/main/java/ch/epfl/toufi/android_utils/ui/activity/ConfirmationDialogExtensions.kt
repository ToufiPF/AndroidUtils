package ch.epfl.toufi.android_utils.ui.activity

import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.toufi.android_utils.R
import ch.epfl.toufi.android_utils.databinding.DialogDoNotShowAgainBinding

object ConfirmationDialogExtensions {

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
            setPositiveButton(R.string.confirm) { dialog, _ ->
                dialog.dismiss()
                onConfirm.invoke()
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
        }.create().apply {
            setCanceledOnTouchOutside(false)
        }.show()
    }

    /**
     * Shows a default confirmation dialog with a custom title and message,
     * "do not show again" checkbox, and "confirm" and "cancel" buttons.
     *
     * @param title [Int], R.string of the title to print
     * @param text [Int], R.string of the message to print
     * @param preferences [SharedPreferences] instance that will store whether to
     * show the confirmation dialog the next time
     * @param showConfirmationKey [String] where to store whether to show the confirmation dialog
     * the next time
     * @param onConfirm will be run only if the user presses "confirm",
     * or if "do not ask again" was previously checked
     */
    fun AppCompatActivity.showConfirmationDialogWithDoNotShowAgain(
        @StringRes title: Int,
        @StringRes text: Int,
        preferences: SharedPreferences,
        showConfirmationKey: String,
        onConfirm: () -> Unit
    ) {
        val showConfirmation = preferences.getBoolean(showConfirmationKey, true)
        if (showConfirmation) {
            val binding = DialogDoNotShowAgainBinding.inflate(layoutInflater)
            binding.messageTextView.text = getString(text)

            AlertDialog.Builder(this).apply {
                setTitle(title)
                setView(binding.root)

                setPositiveButton(R.string.confirm) { dialog, _ ->
                    dialog.dismiss()
                    preferences.edit()
                        .putBoolean(showConfirmationKey, !binding.checkboxDoNotShowAgain.isChecked)
                        .apply()
                    onConfirm.invoke()
                }
                setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }
            }.create().apply {
                setCanceledOnTouchOutside(false)
            }.show()
        } else {
            onConfirm.invoke()
        }
    }

    /**
     * Shows a default confirmation dialog with a custom title and message,
     * "do not ask again" checkbox, and "yes"/"no" buttons.
     *
     * Similar to [showConfirmationDialogWithDoNotShowAgain], but:
     * - displays yes/no buttons;
     * - remember user choice (yes/no) and doesn't prompt them if checkbox is checked, whatever the answer is.
     *
     * @param title [Int], R.string of the title to print
     * @param text [Int], R.string of the message to print
     * @param cancelableByTouchingOutside [Boolean],
     * whether the dialog can be cancelled by clicking outside of it
     * @param preferences [SharedPreferences] instance that will store whether to
     * show the confirmation dialog the next time
     * @param decisionPreferenceKey [String] where to store the decision
     * @param onDecided will be called back if the dialog was not canceled.
     * Parameter is true if user clicked on 'yes', false otherwise.
     */
    fun AppCompatActivity.showYesNoDialogWithDoNotAskAgain(
        @StringRes title: Int,
        @StringRes text: Int,
        cancelableByTouchingOutside: Boolean,
        preferences: SharedPreferences,
        decisionPreferenceKey: String,
        onDecided: (yes: Boolean) -> Unit,
    ) {
        val previousDecision = preferences.getInt(decisionPreferenceKey, -1)
        if (previousDecision == -1) {
            val binding = DialogDoNotShowAgainBinding.inflate(layoutInflater)
            binding.messageTextView.text = getString(text)
            binding.checkboxDoNotShowAgain.text = getString(R.string.do_not_ask_again)

            val callback = DialogInterface.OnClickListener { dialog, which ->
                val clickedYes = which == AlertDialog.BUTTON_POSITIVE
                dialog.dismiss()

                if (binding.checkboxDoNotShowAgain.isChecked) {
                    preferences.edit()
                        .putInt(decisionPreferenceKey, if (clickedYes) 1 else 0)
                        .apply()
                }
                onDecided(clickedYes)
            }

            AlertDialog.Builder(this).apply {
                setTitle(title)
                setView(binding.root)

                setPositiveButton(R.string.yes, callback)
                setNegativeButton(R.string.no, callback)
            }.create().apply {
                setCanceledOnTouchOutside(cancelableByTouchingOutside)
            }.show()
        } else {
            val decidedYes = previousDecision == 1
            onDecided(decidedYes)
        }
    }
}