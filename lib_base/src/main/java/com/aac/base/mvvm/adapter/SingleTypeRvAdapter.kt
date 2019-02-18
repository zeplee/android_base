package com.aac.base.mvvm.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.*

/**
 * 共用recyvlerview所在page或widget的主viewmodel
 */
open class SingleTypeRvAdapter<VD : ViewDataBinding>(private val mList: ArrayList<*>,
                                                     @LayoutRes private val mLayoutRes: Int)
    : RecyclerView.Adapter<SingleTypeRvAdapter.ItemViewHolder<VD>>() {

    override fun getItemCount(): Int = if (mList.isNullOrEmpty()) 0 else mList.size

    override fun onCreateViewHolder(parent: ViewGroup, type: Int)
            : ItemViewHolder<VD> = ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), mLayoutRes, parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder<VD>, position: Int) {
        onItemBind(holder.mHolderBinding, position)
    }

    open fun onItemBind(holderBinding: VD, position: Int) {
//        holderBinding.setVariable(item.mItemLayoutVMId, item.mItemVM)
//        holderBinding.executePendingBindings()
    }

    class ItemViewHolder<VD : ViewDataBinding>(val mHolderBinding: VD)
        : RecyclerView.ViewHolder(mHolderBinding.root)
}