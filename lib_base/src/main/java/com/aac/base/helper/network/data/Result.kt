package com.aac.base.helper.network.data

data class Result<C : Any>(
        var code: Int,
        var msg: String,
        var data: C)