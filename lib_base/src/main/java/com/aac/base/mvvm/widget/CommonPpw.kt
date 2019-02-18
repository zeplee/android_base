package com.aac.base.mvvm.widget

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.PopupWindow
import com.aac.base.mvvm.BaseVM
import com.aac.base.mvvm.page.BaseAty


/**
 * @author lizepeng
 * @time 2018/08/24 13：29
 * @desc 封装可以展示任意布局的自定义dialog
 */

class CommonPpw<VD : ViewDataBinding, VM : BaseVM> private constructor(builder: Builder<VD, VM>) : PopupWindow(builder.mBuildPageContext) {
    val mBinding: VD
    var mVM: VM

    var mPageContext: Context //activity的context，或application的
    val mHeightScale: Double
    val mWidthScale: Double
    var mCancelTouchout: Boolean//触摸空白是否可以消失 默认不消失
    var mCanBack: Boolean = true//点击返回键是否可以消失 默认不消失
    var mGravity: Int
    var mOnDismissListener: CommonPpw.CommonPpWDismissListener?

    init {
        mPageContext = builder.mBuildPageContext
        mHeightScale = builder.mBuildHeightScale
        mWidthScale = builder.mBuildWidthScale
        mGravity = builder.mBuildGravity
        mVM = builder.mBuildViewModel
        mBinding = builder.mBuildBinding
        mCanBack = builder.mBuildCanBack
        mCancelTouchout = builder.mBuildCanTouchout
        mOnDismissListener = builder.mBuildOnDismissListener
        setOnDismissListener { mOnDismissListener?.onDismiss() }
        contentView = mBinding.root
//        isOutsideTouchable = mCancelTouchout
        isFocusable = mCancelTouchout
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    interface CommonPpWDismissListener {
        fun onDismiss()
    }

    class Builder<VD : ViewDataBinding, VM : BaseVM>(val mBuildPageContext: Context) {
        @StyleRes
        lateinit var mBuildBinding: VD
        lateinit var mBuildViewModel: VM
        var mBuildHeightScale = 0.0
        var mBuildWidthScale = 0.0
        var mBuildGravity: Int = Gravity.CENTER
        var mBuildCanBack: Boolean = true
        var mBuildCanTouchout: Boolean = true
        var mBuildOnDismissListener: CommonPpw.CommonPpWDismissListener? = null

        fun setLayout(@LayoutRes resView: Int): Builder<VD, VM> {
            mBuildBinding = DataBindingUtil.inflate(LayoutInflater.from(mBuildPageContext), resView, null, false)
            return this
        }

        fun setViewModel(VMClass: Class<VM>): Builder<VD, VM> {
            mBuildViewModel = ViewModelProviders.of(mBuildPageContext as BaseAty<*, *>).get(VMClass)
            return this
        }

        /**
         * 必须先调用setViewModel传入，此方法可实现在dialog xml中使用viewmodel,不调用的话只可以在dialog类中使用
         */
        fun setLayoutVMId(layoutVMId: Int): Builder<VD, VM> {
            mBuildBinding.setVariable(layoutVMId, mBuildViewModel)
            //实现dialog中数据可以双向绑定
            mBuildBinding.setLifecycleOwner(mBuildPageContext as BaseAty<*, *>)
            return this
        }

        /**
         * 设置dialog弹出的位置 BOOTOM(下) CENTER(中) TOP(上) LEFT(左) RIGHT(右)
         *
         * @return
         */

        fun setGravity(gravity: Int): Builder<VD, VM> {
            this.mBuildGravity = gravity
            return this
        }

        /**
         * 设置dialog的位置大小1.设置dialog高度的屏占比2.设置dialog宽度的屏占比
         * 不设置size时，默认为布局中的size
         *
         * @param heightScale
         * @param widthScale
         * @return
         */
        fun setSize(heightScale: Double, widthScale: Double): Builder<VD, VM> {
            this.mBuildHeightScale = heightScale
            this.mBuildWidthScale = widthScale
            return this
        }

        /**
         * 触摸空白是否可以消失1.true 消失 2.false 不消失3.不传默认为false
         *
         * @param val
         * @return
         */
        fun setCanTouchout(canTouchout: Boolean): Builder<VD, VM> {
            this.mBuildCanTouchout = canTouchout
            return this
        }

        /**
         * 点击返回键dialog是否消失1.false 消失 2.true 不消失3.不传默认为false
         *
         * @param val
         * @return
         */
        fun setCanBack(canBack: Boolean): Builder<VD, VM> {
            this.mBuildCanBack = canBack
            return this
        }

        fun setOnDismissListener(onDismissListener: CommonPpw.CommonPpWDismissListener): Builder<VD, VM> {
            this.mBuildOnDismissListener = onDismissListener
            return this
        }

        fun build(): CommonPpw<VD, VM> {
            return CommonPpw(this)
        }
    }
}