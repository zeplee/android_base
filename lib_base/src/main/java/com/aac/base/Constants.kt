package com.aac.base

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils

//umeng推送使用
val SPKEY_DEVICETOKEN = "device_token"
//val SPKEY_COOKIE = "cookie"
//val SPKEY_COOKIE_TIME = "cookie_time"
val SPKEY_IS_DEBUG_MODE = "isDebugMode"
@JvmField
val IS_DEBUG_MODE: Boolean = AppUtils.isAppDebug() || SPUtils.getInstance().getBoolean(SPKEY_IS_DEBUG_MODE, false)
