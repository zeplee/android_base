package com.aac.base.mvvm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.aac.base.helper.network.NetRequestHelper
import java.util.*


abstract class BaseVM : ViewModel() {
    val TAG = javaClass.name + UUID.randomUUID().toString()
    //true的时候弹出加载动画，false的时候关掉加载动画
    var mIsShowLoading = MutableLiveData<Boolean>()

    open fun afterInitData() {}

    override fun onCleared() {
        super.onCleared()
        NetRequestHelper.cancleRequest(TAG)
    }
}

class EmptyVM : BaseVM()