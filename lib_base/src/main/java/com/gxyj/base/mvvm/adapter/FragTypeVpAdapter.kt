package com.gxyj.base.mvvm.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * 简单单条目类型viewpager adapter
 * https://www.jianshu.com/p/4f28ef25c8f6
 * https://blog.csdn.net/android_cyw/article/details/54632112
 * https://blog.csdn.net/qq_36041639/article/details/69663504
 * https://blog.csdn.net/u013651026/article/details/83505724
 * https://blog.csdn.net/ShellJor/article/details/53728994
 */
class FragTypeVpAdapter(fm: FragmentManager,
                        private val mFrags: List<Fragment>)
    : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return if (mFrags.isNullOrEmpty()) 0 else mFrags.size
    }

    override fun getItem(position: Int): Fragment {
        return mFrags[position]
    }
}