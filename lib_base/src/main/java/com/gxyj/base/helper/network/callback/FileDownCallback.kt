package com.gxyj.base.helper.network.callback

import com.lzy.okgo.model.Progress
import com.lzy.okserver.download.DownloadListener

import java.io.File

/**
 * @author fengda
 * @time 2017/12/5 19:17
 * @desc 文件下载
 * @updateAuthor Author
 * @updateDate Date
 */

abstract class FileDownCallback(tag: Any) : DownloadListener(tag) {

    abstract fun onFinish(file: File)

    open fun onStart() {}

    open fun onError(msg: String) {}

    open fun onProgress(totalSize: Long, currentSize: Long, fraction: Float, speed: Long) {}

    final override fun onStart(progress: Progress) {
        onStart()
    }

    final override fun onProgress(progress: Progress) {
        onProgress(progress.totalSize, progress.currentSize, progress.fraction, progress.speed)
    }

    final override fun onError(progress: Progress) {
        onError("下载失败")
    }

    final override fun onFinish(file: File, progress: Progress) {
        onFinish(file)
    }

    final override fun onRemove(progress: Progress) {}
}
