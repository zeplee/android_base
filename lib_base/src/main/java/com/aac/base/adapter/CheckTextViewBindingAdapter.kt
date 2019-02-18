package com.aac.base.adapter

//package com.aac.base.bindadapter
//
//import android.databinding.BindingAdapter
//import android.view.View
//import com.blankj.utilcode.util.Utils
//import com.aac.base.R
//
//object CheckTextViewBindingAdapter {
//    /**
//     * 命名空间不能app，因为如果是app的话，会从app module中找，结果会找不到
//     */
//    @BindingAdapter("bind:isChecked")
//    fun setIsChecked(view: View, isChecked: Boolean) {
//        if (isChecked) {
//            view.setBackgroundResource(R.drawable.ripple_bg)
//        } else {
//            view.setBackgroundColor(Utils.getApp().resources.getColor(android.R.color.holo_orange_light))
//        }
//    }
//}