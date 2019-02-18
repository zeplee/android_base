package com.aac.base.mvvm.widget

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatDialog
import android.view.*
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ScreenUtils
import com.aac.base.mvvm.BaseVM


/**
 * @author lizepeng
 * @time 2018/08/24 13：29
 * @desc 封装可以展示任意布局的自定义dialog
 */

class CommonDlg<VD : ViewDataBinding, VM : BaseVM> : AppCompatDialog {
    val mBinding: VD
    var mVM: VM

    var mPageContext: Context //只能是activity的context，不能使application的，不然会报错
    val mHeightScale: Double
    val mWidthScale: Double
    var mCancelTouchout: Boolean//触摸空白是否可以消失 默认不消失
    var mCanBack: Boolean = true//点击返回键是否可以消失 默认不消失
    var mGravity: Int
    var mOnDismissListener: CommonDialogDismissListener?
    /**
     * 返回键是否可以消失
     */
    private var mOnKeyListener: DialogInterface.OnKeyListener = DialogInterface.OnKeyListener { dialogInterface, i, keyEvent -> !mCanBack }

    private constructor(builder: Builder<VD, VM>) : super(builder.mBuildPageContext) {
        mPageContext = builder.mBuildPageContext
        mHeightScale = builder.mBuildHeightScale
        mWidthScale = builder.mBuildWidthScale
        mGravity = builder.mBuildGravity
        mVM = builder.mBuildViewModel
        mBinding = builder.mBuildBinding
        mCanBack = builder.mBuildCanBack
        mCancelTouchout = builder.mBuildCanTouchout
        mOnDismissListener = builder.mBuildOnDismissListener
    }

    private constructor(builder: Builder<VD, VM>, resStyle: Int) : super(builder.mBuildPageContext, resStyle) {
        mPageContext = builder.mBuildPageContext
        mHeightScale = builder.mBuildHeightScale
        mWidthScale = builder.mBuildWidthScale
        mGravity = builder.mBuildGravity
        mVM = builder.mBuildViewModel
        mBinding = builder.mBuildBinding
        mCanBack = builder.mBuildCanBack
        mCancelTouchout = builder.mBuildCanTouchout
        mOnDismissListener = builder.mBuildOnDismissListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setCanceledOnTouchOutside(mCancelTouchout)
        setOnKeyListener(mOnKeyListener)
        setOnDismissListener {
            //点击空白区域dialog消失但是软键盘不消失,部分手机也无效，覆写ontouch方法解决
//            if (shouldCloseOnTouch()) {
//                KeyboardUtils.hideSoftInput(currentFocus)
//            }
            mOnDismissListener?.onDismiss()
        }
        val lp = window!!.attributes
        lp.gravity = mGravity
        //不设置size时，默认为布局中的size,即包裹
        lp.height = if (mHeightScale == 0.0) WindowManager.LayoutParams.WRAP_CONTENT else (ScreenUtils.getScreenHeight() * mHeightScale).toInt()
        lp.width = if (mWidthScale == 0.0) WindowManager.LayoutParams.WRAP_CONTENT else (ScreenUtils.getScreenWidth() * mWidthScale).toInt()
        window!!.attributes = lp
    }

    //点击空白区域dialog消失但是软键盘不消失
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN
                && isOutOfBounds(event)
                && shouldCloseOnTouch()) {
            KeyboardUtils.hideSoftInput(currentFocus)
        }
        return super.onTouchEvent(event)
    }

    private fun shouldCloseOnTouch(): Boolean {
        return (isShowing && window?.peekDecorView() != null
                && currentFocus != null
                && currentFocus?.windowToken != null)
    }

    private fun isOutOfBounds(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        val slop = ViewConfiguration.get(context).scaledWindowTouchSlop
        val decorView = window!!.decorView
        return (x < -slop || y < -slop
                || x > decorView.width + slop
                || y > decorView.height + slop)
    }

    fun showOrDismiss(isShow: Boolean) {
        if (isShow) {
            show()
        } else {
            if (shouldCloseOnTouch()) {
                KeyboardUtils.hideSoftInput(currentFocus)
            }
            dismiss()
        }
    }

    interface CommonDialogDismissListener {
        fun onDismiss()
    }

    class Builder<VD : ViewDataBinding, VM : BaseVM>(val mBuildPageContext: Context) {
        @StyleRes
        var mBuildResStyle: Int? = null
        lateinit var mBuildBinding: VD
        lateinit var mBuildViewModel: VM
        var mBuildHeightScale = 0.0
        var mBuildWidthScale = 0.0
        var mBuildGravity: Int = Gravity.CENTER
        var mBuildCanBack: Boolean = true
        var mBuildCanTouchout: Boolean = true
        var mBuildOnDismissListener: CommonDialogDismissListener? = null

        fun setStyle(@StyleRes resStyle: Int): Builder<VD, VM> {
            this.mBuildResStyle = resStyle
            return this
        }

        fun setLayout(@LayoutRes resView: Int): Builder<VD, VM> {
            mBuildBinding = DataBindingUtil.inflate(LayoutInflater.from(mBuildPageContext), resView, null, false)
            return this
        }

        fun setViewModel(VMClass: Class<VM>): Builder<VD, VM> {
            mBuildViewModel = ViewModelProviders.of(mBuildPageContext as FragmentActivity).get(VMClass)
            return this
        }

        /**
         * 必须先调用setViewModel传入，此方法可实现在dialog xml中使用viewmodel,不调用的话只可以在dialog类中使用
         */
        fun setLayoutVMId(layoutVMId: Int): Builder<VD, VM> {
            mBuildBinding.setVariable(layoutVMId, mBuildViewModel)
            //实现dialog中数据可以双向绑定
            mBuildBinding.setLifecycleOwner(mBuildPageContext as FragmentActivity)
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

        fun setOnDismissListener(onDismissListener: CommonDialogDismissListener): Builder<VD, VM> {
            this.mBuildOnDismissListener = onDismissListener
            return this
        }

        fun build(): CommonDlg<VD, VM> {
            mBuildResStyle?.let {
                return CommonDlg(this, it)
            }
            return CommonDlg(this)
        }
    }
}