package com.aac.base.helper.network.callback.datacallback

import com.aac.base.helper.GsonUtils
import com.aac.base.helper.network.callback.DataCallback
import com.aac.base.helper.network.data.Result

/**
 * 三段式规范合适数据
 */

abstract class ResultCallback<C : Any>(val mClazz: Class<C>) : DataCallback() {

    abstract fun onSuccess(data: C)

    final override fun onSucceed(json: String) {
        val result = GsonUtils.fromJson(json, Result::class.java)
        result?.let {
            if (it.code == 0) {
                val dataJson = GsonUtils.toJson(it.data)
                val data: C? = GsonUtils.fromJson(dataJson, mClazz)
                if (data != null) {
                    onSuccess(data)
                } else {
                    onError(it.code, it.msg)
                }
            } else {
                onError(it.code, it.msg)
            }
        }
        result ?: onError(-2, "Gson转换异常")
    }
}
