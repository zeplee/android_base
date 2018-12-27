package com.gxyj.base

import android.app.Application
import android.os.Build
import android.support.multidex.MultiDex
import android.webkit.WebView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.gxyj.base.helper.RouterHelper
import com.gxyj.base.helper.network.NetRequestHelper

open class BaseApp : Application() {

    override fun attachBaseContext(base: android.content.Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        LogUtils.getConfig().setLogSwitch(IS_DEBUG_MODE)
        NetRequestHelper.init(this)
        RouterHelper.init(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(IS_DEBUG_MODE)
        }
    }
}