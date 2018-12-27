package com.gxyj.base.mvvm.page

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gxyj.base.helper.statusbar.StatusBarHelper
import com.gxyj.base.mvvm.BaseVM
import com.gxyj.base.widget.PageLoadDlg
import com.umeng.message.PushAgent

/**
 * 不使用databinding的双向绑定，使用viewmodel结合databing的，这样布局中只需写一个viewmodel变量
 */

abstract class BaseAty<VD : ViewDataBinding, VM : BaseVM> : AppCompatActivity() {
    abstract val mLayoutRes: Int
    abstract val mVMClass: Class<VM>
    //初始化ViewModel的id  BR的id,覆写与否决定是否在xml中使用viewmodel
    open val mLayoutVMId: Int? = null

    lateinit var mVM: VM
    lateinit var mBinding: VD

    lateinit var mStatusBarHelper: StatusBarHelper
    lateinit var mPageLoadDlg: PageLoadDlg
    private fun initSDK() {
        //Push后台进行日活统计及多维度推送的必调用方法
        PushAgent.getInstance(this).onAppStart()
    }

    private fun initCommonWidget() {
        mStatusBarHelper = StatusBarHelper(this)
        setOwnStatusBar()
        mPageLoadDlg = PageLoadDlg(this)
        mVM.mIsShowLoading.observe(this, Observer { out ->
            out?.let {
                mPageLoadDlg.showOrDismiss(it)
            }
        })
    }

    /**
     * 状态栏样式
     */
    open fun setOwnStatusBar() {
        mStatusBarHelper.setBarStyleFullScreenWithStatus(true)
    }

    abstract fun initData(savedInstanceState: Bundle?)

    private fun initMvvm() {
        //有了这行代码才能使用livedata进行双向绑定
        mBinding.setLifecycleOwner(this)
        //设置了这个xml中才可以用viewmodel
        mLayoutVMId?.let {
            mBinding.setVariable(it, mVM)
        }
    }

    abstract fun initView(savedInstanceState: Bundle?)

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSDK()
        mVM = ViewModelProviders.of(this).get(mVMClass)
        initData(savedInstanceState)
        mVM.afterInitData()
        mBinding = DataBindingUtil.setContentView(this, mLayoutRes)
        initMvvm()
        initCommonWidget()
        initView(savedInstanceState)
    }


    override fun onDestroy() {
        mBinding.unbind()
        mStatusBarHelper.destroy()
        super.onDestroy()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

//    @SuppressLint("MissingSuperCall")
//    override fun onSaveInstanceState(outState: Bundle) {
//        //暂时设置转屏、分屏重启app
//    }

    //原生点击空白区隐藏软键盘,但是设置之后同一个页面的两个editText会重复弹起键盘
//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            val v = currentFocus
//            if (isShouldHideKeyboard(v, ev)) {
//                KeyboardUtils.hideSoftInput(this)
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }
//
//    fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
//        if (v != null && (v is EditText)) {
//            val l = intArrayOf(0, 0)
//            v.getLocationInWindow(l)
//            val left = l[0]
//            val top = l[1]
//            val bottom = top + v.getHeight()
//            val right = left + v.getWidth()
//            return !(event.getX() > left && event.getX() < right
//                    && event.getY() > top && event.getY() < bottom);
//        }
//        return false;
//    }
}