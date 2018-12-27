package com.gxyj.base.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.databinding.BindingAdapter
import android.view.View

object LoadingViewBindingAdapter {
    /**
     * 命名空间不能app，因为如果是app的话，会从app module中找，结果会找不到
     */
    @BindingAdapter("fadeVisible")
    fun setFadeVisible(view: View, visible: Boolean) {
        if (view.getTag() == null) {
            view.setTag(true)
            view.setVisibility(if (visible) View.VISIBLE else View.GONE)
        } else {
            view.animate().cancel()

            if (visible) {
                view.setVisibility(View.VISIBLE)
                view.setAlpha(0f)
                view.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.setAlpha(1f)
                    }
                })
            } else {
                view.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.setAlpha(1f)
                        view.setVisibility(View.GONE)
                    }
                })
            }
        }
    }
}