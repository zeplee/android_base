package com.gxyj.base.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

/**
 * 解决rv 内外数据不一致造成的IndexOutOfBoundsException
 * https://www.jianshu.com/p/2eca433869e9
 * https://www.jianshu.com/p/840feaafc768
 * https://www.jianshu.com/p/654dac931667
 * 这里主要是配合notifyItemRangeChanged使用，这个虽然可以确保刷新不闪烁，但是会导致数据不一致
 */
class WrapContentLinearLayoutManager : LinearLayoutManager {

    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }
}