package com.aac.base.helper.network.callback.datacallback

import com.aac.base.helper.GsonUtils
import com.aac.base.helper.network.callback.DataCallback

/**
 * 格式不规范的后台数据-bean
 */

abstract class BeanCallback<T>(private val mClass: Class<T>) : DataCallback() {

    abstract fun onSuccess(data: T?)

    final override fun onSucceed(dataJson: String) {
        onSuccess(GsonUtils.fromJson(dataJson, mClass))
    }
}
