package ch.epfl.toufi.android_utils.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.getLayoutDirection
import ch.epfl.toufi.android_utils.ui.view.ClickableDrawableMaterialEditText.Place.BOTTOM
import ch.epfl.toufi.android_utils.ui.view.ClickableDrawableMaterialEditText.Place.LEFT
import ch.epfl.toufi.android_utils.ui.view.ClickableDrawableMaterialEditText.Place.RIGHT
import ch.epfl.toufi.android_utils.ui.view.ClickableDrawableMaterialEditText.Place.TOP
import com.google.android.material.R
import com.google.android.material.textfield.TextInputEditText
import java.util.EnumMap

class ClickableDrawableMaterialEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle,
) : TextInputEditText(context, attrs, defStyleAttr) {

    private enum class Place {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM;
    }

    @Suppress("PrivatePropertyName")
    private val START: Place
        get() = if (getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) RIGHT else LEFT

    @Suppress("PrivatePropertyName")
    private val END: Place
        get() = if (getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) LEFT else RIGHT

    private val listeners: EnumMap<Place, OnClickListener?> = EnumMap(Place::class.java)

    init {
        Place.values().forEach { listeners[it] = null }
    }

    /**
     * Sets the [View.OnClickListener] on the top drawable.
     * @param onClick click listener. Set it to null to disable the previous listener.
     */
    fun setTopDrawableClickListener(onClick: OnClickListener?) {
        listeners[TOP] = onClick
    }

    /**
     * Sets the [View.OnClickListener] on the bottom drawable.
     * @param onClick click listener. Set it to null to disable the previous listener.
     */
    fun setBottomDrawableClickListener(onClick: OnClickListener?) {
        listeners[BOTTOM] = onClick
    }

    /**
     * Sets the [View.OnClickListener] on the left drawable.
     * Should not be used together with [setStartDrawableClickListener] / [setEndDrawableClickListener].
     * @param onClick click listener. Set it to null to disable the previous listener.
     */
    fun setLeftDrawableClickListener(onClick: OnClickListener?) {
        listeners[LEFT] = onClick
    }

    /**
     * Sets the [View.OnClickListener] on the right drawable.
     * Should not be used together with [setStartDrawableClickListener] / [setEndDrawableClickListener].
     * @param onClick click listener. Set it to null to disable the previous listener.
     */
    fun setRightDrawableClickListener(onClick: OnClickListener?) {
        listeners[RIGHT] = onClick
    }

    /**
     * Sets the [View.OnClickListener] on the start drawable (left or right depending on phone locale).
     * Should not be used together with [setLeftDrawableClickListener] / [setRightDrawableClickListener].
     * @param onClick click listener. Set it to null to disable the previous listener.
     */
    fun setStartDrawableClickListener(onClick: OnClickListener?) {
        listeners[START] = onClick
    }

    /**
     * Sets the [View.OnClickListener] on the end drawable (left or right depending on phone locale).
     * Should not be used together with [setLeftDrawableClickListener] / [setRightDrawableClickListener].
     * @param onClick click listener. Set it to null to disable the previous listener.
     */
    fun setEndDrawableClickListener(onClick: OnClickListener?) {
        listeners[END] = onClick
    }

    // performClick called in super.onTouchEvent
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            val halfWidth = width / 2
            val halfHeight = height / 2

            compoundDrawables[LEFT.ordinal]?.let { drawable ->
                listeners[LEFT]?.let { listener ->
                    if (paddingLeft <= event.x && event.x <= totalPaddingLeft
                        && halfHeight - drawable.bounds.height() <= event.y
                        && event.y <= halfHeight + drawable.bounds.height()
                    ) {
                        listener.onClick(this)
                        return true
                    }
                }
            }
            compoundDrawables[TOP.ordinal]?.let { drawable ->
                listeners[TOP]?.let { listener ->
                    if (paddingTop <= event.y && event.y <= totalPaddingTop
                        && halfWidth - drawable.bounds.width() <= event.x
                        && event.x <= halfWidth + drawable.bounds.width()
                    ) {
                        listener.onClick(this)
                        return true
                    }
                }
            }
            compoundDrawables[RIGHT.ordinal]?.let { drawable ->
                listeners[RIGHT]?.let { listener ->
                    if (width - totalPaddingRight <= event.x && event.x <= width - paddingRight
                        && halfHeight - drawable.bounds.height() <= event.y
                        && event.y <= halfHeight + drawable.bounds.height()
                    ) {
                        listener.onClick(this)
                        return true
                    }
                }
            }
            compoundDrawables[BOTTOM.ordinal]?.let { drawable ->
                listeners[BOTTOM]?.let { listener ->
                    if (height - totalPaddingBottom <= event.y && event.y <= height - paddingBottom
                        && halfWidth - drawable.bounds.width() <= event.x
                        && event.x <= halfWidth + drawable.bounds.width()
                    ) {
                        listener.onClick(this)
                        return true
                    }
                }
            }
        }

        return super.onTouchEvent(event)
    }
}
