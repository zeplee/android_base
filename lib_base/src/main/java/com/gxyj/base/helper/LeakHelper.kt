package com.gxyj.base.helper

import android.app.Application
import com.squareup.leakcanary.LeakCanary

class LeakHelper {
    companion object {
        fun init(context: Application) {
            LeakCanary.install(context);
        }
    }
}