package com.example.fe.Home.Category

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = space  // 아이템 오른쪽 간격 설정
        outRect.left = space   // 아이템 왼쪽 간격 설정
    }
}
