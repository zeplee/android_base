package com.gxyj.base.mvvm.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.*

/**
 * https://github.com/baurine/multi-type-adapter/blob/master/note/multi-type-adapter-tutorial-2.md#%E4%BD%BF%E7%94%A8%E7%AF%87
 * 共用recyvlerview所在page或widget的主viewmodel
 * 这样刷新不会闪
 * if (mBinding.homeRv.itemAnimator is SimpleItemAnimator) {(mBinding.homeRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false}
 * notifyItemRangeChanged(0, mAdapter.mItems.size)
 */
open class MultiTypeRvAdapter : RecyclerView.Adapter<MultiTypeRvAdapter.ItemViewHolder>() {
    val mItems = ArrayList<RvItem>()

//    下拉刷新不闪烁的方法 https://blog.csdn.net/zhoulesin_dev/article/details/80183715
//    https://blog.csdn.net/donichen/article/details/71104506
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
// https://blog.csdn.net/qq_36523667/article/details/78736015
// 配合 mAdapter.setHasStableIds(true)

    override fun getItemCount(): Int = mItems.size

    override fun getItemViewType(position: Int): Int = mItems[position].mItemLayoutRes

    override fun onCreateViewHolder(parent: ViewGroup, itemLayoutRes: Int): ItemViewHolder {
        return ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), itemLayoutRes, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        mItems[position].mPostion = position
        onItemBind(holder.mItemBinding, mItems[position])
    }

    open fun onItemBind(itemBinding: ViewDataBinding, item: RvItem) {
        if (item.mItemLayoutVMId != -1) {
            itemBinding.setVariable(item.mItemLayoutVMId, item)
        }
        itemBinding.executePendingBindings()
    }

    class ItemViewHolder(val mItemBinding: ViewDataBinding) : RecyclerView.ViewHolder(mItemBinding.root)

    abstract class RvItem(
            // get the xml layout this type item used in
            @LayoutRes open val mItemLayoutRes: Int,
            // get the variable name in the xml
            val mItemLayoutVMId: Int = -1
//        val mItemVM: BaseVM? = null
//        var mItemPostion: Int = -1//相同类型条目中的相对位置
    ) {
        var mPostion: Int = -1//总集合中的绝对位置
    }

    // operate mItems
    fun getItems(): List<RvItem> {
        return mItems
    }

    fun findPos(item: RvItem): Int {
        return mItems.indexOf(item)
    }

    fun setItem(item: RvItem) {
        clearItems()
        addItem(item)
    }

    fun setItems(mItems: List<RvItem>) {
        clearItems()
        addItems(mItems)
    }

    fun addItem(item: RvItem) {
        mItems.add(item)
    }

    fun addItem(item: RvItem, index: Int) {
        mItems.add(index, item)
    }

    fun addItems(mItems: List<RvItem>) {
        this.mItems.addAll(mItems)
    }

    fun removeItem(item: RvItem): Int {
        val pos = findPos(item)
        mItems.remove(item)
        return pos
    }

    fun removeItem(index: Int) {
        mItems.removeAt(index)
    }

    fun clearItems() {
        mItems.clear()
    }
}

