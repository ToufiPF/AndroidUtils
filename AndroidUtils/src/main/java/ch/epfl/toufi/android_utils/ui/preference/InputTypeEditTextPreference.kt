package ch.epfl.toufi.android_utils.ui.preference

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.EditTextPreference
import ch.epfl.toufi.android_utils.R

class InputTypeEditTextPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.editTextPreferenceStyle,
    defStyleRes: Int = 0,
) : EditTextPreference(context, attrs, defStyleAttr, defStyleRes) {

    var inputType: Int

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.InputTypeEditTextPreference,
            defStyleAttr,
            defStyleRes,
        ).let { array ->

            try {
                inputType = array.getInt(
                    R.styleable.InputTypeEditTextPreference_android_inputType,
                    EditorInfo.TYPE_NULL,
                )
            } finally {
                array.recycle()
            }
        }

        setOnBindEditTextListener {
            it.inputType = inputType
        }
    }
}