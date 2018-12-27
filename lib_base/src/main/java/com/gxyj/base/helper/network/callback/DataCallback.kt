package com.gxyj.base.helper.network.callback

import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Progress
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request

/**
 * 不规范格式数据-原始数据-尽量不要使用，用它的子类
 */

abstract class DataCallback : StringCallback() {

    abstract fun onSucceed(dataJson: String)

    open fun onStart() {}

    override fun onFinish() {}

    open fun onError(code: Int, msg: String) {}

    final override fun onError(response: Response<String>) {
        super.onError(response)
//        LogUtils.eTag("NetRequestHelper", response.body())
        onError(-1, "获取数据出错")
    }


    final override fun onSuccess(response: Response<String>) {
//        LogUtils.eTag("NetRequestHelper", response.body())
        onSucceed(response.body())
    }

    final override fun onCacheSuccess(response: Response<String>) {
        super.onCacheSuccess(response)
        //缓存暂时也走成功，需要时可在成功里添加是否为缓存的标识
        onSuccess(response)
    }

    final override fun onStart(request: Request<String, out Request<*, *>>?) {
        onStart()
    }

    final override fun uploadProgress(progress: Progress?) {}
}
