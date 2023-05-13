package ch.epfl.toufi.android_utils.ui.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
        val position = parent.getChildAdapterPosition(view)
        outRect.apply {
            top =
                if (orientation == LinearLayoutManager.VERTICAL && position == 0) verticalSpace else 0
            bottom = verticalSpace

            left =
                if (orientation == LinearLayoutManager.HORIZONTAL && position == 0) horizontalSpace else 0
            right = horizontalSpace
        }
    }
}