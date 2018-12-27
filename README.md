*api 'com.github.Li-Zepeng:android_aac_lib:v0.1.0'
*dependencies = [
*            //design lib包含Support v4 ； AppCompat v7；v7包下的recyclerview，无cardview
*            "design"               : "com.android.support:design:$version.supportVersion",
*            "constraint-layout"    : "com.android.support.constraint:constraint-layout:1.1.3",
*            "navigation-ui"        : "android.arch.navigation:navigation-ui-ktx:$version.naviVersion",
*            "navigation-fragment"  : "android.arch.navigation:navigation-fragment-ktx:$version.naviVersion",
*            "lifecycle-extensions" : "android.arch.lifecycle:extensions:$version.archVersion",
*//            "lifecycle-compiler"   : "android.arch.lifecycle:compiler:$version.archVersion",
*            //https://kotlinlang.org/docs/reference/
*            "kotlin"               : "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version.kotlinVersion",
*            "kotlin-reflect"       : "org.jetbrains.kotlin:kotlin-reflect:$version.kotlinVersion",
*//            "databinding-compiler" : "com.android.databinding:compiler:3.2.0-alpha10",
*            "room-runtime"         : "android.arch.persistence.room:runtime:$version.archVersion",
*            "room-compiler"        : "android.arch.persistence.room:compiler:$version.archVersion",
*            "multidex"             : "com.android.support:multidex:1.0.2",
*            //https://github.com/gyf-dev/ImmersionBar
*            "statusbar"            : "com.gyf.immersionbar:immersionbar:2.3.2",
*            //https://github.com/scwang90/SmartRefreshLayout
*            "refreshLayout"        : "com.scwang.smartrefresh:SmartRefreshLayout:1.1.0-alpha-18",
*//            https://github.com/ittianyu/BottomNavigationViewEx
*            "bottomNavigationView" : "com.github.ittianyu:BottomNavigationViewEx:2.0.2",
*            //https://github.com/youth5201314/banner
*            "banner"               : "com.youth.banner:banner:1.4.10",
*            //https://github.com/google/gson
*            "gson"                 : "com.google.code.gson:gson:2.8.5",
*            //https://github.com/alibaba/ARouter
*            "arouter"              : "com.alibaba:arouter-api:1.4.1",
*//            //https://github.com/bumptech/glide  http://m.blog.csdn.net/sinyu890807/article/details/78582548
*            "glide"                : "com.github.bumptech.glide:glide:4.8.0",
*//            //https://github.com/jeasonlzy/okhttp-OkGo
*            "okgo"                 : "com.lzy.net:okgo:3.0.4",
*            "okGoDown"             : "com.lzy.net:okserver:2.0.5",
*            //友盟 https://www.umeng.com/
*            "umeng_common"         : "com.umeng.umsdk:common:1.5.4",
*            "umeng_utdid"          : "com.umeng.umsdk:utdid:1.1.5.3",
*            "umengshare_sdk"       : "com.umeng.umsdk:share-core:6.9.4",
*            "umengshare_shareboard": "com.umeng.umsdk:share-board:6.9.4",
*            "umengshare_qq"        : "com.umeng.umsdk:share-qq:6.9.4",
*            "umengshare_wechat"    : "com.umeng.umsdk:share-wx:6.9.4",
*            "umengshare_sina"      : "com.umeng.umsdk:share-sina:6.9.4",
*            "umengpush_sdk"        : "com.umeng.umsdk:push:5.0.2",
*            //二维码
*            "zxing"                : "com.google.zxing:core:3.3.0",
*            //https://github.com/Blankj/AndroidUtilCode
*            "utilcode"             : "com.blankj:utilcode:1.22.7",
*            //https://bugly.qq.com/docs/user-guide/instruction-manual-android/?v=20180521124306
*            "bugly"                : "com.tencent.bugly:crashreport:latest.release",
*            //网络、数据库抓包 http://facebook.github.io/stetho/    https://www.jianshu.com/p/03da9f91f41f
*            "stetho"               : "com.facebook.stetho:stetho:latest.release",
*            "stethoOkgo"           : "com.facebook.stetho:stetho-okhttp3:latest.release",
*            //内存泄漏 https://github.com/square/leakcanary
*            "leakcanary-release"   : "com.squareup.leakcanary:leakcanary-android-no-op:latest.release",
*            "leakcanary-debug"     : "com.squareup.leakcanary:leakcanary-android:latest.release",
*    ]