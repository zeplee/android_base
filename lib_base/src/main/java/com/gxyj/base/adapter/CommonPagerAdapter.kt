//package com.gxyj.base.adapter
//
//import android.support.v4.view.PagerAdapter
//import android.view.View
//import android.view.ViewGroup
//import java.util.*
//
//open class SingleTypeVpAdapter(private val mList: ArrayList<*>) : PagerAdapter() {
//
//    override fun getCount(): Int {
//        return if (mList.isNullOrEmpty()) 0 else mList.size
//    }
//
//    override fun isViewFromObject(view: View, ob: Any): Boolean {
//        return view === ob
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, ob: Any) {
//        container.removeView(ob as View?)
//    }
//}