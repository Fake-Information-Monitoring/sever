package com.fake.information.sever.demo.Http.Api.Response

import com.google.gson.Gson

open class Result<T>(//是否成功
        var success: Boolean,//状态码
        var code: Int,//提示信息
        var msg: Any,//数据
        var data: T? = null
        ) {

    fun toJson():String{
        val gson = Gson()
        return gson.toJson(this)
    }

    companion object {
    }
}