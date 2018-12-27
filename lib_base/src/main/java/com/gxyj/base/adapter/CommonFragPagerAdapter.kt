//package com.gxyj.base.adapter
//
//import android.support.v4.app.Fragment
//import android.support.v4.app.FragmentManager
//import android.support.v4.app.FragmentPagerAdapter
//
///**
// * ***************************************
// * statement: 公用的BaseFragmentPagerAdapter
// * auther: lingguiqin
// * date created : 2017/4/15 0015
// * ***************************************
// */
//
//class CommonFragPagerAdapter(fm: FragmentManager,
//                             private val mFragList: List<Fragment>) : FragmentPagerAdapter(fm) {
//
//    override fun getItem(position: Int): Fragment {
//        return mFragList[position]
//    }
//
//    override fun getCount(): Int {
//        return if (mFragList.isNullOrEmpty()) 0 else mFragList.size
//    }
//}
