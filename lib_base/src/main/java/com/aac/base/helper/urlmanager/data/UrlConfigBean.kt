package com.aac.base.helper.urlmanager.data

import java.util.*

/**
 * Created by lizepeng on 2018/08/24.
 * Description：
 */

data class UrlConfigBean(var curEnvName: String,
                         var appEnvMaps: LinkedHashMap<String, AppEnvBean>,
                         var appUrlMaps: LinkedHashMap<String, UrlBean>) {

    data class AppEnvBean(var appServer: String
//                          var webServer: String
    ) {
        var isChecked: Boolean = false
    }

    data class UrlBean(var host: String,
                       var method: String,
                       var url: String,
                       var cacheTime: Long,
                       var cacheMode: String)
}
