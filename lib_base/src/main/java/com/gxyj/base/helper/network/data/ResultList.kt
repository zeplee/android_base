package com.gxyj.base.helper.network.data

import java.util.*

data class ResultList<T>(
        var status: Boolean = false,
        var msg: String="获取数据出错",
        var data: ArrayList<T>)