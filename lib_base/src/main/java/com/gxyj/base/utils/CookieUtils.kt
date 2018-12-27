package com.gxyj.base.utils

import android.os.Build
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.Utils
import com.gxyj.base.helper.urlmanager.UrlManager
import com.lzy.okgo.OkGo
import okhttp3.HttpUrl

class CookieUtils {

    companion object {
        @JvmField
        val KEY_TOKEN = "FSID"

        /**
         *  同步Token 即登录状态
         */
        fun syncToken(tokenValue: String) {
            //保存本地
            SPUtils.getInstance().put(KEY_TOKEN, tokenValue)
            //同步给okgo
            val cookies = OkGo.getInstance().cookieJar.cookieStore.getCookie(HttpUrl.parse(UrlManager.curEnvBean.appServer))
            //同步给webview要加载的url
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.createInstance(Utils.getApp())
            }
            val cookieManager = CookieManager.getInstance()
            cookieManager.setCookie(UrlManager.curEnvBean.appServer, "$KEY_TOKEN=$tokenValue")
        }


        /**
         * 移除Token
         */
        fun removeToken() {
            //移除本地的TOKEN
            SPUtils.getInstance().remove(KEY_TOKEN)
            //移除webview的coolie
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.createInstance(Utils.getApp())
            }
            val cookieManager = CookieManager.getInstance()
            cookieManager.removeSessionCookie()
            cookieManager.removeAllCookie()
//            if (Build.VERSION.SDK_INT < 21) {
//                CookieSyncManager.getInstance().sync()
//            } else {
//                CookieManager.getInstance().flush()
//            }
        }

        fun isLogin(): Boolean {
            return SPUtils.getInstance().getString(KEY_TOKEN).isNotEmpty()
        }
    }
}
