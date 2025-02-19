package com.example.fe.Home.Category

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CategoryItemDecoration(private val verticalSpace: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position != RecyclerView.NO_POSITION) {
            outRect.top = verticalSpace / 2  // 위쪽 간격
            outRect.bottom = verticalSpace / 2  // 아래쪽 간격
        }
    }
}
