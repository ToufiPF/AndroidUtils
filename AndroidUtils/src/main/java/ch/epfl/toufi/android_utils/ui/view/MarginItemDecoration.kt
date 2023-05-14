package ch.epfl.toufi.android_utils.ui.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView

/**
 * Subclass of [RecyclerView.ItemDecoration] that introduces margin vertically/horizontally
 * depending on the orientation of the RecyclerView.
 */
class MarginItemDecoration(
    private val orientation: Int,
    private val verticalSpace: Int = 0,
    private val horizontalSpace: Int = 0,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (orientation !in setOf(VERTICAL, HORIZONTAL))
            return

        val position = parent.getChildAdapterPosition(view)
        outRect.apply {
            top =
                if (orientation == VERTICAL && position == 0) verticalSpace else 0
            bottom = verticalSpace

            left =
                if (orientation == HORIZONTAL && position == 0) horizontalSpace else 0
            right = horizontalSpace
        }
    }
}