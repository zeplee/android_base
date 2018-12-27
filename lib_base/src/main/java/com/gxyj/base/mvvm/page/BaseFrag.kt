package com.gxyj.base.mvvm.page

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gxyj.base.mvvm.BaseVM


abstract class BaseFrag<VD : ViewDataBinding, VM : BaseVM> : Fragment() {
    abstract val mLayoutRes: Int
    abstract val mVMClass: Class<VM>
    //初始化ViewModel的id  BR的id,设置了这个xml中才可以用viewmodel
    open val mLayoutVMId: Int? = null

    lateinit var mVM: VM
    lateinit var mBinding: VD
    abstract fun initData(savedInstanceState: Bundle?)

    fun initMvvm() {
        //有了这行代码才能使用livedata进行双向绑定
        mBinding.setLifecycleOwner(this)
        //设置了这个xml中才可以用viewmodel
        mLayoutVMId?.let {
            mBinding.setVariable(it, mVM)
        }
    }

    abstract fun initView(contentView: View, savedInstanceState: Bundle?)

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mVM = ViewModelProviders.of(this).get(mVMClass)
        initData(savedInstanceState)
        mVM.mIsShowLoading = (context as BaseAty<*, *>).mVM.mIsShowLoading
        mVM.afterInitData()
//        if (rootView == null) {
//            rootView = mBinding.root
        mBinding = DataBindingUtil.inflate(inflater, mLayoutRes, container, false)
        initMvvm()
        initView(mBinding.root, savedInstanceState)
//        }
//        val parent: ViewGroup? = mBinding.root.parent as ViewGroup?
//        if (parent != null) {
//            parent.removeView(mBinding.root)
//        }
        return mBinding.root
    }

    /**
     * 控件是否初始化完成
     */
    var mIsViewCreated: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mIsViewCreated = true
    }

    /**
     * 在isAttach之前执行，此处可实现懒加载（可见时网络请求）和vp配合frag使用时真正的onresume
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (mIsViewCreated && isVisibleToUser) onVisible()
    }

    open fun onVisible() {}

    override fun onDestroy() {
        mBinding.unbind()
        super.onDestroy()
    }
}