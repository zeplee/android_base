package com.gxyj.base.helper

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.support.annotation.AnimRes
import android.support.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.gxyj.base.IS_DEBUG_MODE

/**
 * //部分手机如华为对xml中设置的转场动画无效，为了统一，所以手动设置
 */
class RouterHelper {
    companion object {
        @AnimRes
        var mEnterAnim: Int = 0
        @AnimRes
        var mExitAnim: Int = 0

        fun init(
            context: Application,
            @AnimRes enterAnim: Int,
            @AnimRes exitAnim: Int
        ) {
            mEnterAnim = enterAnim
            mExitAnim = exitAnim
            ARouter.init(context)
            if (IS_DEBUG_MODE) {
                ARouter.openLog()
                ARouter.openDebug()
                ARouter.printStackTrace()
            }
        }

        /**
         * 添加动画的话必须传入aty的context（v的context也是）,代表这个aty，不然默认是application，动画不生效
         */
        fun gotoAty(
            path: String,
            @AnimRes enterAnim: Int = mEnterAnim,
            @AnimRes exitAnim: Int = mEnterAnim
        ): Postcard {
            return ARouter.getInstance().build(path)
                .withTransition(enterAnim, exitAnim)
        }

        /**
         * 去单个实例的栈位置,并清除跳转之前的所有的栈
         * 参数通过onNewIntent接收
         */
        fun gotoAtySingleTask(
            path: String,
            @AnimRes enterAnim: Int = mEnterAnim,
            @AnimRes exitAnim: Int = mEnterAnim
        ): Postcard {
            return gotoAty(path, enterAnim, exitAnim)
                .withFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        fun gotoFrag(
            navController: NavController,
            @IdRes path: Int,
            bundle: Bundle? = null,
            @AnimRes enterAnim: Int = mEnterAnim,
            @AnimRes exitAnim: Int = mEnterAnim
        ) {
            navController.navigate(path, bundle, navOptions {
                anim {
                    enter = enterAnim
                    exit = exitAnim
                    popEnter = enterAnim
                    popExit = exitAnim
                }
            })
        }

        /**
         * 去单个实例的栈位置,并清除跳转之前的所有的栈
         * 如果要传参数通过aty的vm共享
         * 自动使用了去的时候的动画
         */
        fun gotoFragSingleTask(navController: NavController, @IdRes path: Int) {
            navController.popBackStack(path, false)
        }
    }
}