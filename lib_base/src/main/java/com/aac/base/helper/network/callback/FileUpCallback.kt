package com.aac.base.helper.network.callback

import com.blankj.utilcode.util.LogUtils
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Progress
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request

/**
 *  上传一般只返回string状态，有其他类型需要的话可以加泛型拓展
 */

abstract class FileUpCallback : StringCallback() {

    abstract fun onSuccess()

    open fun onError(msg: String) {}

    open fun uploadProgress(currentSize: Long, totalSize: Long, fraction: Float, speed: Long) {}

    open fun onStart() {}

    override fun onFinish() {}

    final override fun onStart(request: Request<String, out Request<*, *>>?) {
        onStart()
    }

    final override fun onSuccess(response: Response<String>) {
        // todo
        if (response.body().contains("true")) {
            onSuccess()
        } else {
            onError("上传失败")
        }
        response.body().contains("true")

    }

    final override fun onError(response: Response<String>) {
        super.onError(response)
        LogUtils.eTag("NetRequestHelper", response.body())
        onError("上传失败")
    }

    final override fun uploadProgress(progress: Progress?) {
        super.uploadProgress(progress)
        progress?.let {
            uploadProgress(progress.currentSize, progress.totalSize, progress.fraction, progress.speed)
        }
        progress ?: onError("上传进度为null")
    }

    final override fun onCacheSuccess(response: Response<String>?) {
        super.onCacheSuccess(response)
    }
}
