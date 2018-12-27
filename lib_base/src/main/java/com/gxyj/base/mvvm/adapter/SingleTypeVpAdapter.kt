package com.gxyj.base.mvvm.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * 简单单条目类型viewpager adapter
 * https://www.jianshu.com/p/4f28ef25c8f6
 * https://blog.csdn.net/android_cyw/article/details/54632112
 * https://blog.csdn.net/qq_36041639/article/details/69663504
 * https://blog.csdn.net/u013651026/article/details/83505724
 * https://blog.csdn.net/ShellJor/article/details/53728994
 */
open class SingleTypeVpAdapter<VD : ViewDataBinding>(private val mList: ArrayList<*>,
                                                     @LayoutRes private val mLayoutRes: Int)
    : PagerAdapter() {

    override fun getCount(): Int = if (mList.isNullOrEmpty()) 0 else mList.size

    override fun isViewFromObject(view: View, ob: Any): Boolean {
        val binding = ob as ViewDataBinding
        return view === binding.root
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding: VD = DataBindingUtil.inflate(LayoutInflater.from(container.context), mLayoutRes, container, true)
//        binding.setVariable()
        onItemBind(binding, position)
        return binding
    }

    override fun destroyItem(container: ViewGroup, position: Int, ob: Any) {
        val binding = ob as ViewDataBinding
        container.removeView(binding.root)
    }

    open fun onItemBind(binding: VD, position: Int) {}
}