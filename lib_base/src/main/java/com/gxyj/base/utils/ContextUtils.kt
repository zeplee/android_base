package com.gxyj.base.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View

/**
 * 兼容部分低版本手机v的context不为activity的问题(偶现，并不是每个转换都有问题，神奇的bug)
 */
class ContextUtils {
    companion object {
        /**
         * 通过View获取到Activity
         */
        fun <T> getActivityFromView(view: View): T? {
            var context = view.context
            while (context is ContextWrapper) {
                if (context is Activity) {
                    return context as T
                }
                context = context.baseContext
            }
            return null
        }

        /**
         * 通过context获取到Activity
         */
        fun <T> getActivityFromContext(context: Context): T? {
            var context = context
            while (context is ContextWrapper) {
                if (context is Activity) {
                    return context as T
                }
                context = context.baseContext
            }
            return null
        }
    }
}
